package ru.psu.org_info_server.services.implementations;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import ru.psu.org_info_server.model.dto.EmployeeDto;
import ru.psu.org_info_server.services.interfaces.EmployeeService;

import static ru.psu.org_info_server.model.persistence.tables.Employees.EMPLOYEES;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final DSLContext context;

    public EmployeeServiceImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public UUID createEmployee(EmployeeDto newEmployee) {
        return context.insertInto(EMPLOYEES)
                .set(EMPLOYEES.NAME, newEmployee.getName())
                .set(EMPLOYEES.ORGANIZATION, newEmployee.getOrganization())
                .set(EMPLOYEES.CHIEF, newEmployee.getChief())
                .returning(EMPLOYEES.ID).fetchOne().getId();
    }

    @Override
    public void deleteEmployee(UUID id) {
        context.deleteFrom(EMPLOYEES).where(EMPLOYEES.ID.eq(id));
    }

    @Override
    public List<EmployeeDto> getEmployeeList() {
        return context.select().from(EMPLOYEES).fetchInto(EmployeeDto.class);
    }

    @Override
    public void getEmployeeTree() {

    }

    @Override
    public void updateEmployee(EmployeeDto updatedEmployee) {
        context.update(EMPLOYEES)
                .set(EMPLOYEES.NAME, updatedEmployee.getName())
                .set(EMPLOYEES.ORGANIZATION, updatedEmployee.getChief())
                .set(EMPLOYEES.CHIEF, updatedEmployee.getChief());
    }
}
