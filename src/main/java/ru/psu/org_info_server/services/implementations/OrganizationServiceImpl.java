package ru.psu.org_info_server.services.implementations;

import org.jooq.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import ru.psu.org_info_server.exceptions.HasChildrenException;
import ru.psu.org_info_server.exceptions.NotFoundException;
import ru.psu.org_info_server.exceptions.UnacceptableParamsException;
import ru.psu.org_info_server.model.dto.*;
import ru.psu.org_info_server.model.persistence.tables.Organizations;
import ru.psu.org_info_server.model.persistence.tables.records.OrganizationsRecord;
import ru.psu.org_info_server.services.interfaces.OrganizationService;

import static org.jooq.impl.DSL.*;
import static ru.psu.org_info_server.model.persistence.tables.Employees.EMPLOYEES;
import static ru.psu.org_info_server.model.persistence.tables.Organizations.ORGANIZATIONS;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
public class OrganizationServiceImpl implements OrganizationService {
    private final DSLContext context;

    public OrganizationServiceImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public UUID createOrganization(OrganizationDto newOrganization) {
        if (newOrganization.getParent() != null && Validator.organizationNotFound(context, newOrganization.getParent()))
            throw new UnacceptableParamsException("Головная организация не найдена");
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
                    "У организации ещё есть дочерние элементы (организации или сотрудники)");
        context.deleteFrom(ORGANIZATIONS).where(ORGANIZATIONS.ID.eq(id)).execute();
    }

    @Override
    public OrganizationDto getOrganizationInfo(UUID id) {
        OrganizationDto result = context
                .select()
                .from(ORGANIZATIONS)
                .where(ORGANIZATIONS.ID.eq(id))
                .fetchOneInto(OrganizationDto.class);
        if (result == null)
            throw new NotFoundException("Запрашиваемая организация не найдена");
        return result;
    }

    @Override
    @Transactional
    public ListChunk<OrgInfoDto> getOrganizationList(Number limit, Number offset, String search, UUID exclude) {
        Condition selectCondition = ORGANIZATIONS.NAME.contains(search);
        if (exclude != null)
            selectCondition = selectCondition.and(not(ORGANIZATIONS.ID.eq(exclude)));
        int count = context.selectCount().from(ORGANIZATIONS).where(selectCondition).fetchOne(0, int.class);
        CommonTableExpression<Record> childrenOrgs = name("childrenOrgs").as(
                select().from(ORGANIZATIONS)
                .where(selectCondition)
                .limit(limit).offset(offset)
        );
            Field<UUID> childId = childrenOrgs.field(ORGANIZATIONS.ID);
            Field<String> childName = childrenOrgs.field(ORGANIZATIONS.NAME);
            Field<UUID> childParent = childrenOrgs.field(ORGANIZATIONS.PARENT);
        Table<OrganizationsRecord> parentOrgs = ORGANIZATIONS.as("parentOrgs");
            Field<UUID> parentId = parentOrgs.field(ORGANIZATIONS.ID);
            Field<String> parentName = parentOrgs.field(ORGANIZATIONS.NAME);
        List<OrgInfoDto> chunk = context
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
        return ListChunk.<OrgInfoDto>builder().totalCount(count).dataChunk(chunk).build();
    }

    @Override
    @Transactional
    public List<TreeNode<OrganizationDto>> getOrganizationTree(UUID rootId) {
        if (rootId != null && Validator.organizationNotFound(context, rootId))
            throw new NotFoundException("Головная организация не найдена");
        Organizations parentOrgs = ORGANIZATIONS.as("parentOrgs");
        Condition parentCondition = rootId == null ? parentOrgs.PARENT.isNull() : parentOrgs.PARENT.eq(rootId);
        return context
               .select(parentOrgs.ID, parentOrgs.NAME,
                       field(exists(selectFrom(ORGANIZATIONS).where(parentOrgs.ID.eq(ORGANIZATIONS.PARENT)))))
               .from(parentOrgs)
               .where(parentCondition)
               .orderBy(parentOrgs.ID)
               .fetchStream().map(
                        record -> TreeNode.<OrganizationDto>builder().value(
                                OrganizationDto.builder().id(record.value1()).name(record.value2()).parent(rootId).build()
                        ).hasChildren(record.value3()).build()
               ).collect(Collectors.toList());
    }

    @Override
    public void updateOrganization(OrganizationDto updatedOrganization) {
        if (Validator.organizationNotFound(context, updatedOrganization.getId()))
            throw new NotFoundException("Обновляемая организация не найдена");
        if (updatedOrganization.getParent() != null) {
            if (updatedOrganization.getId().equals(updatedOrganization.getParent()))
                throw new UnacceptableParamsException("Организация не может являться головной для самой себя, это как-то странно...");
            if (Validator.organizationNotFound(context, updatedOrganization.getParent()))
                throw new UnacceptableParamsException("Головная организация не найдена");
        }
        context.update(ORGANIZATIONS)
                .set(ORGANIZATIONS.NAME, updatedOrganization.getName())
                .set(ORGANIZATIONS.PARENT, updatedOrganization.getParent())
                .where(ORGANIZATIONS.ID.eq(updatedOrganization.getId()));
    }
}
