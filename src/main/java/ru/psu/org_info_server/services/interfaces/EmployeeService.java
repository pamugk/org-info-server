package ru.psu.org_info_server.services.interfaces;

import ru.psu.org_info_server.model.dto.*;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    UUID createEmployee(EmployeeDto newEmployee);
    void deleteEmployee(UUID id);
    EmployeeDto getEmployeeInfo(UUID id);
    ListChunk<EmployeeInfoDto> getEmployeeList(
            Number limit, Number offset,
            String search, String organization,
            UUID exclude, UUID orgId);
    Tree<EmployeeDto> getEmployeeTree(UUID rootId, Number limit, Number offset);
    void updateEmployee(EmployeeDto updatedEmployee);
}
