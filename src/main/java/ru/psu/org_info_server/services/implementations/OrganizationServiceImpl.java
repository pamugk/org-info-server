package ru.psu.org_info_server.services.implementations;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import ru.psu.org_info_server.model.dto.OrganizationDto;
import ru.psu.org_info_server.services.interfaces.OrganizationService;

import java.util.UUID;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final DSLContext context;

    public OrganizationServiceImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public void createOrganization(OrganizationDto newOrganization) {

    }

    @Override
    public void getOrganizationList() {

    }

    @Override
    public void getOrganizationTree() {

    }

    @Override
    public void removeOrganization(UUID id) {

    }

    @Override
    public void updateOrganization(OrganizationDto updatedOrganization) {

    }
}
