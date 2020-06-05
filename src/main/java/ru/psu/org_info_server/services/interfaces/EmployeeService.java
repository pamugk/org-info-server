package ru.psu.org_info_server.services.interfaces;

import ru.psu.org_info_server.model.dto.EmployeeDto;

import java.util.UUID;

public interface EmployeeService {
    void createEmployee(EmployeeDto newEmployee);
    void getEmployeeList();
    void getEmployeeTree();
    void removeEmployee(UUID id);
    void updateEmployee(EmployeeDto updatedEmployee);
}
