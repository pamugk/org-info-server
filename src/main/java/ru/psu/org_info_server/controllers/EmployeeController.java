package ru.psu.org_info_server.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.psu.org_info_server.model.dto.EmployeeDto;
import ru.psu.org_info_server.model.dto.EmployeeInfoDto;
import ru.psu.org_info_server.model.dto.Response;
import ru.psu.org_info_server.model.transfer.Exists;
import ru.psu.org_info_server.model.transfer.New;
import ru.psu.org_info_server.services.interfaces.EmployeeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public Response<UUID> createEmployee(@Validated(New.class) @RequestBody EmployeeDto newEmployee) {
        return Response.<UUID>builder().data(service.createEmployee(newEmployee)).build();
    }

    @DeleteMapping
    public void deleteEmployee(@RequestParam UUID id) {
        service.deleteEmployee(id);
    }

    @GetMapping("/getList")
    public Response<List<EmployeeInfoDto>> getEmployeeList() {
        return Response.<List<EmployeeInfoDto>>builder().data(service.getEmployeeList()).build();
    }

    @GetMapping("/getTree")
    public void getEmployeeTree() {
        service.getEmployeeTree();
    }

    @PutMapping
    public void updateEmployee(@Validated(Exists.class) @RequestBody EmployeeDto updatedEmployee) {
        service.updateEmployee(updatedEmployee);
    }
}
