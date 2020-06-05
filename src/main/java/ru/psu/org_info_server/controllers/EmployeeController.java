package ru.psu.org_info_server.controllers;

import org.springframework.web.bind.annotation.*;
import ru.psu.org_info_server.model.dto.EmployeeDto;
import ru.psu.org_info_server.services.interfaces.EmployeeService;

import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public void createEmployee(@RequestBody EmployeeDto newEmployee) {
        service.createEmployee(newEmployee);
    }

    @GetMapping("/getList")
    public void getEmployeeList() {
        service.getEmployeeList();
    }

    @GetMapping("/getTree")
    public void getEmployeeTree() {
        service.getEmployeeTree();
    }

    @DeleteMapping("/remove")
    public void removeEmployee(@RequestParam(value = "id") UUID employeeId) {
        service.removeEmployee(employeeId);
    }

    @PutMapping("/update")
    public void updateEmployee(@RequestBody EmployeeDto updatedEmployee) {
        service.updateEmployee(updatedEmployee);
    }
}
