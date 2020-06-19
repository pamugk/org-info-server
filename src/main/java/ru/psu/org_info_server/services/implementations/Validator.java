package ru.psu.org_info_server.services.implementations;

import org.jooq.DSLContext;

import java.util.UUID;

import static ru.psu.org_info_server.model.persistence.tables.Employees.EMPLOYEES;
import static ru.psu.org_info_server.model.persistence.tables.Organizations.ORGANIZATIONS;

class Validator {
    static boolean employeeHasSubordinatesButOrgChanged(DSLContext context, UUID empId, UUID orgId) {
        return context.fetchExists(EMPLOYEES.where(EMPLOYEES.CHIEF.eq(empId).and(EMPLOYEES.ORGANIZATION.notEqual(orgId))));
    }

    static boolean employeeNotFound(DSLContext context, UUID id) {
        return !context.fetchExists(EMPLOYEES.where(EMPLOYEES.ID.eq(id)));
    }

    static boolean employeeNotInOrganization(DSLContext context, UUID employeeId, UUID orgId) {
        return !context.fetchExists(EMPLOYEES.where(EMPLOYEES.ID.eq(employeeId).and(EMPLOYEES.ORGANIZATION.eq(orgId))));
    }

    static boolean employeeHasChildren(DSLContext context, UUID id) {
        return context.fetchExists(EMPLOYEES.where(EMPLOYEES.CHIEF.eq(id)));
    }

    static boolean organizationHasChildren(DSLContext context, UUID id) {
        return context.fetchExists(ORGANIZATIONS.where(ORGANIZATIONS.PARENT.eq(id))) ||
                context.fetchExists(EMPLOYEES.where(EMPLOYEES.ORGANIZATION.eq(id)));
    }

    static boolean organizationNotFound(DSLContext context, UUID id) {
        return !context.fetchExists(ORGANIZATIONS.where(ORGANIZATIONS.ID.eq(id)));
    }
}
