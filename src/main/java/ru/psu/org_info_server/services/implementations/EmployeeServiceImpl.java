package ru.psu.org_info_server.services.implementations;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.springframework.stereotype.Service;
import ru.psu.org_info_server.exceptions.HasChildrenException;
import ru.psu.org_info_server.exceptions.NotFoundException;
import ru.psu.org_info_server.exceptions.UnacceptableParamsException;
import ru.psu.org_info_server.model.dto.EmployeeDto;
import ru.psu.org_info_server.model.dto.EmployeeInfoDto;
import ru.psu.org_info_server.model.persistence.tables.records.EmployeesRecord;
import ru.psu.org_info_server.services.interfaces.EmployeeService;

import static ru.psu.org_info_server.model.persistence.tables.Employees.EMPLOYEES;
import static ru.psu.org_info_server.model.persistence.tables.Organizations.ORGANIZATIONS;

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
        if (newEmployee.getChief() != null && Validator.employeeNotFound(context, newEmployee.getChief()))
            throw new NotFoundException("Chief not found");
        if (Validator.organizationNotFound(context, newEmployee.getOrganization()))
            throw new UnacceptableParamsException("Organization not found");
        return context.insertInto(EMPLOYEES)
                .set(EMPLOYEES.NAME, newEmployee.getName())
                .set(EMPLOYEES.ORGANIZATION, newEmployee.getOrganization())
                .set(EMPLOYEES.CHIEF, newEmployee.getChief())
                .returning(EMPLOYEES.ID).fetchOne().getId();
    }

    @Override
    public void deleteEmployee(UUID id) {
        if (Validator.employeeNotFound(context, id))
            throw new NotFoundException("Employee not found");
        if (Validator.employeeHasChildren(context, id))
            throw new HasChildrenException("Employee still has subordinates");
        context.deleteFrom(EMPLOYEES).where(EMPLOYEES.ID.eq(id));
    }

    @Override
    public List<EmployeeInfoDto> getEmployeeList() {
        Table<EmployeesRecord> subordinates = EMPLOYEES.as("subordinates");
            Field<UUID> subId = subordinates.field(EMPLOYEES.ID);
            Field<String> subName = subordinates.field(EMPLOYEES.NAME);
            Field<UUID> subOrgId = subordinates.field(EMPLOYEES.ORGANIZATION);
            Field<UUID> subChief = subordinates.field(EMPLOYEES.CHIEF);
        Table<EmployeesRecord> chiefs = EMPLOYEES.as("chiefs");
            Field<UUID> chiefId = chiefs.field(EMPLOYEES.ID);
            Field<String> chiefName = chiefs.field(EMPLOYEES.NAME);
        return context
                .select(subId, subName,
                        subChief, chiefName.as("chiefName"),
                        subOrgId, ORGANIZATIONS.NAME.as("organizationName"))
                .from(subordinates)
                    .join(ORGANIZATIONS).on(subOrgId.eq(ORGANIZATIONS.ID))
                    .leftJoin(chiefs).on(subChief.eq(chiefId))
                .fetchInto(EmployeeInfoDto.class);
    }

    @Override
    public void getEmployeeTree() {

    }

    @Override
    public void updateEmployee(EmployeeDto updatedEmployee) {
        if (Validator.employeeNotFound(context, updatedEmployee.getId()))
            throw new NotFoundException("Employee not found");
        if (updatedEmployee.getChief() != null && Validator.employeeNotFound(context, updatedEmployee.getChief()))
            throw new NotFoundException("Chief not found");
        if (Validator.organizationNotFound(context, updatedEmployee.getOrganization()))
            throw new UnacceptableParamsException("Organization not found");
        context.update(EMPLOYEES)
                .set(EMPLOYEES.NAME, updatedEmployee.getName())
                .set(EMPLOYEES.ORGANIZATION, updatedEmployee.getChief())
                .set(EMPLOYEES.CHIEF, updatedEmployee.getChief());
    }
}
