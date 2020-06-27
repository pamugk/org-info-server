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
            throw new UnacceptableParamsException("Организация не найдена");
        if (newEmployee.getChief() != null) {
            if (Validator.employeeNotFound(context, newEmployee.getChief()))
                throw new NotFoundException("Руководитель не найден");
            if (Validator.employeeNotInOrganization(context, newEmployee.getChief(), newEmployee.getOrganization()))
                throw new NotFoundException("Руководитель из другой организации, что недопустимо");
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
            throw new NotFoundException("Удаляемый сотрудник не найден");
        if (Validator.employeeHasChildren(context, id))
            throw new HasChildrenException("У сотрудника ещё есть подчинённые");
        context.deleteFrom(EMPLOYEES).where(EMPLOYEES.ID.eq(id)).execute();
    }

    @Override
    public EmployeeDto getEmployeeInfo(UUID id) {
        EmployeeDto result = context
                .select()
                .from(EMPLOYEES)
                .where(EMPLOYEES.ID.eq(id))
                .fetchOneInto(EmployeeDto.class);
        if (result == null)
            throw new NotFoundException("Запрашиваемый сотрудник не найден");
        return result;
    }

    @Override
    @Transactional
    public ListChunk<EmployeeInfoDto> getEmployeeList(Number limit, Number offset, String search, String organization, UUID orgId) {
        Condition selectCondition = EMPLOYEES.NAME.contains(search);
        if (orgId != null)
            selectCondition = selectCondition.and(EMPLOYEES.ORGANIZATION.eq(orgId));
        int count = context.selectCount().from(EMPLOYEES).where(selectCondition).fetchOne(0, int.class);
        CommonTableExpression<Record> subordinates = name("subordinates").as(
                select().from(EMPLOYEES)
                .where(selectCondition)
                .limit(limit).offset(offset)
        );
            Field<UUID> subId = subordinates.field(EMPLOYEES.ID);
            Field<String> subName = subordinates.field(EMPLOYEES.NAME);
            Field<UUID> subOrgId = subordinates.field(EMPLOYEES.ORGANIZATION);
            Field<UUID> subChief = subordinates.field(EMPLOYEES.CHIEF);
        Table<EmployeesRecord> chiefs = EMPLOYEES.as("chiefs");
            Field<UUID> chiefId = chiefs.field(EMPLOYEES.ID);
            Field<String> chiefName = chiefs.field(EMPLOYEES.NAME);
        Condition orgCondition = "".equals(organization) ? condition(true) : ORGANIZATIONS.NAME.contains(organization);
        return ListChunk.<EmployeeInfoDto>builder().dataChunk(
                context
                        .with(subordinates)
                        .select(subId, subName, subChief, chiefName.as("chiefName"),
                                subOrgId, ORGANIZATIONS.NAME.as("organizationName"))
                        .from(subordinates)
                            .leftJoin(ORGANIZATIONS).on(subOrgId.eq(ORGANIZATIONS.ID))
                            .leftJoin(chiefs).on(subChief.eq(chiefId))
                        .where(orgCondition)
                        .orderBy(ORGANIZATIONS.NAME, subOrgId, subName)
                        .fetchInto(EmployeeInfoDto.class)
        ).totalCount(count).build();
    }

    @Override
    @Transactional
    public Tree<EmployeeDto> getEmployeeTree(UUID rootId, Number limit, Number offset) {
        if (rootId != null && Validator.employeeNotFound(context, rootId))
            throw new NotFoundException("Руководитель не найден");
        Employees chiefs = EMPLOYEES.as("chiefs");
        Condition chiefCondition = rootId == null ? chiefs.CHIEF.isNull() : chiefs.CHIEF.eq(rootId);
        int count = context.selectCount().from(chiefs).where(chiefCondition).fetchOne(0, int.class);
        return Tree.<EmployeeDto>builder().nodes(context
                .select(chiefs.ID, chiefs.NAME, chiefs.ORGANIZATION,
                        field(exists(selectFrom(EMPLOYEES).where(chiefs.ID.eq(EMPLOYEES.CHIEF)))))
                .from(chiefs)
                .where(chiefCondition)
                .orderBy(chiefs.NAME)
                .limit(limit).offset(offset)
                .fetchStream().map(
                        record -> TreeNode.<EmployeeDto>builder().value(
                                EmployeeDto.builder().id(record.value1())
                                        .name(record.value2()).organization(record.value3()).chief(rootId).build()
                        ).hasChildren(record.value4()).build()
                ).collect(Collectors.toList())).totalCount(count).build();
    }

    @Override
    public void updateEmployee(EmployeeDto updatedEmployee) {
        if (Validator.employeeNotFound(context, updatedEmployee.getId()))
            throw new NotFoundException("Обновляемый сотрудник не найден");
        if (Validator.organizationNotFound(context, updatedEmployee.getOrganization()))
            throw new UnacceptableParamsException("Организация не найдена");
        if (Validator.employeeHasSubordinatesButOrgChanged(context, updatedEmployee.getId(), updatedEmployee.getOrganization()))
            throw new UnacceptableParamsException("Сотрудник не может менять организацию, пока у него есть подчинённые");
        if (updatedEmployee.getChief() != null) {
            if (updatedEmployee.getId().equals(updatedEmployee.getChief()))
                throw new UnacceptableParamsException("Сотрудник не может быть руководителем самому себе, это дурдом");
            if (Validator.employeeNotFound(context, updatedEmployee.getChief()))
                throw new UnacceptableParamsException("Руководитель не найден");
            if (Validator.employeeNotInOrganization(context, updatedEmployee.getChief(), updatedEmployee.getOrganization()))
                throw new UnacceptableParamsException("Руководитель из другой организации, что недопустимо");
        }
        context.update(EMPLOYEES)
                .set(EMPLOYEES.NAME, updatedEmployee.getName())
                .set(EMPLOYEES.ORGANIZATION, updatedEmployee.getChief())
                .set(EMPLOYEES.CHIEF, updatedEmployee.getChief())
                .where(EMPLOYEES.ID.eq(updatedEmployee.getId()))
                .execute();
    }
}
