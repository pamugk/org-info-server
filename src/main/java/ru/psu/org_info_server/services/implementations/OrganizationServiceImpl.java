package ru.psu.org_info_server.services.implementations;

import org.jooq.*;
import org.springframework.stereotype.Service;
import ru.psu.org_info_server.exceptions.HasChildrenException;
import ru.psu.org_info_server.exceptions.NotFoundException;
import ru.psu.org_info_server.exceptions.UnacceptableParamsException;
import ru.psu.org_info_server.model.dto.OrgInfoDto;
import ru.psu.org_info_server.model.dto.OrganizationDto;
import ru.psu.org_info_server.model.persistence.tables.records.OrganizationsRecord;
import ru.psu.org_info_server.services.interfaces.OrganizationService;

import static org.jooq.impl.DSL.*;
import static ru.psu.org_info_server.model.persistence.tables.Employees.EMPLOYEES;
import static ru.psu.org_info_server.model.persistence.tables.Organizations.ORGANIZATIONS;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final DSLContext context;

    public OrganizationServiceImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public UUID createOrganization(OrganizationDto newOrganization) {
        if (newOrganization.getParent() != null && Validator.organizationNotFound(context, newOrganization.getParent()))
            throw new UnacceptableParamsException("Parent organization not found");
        return context.insertInto(ORGANIZATIONS)
                .set(ORGANIZATIONS.NAME, newOrganization.getName())
                .set(ORGANIZATIONS.PARENT, newOrganization.getParent())
                .returning(ORGANIZATIONS.ID).fetchOne().getId();
    }

    @Override
    public void deleteOrganization(UUID id) {
        if (Validator.organizationNotFound(context, id))
            throw new NotFoundException("Organization not found");
        if (Validator.organizationHasChildren(context, id))
            throw new HasChildrenException(
                    "Organization still has children elements (either employees or other organizations)");
        context.deleteFrom(ORGANIZATIONS).where(ORGANIZATIONS.ID.eq(id));
    }

    @Override
    public List<OrgInfoDto> getOrganizationList(Number limit, Number offset, String search) {
        CommonTableExpression<Record> childrenOrgs = name("childrenOrgs").as(
                select().from(ORGANIZATIONS)
                .where(ORGANIZATIONS.NAME.contains(search))
                .limit(limit).offset(offset)
        );
            Field<UUID> childId = childrenOrgs.field(ORGANIZATIONS.ID);
            Field<String> childName = childrenOrgs.field(ORGANIZATIONS.NAME);
            Field<UUID> childParent = childrenOrgs.field(ORGANIZATIONS.PARENT);
        Table<OrganizationsRecord> parentOrgs = ORGANIZATIONS.as("parentOrgs");
            Field<UUID> parentId = parentOrgs.field(ORGANIZATIONS.ID);
            Field<String> parentName = parentOrgs.field(ORGANIZATIONS.NAME);
        return context
                .with(childrenOrgs)
                .select(childId, childName,
                        parentId.as("parentId"), parentName.as("parentName"),
                        count(EMPLOYEES.ID).as("employeeCount"))
                .from(childrenOrgs)
                    .leftJoin(parentOrgs).on(childParent.eq(parentId))
                    .leftJoin(EMPLOYEES).on(childId.eq(EMPLOYEES.ORGANIZATION))
                .groupBy(childId, childName, parentId, parentName)
                .orderBy(childId)
                .fetchInto(OrgInfoDto.class);
    }

    @Override
    public void getOrganizationTree() {

    }

    @Override
    public void updateOrganization(OrganizationDto updatedOrganization) {
        if (Validator.organizationNotFound(context, updatedOrganization.getId()))
            throw new NotFoundException("Organization not found");
        if (updatedOrganization.getParent() != null
                && Validator.organizationNotFound(context, updatedOrganization.getParent()))
            throw new UnacceptableParamsException("Parent organization not found");
        context.update(ORGANIZATIONS)
                .set(ORGANIZATIONS.NAME, updatedOrganization.getName())
                .set(ORGANIZATIONS.PARENT, updatedOrganization.getParent())
                .where(ORGANIZATIONS.ID.eq(updatedOrganization.getId()));
    }
}
