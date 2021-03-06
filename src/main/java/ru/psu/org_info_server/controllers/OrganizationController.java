package ru.psu.org_info_server.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.psu.org_info_server.model.dto.*;
import ru.psu.org_info_server.model.transfer.Exists;
import ru.psu.org_info_server.model.transfer.New;
import ru.psu.org_info_server.services.interfaces.OrganizationService;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@Validated
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

    @GetMapping
    public Response<OrganizationDto> getInfo(@RequestParam UUID id) {
        return Response.<OrganizationDto>builder().data(service.getOrganizationInfo(id)).build();
    }

    @GetMapping("/list")
    public Response<ListChunk<OrgInfoDto>> getOrganizationList(@RequestParam(defaultValue = "0") @Min(0) int offset,
                                                               @RequestParam Optional<@Min(0) Integer> limit,
                                                               @RequestParam(defaultValue = "") String search,
                                                               @RequestParam(required = false) UUID exclude) {
        return Response.<ListChunk<OrgInfoDto>>builder()
                .data(service.getOrganizationList(limit.orElse(null), offset, search, exclude)).build();
    }

    @GetMapping("/tree")
    public Response<Tree<OrganizationDto>> getOrganizationTree(@RequestParam(required = false) UUID id) {
        return Response.<Tree<OrganizationDto>>builder().data(service.getOrganizationTree(id)).build();
    }

    @PutMapping
    public void updateOrganization(@Validated(Exists.class) @RequestBody OrganizationDto updatedOrganization) {
        service.updateOrganization(updatedOrganization);
    }
}
