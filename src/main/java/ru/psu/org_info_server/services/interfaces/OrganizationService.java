package ru.psu.org_info_server.services.interfaces;

import ru.psu.org_info_server.model.dto.*;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    UUID createOrganization(OrganizationDto newOrganization);
    void deleteOrganization(UUID id);
    OrganizationDto getOrganizationInfo(UUID id);
    ListChunk<OrgInfoDto> getOrganizationList(Number limit, Number offset, String search, UUID exclude);
   Tree<OrganizationDto> getOrganizationTree(UUID rootId);
    void updateOrganization(OrganizationDto updatedOrganization);
}
