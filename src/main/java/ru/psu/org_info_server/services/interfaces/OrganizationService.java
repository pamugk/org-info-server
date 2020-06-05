package ru.psu.org_info_server.services.interfaces;

import ru.psu.org_info_server.model.dto.OrganizationDto;

import java.util.UUID;

public interface OrganizationService {
    void createOrganization(OrganizationDto newOrganization);
    void getOrganizationList();
    void getOrganizationTree();
    void removeOrganization(UUID id);
    void updateOrganization(OrganizationDto updatedOrganization);
}
