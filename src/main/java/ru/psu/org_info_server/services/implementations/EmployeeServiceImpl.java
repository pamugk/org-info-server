package ru.psu.org_info_server.services.implementations;

import org.jooq.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import ru.psu.org_info_server.exceptions.HasChildrenException;
import ru.psu.org_info_server.exceptions.NotFoundException;
import ru.psu.org_info_server.exceptions.UnacceptableParamsException;
import ru.psu.org_info_server.model.dto.*;
import ru.psu.org_info_server.model.persistence.tables.Employees;
import ru.psu.org_info_server.model.persistence.tables.records.EmployeesRecord;
import ru.psu.org_info_server.services.interfaces.EmployeeService;

import static org.jooq.impl.DSL.*;
import static ru.psu.org_info_server.model.persistence.tables.Employees.EMPLOYEES;
import static ru.psu.org_info_server.model.persistence.tables.Organizations.ORGANIZATIONS;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
public class EmployeeServiceImpl implements EmployeeService {
    private final DSLContext context;

    public EmployeeServiceImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public UUID createEmployee(EmployeeDto newEmployee) {
        if (Validator.organizationNotFound(context, newEmployee.getOrganization()))
            throw new UnacceptableParamsException("Organization not found");
        if (newEmployee.getChief() != null) {
            if (Validator.employeeNotFound(context, newEmployee.getChief()))
                throw new NotFoundException("Chief not found");
            if (Validator.employeeNotInOrganization(context, newEmployee.getChief(), newEmployee.getOrganization()))
                throw new NotFoundException("Chief not in the same organization");
        }
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
    @Transactional
    public ListChunk<EmployeeInfoDto> getEmployeeList(Number limit, Number offset, String search) {
        int count = context.selectCount().from(EMPLOYEES).where(EMPLOYEES.NAME.contains(search)).fetchOne(0, int.class);
        CommonTableExpression<Record> subordinates = name("subordinates").as(
                select().from(EMPLOYEES)
                .where(EMPLOYEES.NAME.contains(search))
                .limit(limit).offset(offset)
        );
            Field<UUID> subId = subordinates.field(EMPLOYEES.ID);
            Field<String> subName = subordinates.field(EMPLOYEES.NAME);
            Field<UUID> subOrgId = subordinates.field(EMPLOYEES.ORGANIZATION);
            Field<UUID> subChief = subordinates.field(EMPLOYEES.CHIEF);
        Table<EmployeesRecord> chiefs = EMPLOYEES.as("chiefs");
            Field<UUID> chiefId = chiefs.field(EMPLOYEES.ID);
            Field<String> chiefName = chiefs.field(EMPLOYEES.NAME);
        List<EmployeeInfoDto> chunk = context
                .with(subordinates)
                .select(subId, subName, subChief, chiefName.as("chiefName"),
                        subOrgId, ORGANIZATIONS.NAME.as("organizationName"))
                .from(subordinates)
                    .join(ORGANIZATIONS).on(subOrgId.eq(ORGANIZATIONS.ID))
                    .leftJoin(chiefs).on(subChief.eq(chiefId))
                .orderBy(subId)
                .fetchInto(EmployeeInfoDto.class);
        return ListChunk.<EmployeeInfoDto>builder().totalCount(count).dataChunk(chunk).build();
    }

    @Override
    @Transactional
    public List<TreeNode<EmployeeDto>> getEmployeeTree(UUID rootId) {
        if (rootId != null && Validator.employeeNotFound(context, rootId))
            throw new NotFoundException("Chief not found");
        Employees chiefs = EMPLOYEES.as("chiefs");
        Condition chiefCondition = rootId == null ? chiefs.CHIEF.isNull() : chiefs.CHIEF.eq(rootId);
        return context
                .select(chiefs.ID, chiefs.NAME, chiefs.ORGANIZATION,
                        field(exists(selectFrom(EMPLOYEES).where(chiefs.ID.eq(EMPLOYEES.CHIEF)))))
                .from(chiefs)
                .where(chiefCondition)
                .orderBy(chiefs.ID)
                .fetchStream().map(
                        record -> TreeNode.<EmployeeDto>builder().value(
                                EmployeeDto.builder().id(record.value1())
                                        .name(record.value2()).organization(record.value3()).chief(rootId).build()
                        ).hasChildren(record.value4()).build()
                ).collect(Collectors.toList());
    }

    @Override
    public void updateEmployee(EmployeeDto updatedEmployee) {
        if (Validator.employeeNotFound(context, updatedEmployee.getId()))
            throw new NotFoundException("Employee not found");
        if (updatedEmployee.getChief() != null) {
            if (Validator.employeeNotFound(context, updatedEmployee.getChief()))
                throw new UnacceptableParamsException("Chief not found");
            if (Validator.employeeNotInOrganization(context, updatedEmployee.getChief(), updatedEmployee.getOrganization()))
                throw new UnacceptableParamsException("Chief not in the same organization");
        }
        if (Validator.organizationNotFound(context, updatedEmployee.getOrganization()))
            throw new UnacceptableParamsException("Organization not found");
        context.update(EMPLOYEES)
                .set(EMPLOYEES.NAME, updatedEmployee.getName())
                .set(EMPLOYEES.ORGANIZATION, updatedEmployee.getChief())
                .set(EMPLOYEES.CHIEF, updatedEmployee.getChief());
    }
}
