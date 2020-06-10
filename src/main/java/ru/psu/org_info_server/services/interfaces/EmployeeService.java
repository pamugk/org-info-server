package ru.psu.org_info_server.services.interfaces;

import ru.psu.org_info_server.model.dto.EmployeeDto;
import ru.psu.org_info_server.model.dto.EmployeeInfoDto;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    UUID createEmployee(EmployeeDto newEmployee);
    void deleteEmployee(UUID id);
    List<EmployeeInfoDto> getEmployeeList(Number limit, Number offset, String search);
    void getEmployeeTree();
    void updateEmployee(EmployeeDto updatedEmployee);
}
