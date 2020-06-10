package ru.psu.org_info_server.services.interfaces;

import ru.psu.org_info_server.model.dto.EmployeeDto;
import ru.psu.org_info_server.model.dto.EmployeeInfoDto;
import ru.psu.org_info_server.model.dto.ListChunk;

import java.util.UUID;

public interface EmployeeService {
    UUID createEmployee(EmployeeDto newEmployee);
    void deleteEmployee(UUID id);
    ListChunk<EmployeeInfoDto> getEmployeeList(Number limit, Number offset, String search);
    void getEmployeeTree();
    void updateEmployee(EmployeeDto updatedEmployee);
}
