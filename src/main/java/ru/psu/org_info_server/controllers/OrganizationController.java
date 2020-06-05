package ru.psu.org_info_server.controllers;

import org.springframework.web.bind.annotation.*;
import ru.psu.org_info_server.model.dto.OrganizationDto;
import ru.psu.org_info_server.model.dto.Response;
import ru.psu.org_info_server.services.interfaces.OrganizationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    private final OrganizationService service;

    public OrganizationController(OrganizationService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public Response<UUID> createOrganization(@RequestBody OrganizationDto newOrganization) {
        return Response.<UUID>builder().data(service.createOrganization(newOrganization)).build();
    }

    @GetMapping("/getList")
    public Response<List<OrganizationDto>> getOrganizationList() {
        return Response.<List<OrganizationDto>>builder().data(service.getOrganizationList()).build();
    }

    @GetMapping("/getTree")
    public void getOrganizationTree() {
        service.getOrganizationTree();
    }

    @DeleteMapping("/remove")
    public void removeOrganization(@RequestBody UUID organizationId) {
        service.deleteOrganization(organizationId);
    }

    @PutMapping("/update")
    public void updateOrganization(@RequestBody OrganizationDto updatedOrganization) {
        service.updateOrganization(updatedOrganization);
    }
}
