package ru.psu.org_info_server.services.implementations;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import ru.psu.org_info_server.exceptions.HasChildrenException;
import ru.psu.org_info_server.exceptions.NotFoundException;
import ru.psu.org_info_server.exceptions.UnacceptableParamsException;
import ru.psu.org_info_server.model.dto.OrganizationDto;
import ru.psu.org_info_server.services.interfaces.OrganizationService;

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
    public List<OrganizationDto> getOrganizationList() {
        return context.select().from(ORGANIZATIONS).fetchInto(OrganizationDto.class);
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
