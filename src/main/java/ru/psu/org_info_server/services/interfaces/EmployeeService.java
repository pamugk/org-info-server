package ru.psu.org_info_server.services.interfaces;

import ru.psu.org_info_server.model.dto.EmployeeDto;
import ru.psu.org_info_server.model.dto.EmployeeInfoDto;
import ru.psu.org_info_server.model.dto.ListChunk;
import ru.psu.org_info_server.model.dto.TreeNode;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    UUID createEmployee(EmployeeDto newEmployee);
    void deleteEmployee(UUID id);
    EmployeeDto getEmployeeInfo(UUID id);
    ListChunk<EmployeeInfoDto> getEmployeeList(Number limit, Number offset, String search, UUID[] exclude);
    List<TreeNode<EmployeeDto>> getEmployeeTree(UUID rootId);
    void updateEmployee(EmployeeDto updatedEmployee);
}
