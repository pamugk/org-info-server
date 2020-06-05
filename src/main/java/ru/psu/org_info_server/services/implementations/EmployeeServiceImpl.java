package ru.psu.org_info_server.services.implementations;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import ru.psu.org_info_server.model.dto.EmployeeDto;
import ru.psu.org_info_server.services.interfaces.EmployeeService;

import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final DSLContext context;

    public EmployeeServiceImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public void createEmployee(EmployeeDto newEmployee) {

    }

    @Override
    public void getEmployeeList() {

    }

    @Override
    public void getEmployeeTree() {

    }

    @Override
    public void removeEmployee(UUID id) {

    }

    @Override
    public void updateEmployee(EmployeeDto updatedEmployee) {

    }
}
