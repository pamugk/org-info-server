package ru.psu.org_info_server.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.psu.org_info_server.model.dto.OrganizationDto;
import ru.psu.org_info_server.model.dto.Response;
import ru.psu.org_info_server.model.dto.transfer.Exists;
import ru.psu.org_info_server.model.dto.transfer.New;
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

    @PostMapping
    public Response<UUID> createOrganization(@Validated(New.class) @RequestBody OrganizationDto newOrganization) {
        return Response.<UUID>builder().data(service.createOrganization(newOrganization)).build();
    }

    @DeleteMapping
    public void deleteOrganization(@RequestParam UUID id) {
        service.deleteOrganization(id);
    }

    @GetMapping("/getList")
    public Response<List<OrganizationDto>> getOrganizationList() {
        return Response.<List<OrganizationDto>>builder().data(service.getOrganizationList()).build();
    }

    @GetMapping("/getTree")
    public void getOrganizationTree() {
        service.getOrganizationTree();
    }

    @PutMapping
    public void updateOrganization(@Validated(Exists.class) @RequestBody OrganizationDto updatedOrganization) {
        service.updateOrganization(updatedOrganization);
    }
}
