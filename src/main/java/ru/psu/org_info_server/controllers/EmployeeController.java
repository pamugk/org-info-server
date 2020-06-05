package ru.psu.org_info_server.controllers;

import org.springframework.web.bind.annotation.*;
import ru.psu.org_info_server.model.dto.EmployeeDto;
import ru.psu.org_info_server.model.dto.Response;
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

    @PostMapping("/add")
    public Response<UUID> createEmployee(@RequestBody EmployeeDto newEmployee) {
        return Response.<UUID>builder().data(service.createEmployee(newEmployee)).build();
    }

    @GetMapping("/getList")
    public Response<List<EmployeeDto>> getEmployeeList() {
        return Response.<List<EmployeeDto>>builder().data(service.getEmployeeList()).build();
    }

    @GetMapping("/getTree")
    public void getEmployeeTree() {
        service.getEmployeeTree();
    }

    @DeleteMapping("/remove")
    public void removeEmployee(@RequestParam(value = "id") UUID employeeId) {
        service.deleteEmployee(employeeId);
    }

    @PutMapping("/update")
    public void updateEmployee(@RequestBody EmployeeDto updatedEmployee) {
        service.updateEmployee(updatedEmployee);
    }
}
