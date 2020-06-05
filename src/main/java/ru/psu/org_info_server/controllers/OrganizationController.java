package ru.psu.org_info_server.controllers;

import org.springframework.web.bind.annotation.*;
import ru.psu.org_info_server.model.dto.OrganizationDto;
import ru.psu.org_info_server.services.interfaces.OrganizationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    private final OrganizationService service;

    public OrganizationController(OrganizationService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public void createOrganization(@RequestBody OrganizationDto newOrganization) {
        service.createOrganization(newOrganization);
    }

    @GetMapping("/getList")
    public void getOrganizationList() {
        service.getOrganizationList();
    }

    @GetMapping("/getTree")
    public void getOrganizationTree() {
        service.getOrganizationTree();
    }

    @DeleteMapping("/remove")
    public void removeOrganization(@RequestBody UUID organizationId) {
        service.removeOrganization(organizationId);
    }

    @PutMapping("/update")
    public void updateOrganization(@RequestBody OrganizationDto updatedOrganization) {
        service.updateOrganization(updatedOrganization);
    }
}
