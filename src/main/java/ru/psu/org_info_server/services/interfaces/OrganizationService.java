package ru.psu.org_info_server.services.interfaces;

import ru.psu.org_info_server.model.dto.OrgInfoDto;
import ru.psu.org_info_server.model.dto.OrganizationDto;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    UUID createOrganization(OrganizationDto newOrganization);
    void deleteOrganization(UUID id);
    List<OrgInfoDto> getOrganizationList(Number limit, Number offset, String search);
    void getOrganizationTree();
    void updateOrganization(OrganizationDto updatedOrganization);
}
