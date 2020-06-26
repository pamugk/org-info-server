/*
 * This file is generated by jOOQ.
 */
package ru.psu.org_info_server.model.persistence.tables;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import ru.psu.org_info_server.model.persistence.Info;
import ru.psu.org_info_server.model.persistence.Keys;
import ru.psu.org_info_server.model.persistence.tables.records.EmployeesRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Employees extends TableImpl<EmployeesRecord> {

    private static final long serialVersionUID = -1207501359;

    /**
     * The reference instance of <code>info.employees</code>
     */
    public static final Employees EMPLOYEES = new Employees();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EmployeesRecord> getRecordType() {
        return EmployeesRecord.class;
    }

    /**
     * The column <code>info.employees.id</code>.
     */
    public final TableField<EmployeesRecord, UUID> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.UUID.nullable(false).defaultValue(org.jooq.impl.DSL.field("uuid_generate_v4()", org.jooq.impl.SQLDataType.UUID)), this, "");

    /**
     * The column <code>info.employees.name</code>.
     */
    public final TableField<EmployeesRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>info.employees.organization</code>.
     */
    public final TableField<EmployeesRecord, UUID> ORGANIZATION = createField(DSL.name("organization"), org.jooq.impl.SQLDataType.UUID, this, "");

    /**
     * The column <code>info.employees.chief</code>.
     */
    public final TableField<EmployeesRecord, UUID> CHIEF = createField(DSL.name("chief"), org.jooq.impl.SQLDataType.UUID, this, "");

    /**
     * Create a <code>info.employees</code> table reference
     */
    public Employees() {
        this(DSL.name("employees"), null);
    }

    /**
     * Create an aliased <code>info.employees</code> table reference
     */
    public Employees(String alias) {
        this(DSL.name(alias), EMPLOYEES);
    }

    /**
     * Create an aliased <code>info.employees</code> table reference
     */
    public Employees(Name alias) {
        this(alias, EMPLOYEES);
    }

    private Employees(Name alias, Table<EmployeesRecord> aliased) {
        this(alias, aliased, null);
    }

    private Employees(Name alias, Table<EmployeesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Employees(Table<O> child, ForeignKey<O, EmployeesRecord> key) {
        super(child, key, EMPLOYEES);
    }

    @Override
    public Schema getSchema() {
        return Info.INFO;
    }

    @Override
    public UniqueKey<EmployeesRecord> getPrimaryKey() {
        return Keys.EMPLOYEES_PKEY;
    }

    @Override
    public List<UniqueKey<EmployeesRecord>> getKeys() {
        return Arrays.<UniqueKey<EmployeesRecord>>asList(Keys.EMPLOYEES_PKEY);
    }

    @Override
    public List<ForeignKey<EmployeesRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<EmployeesRecord, ?>>asList(Keys.EMPLOYEES__FK_ORGANIZATION_EMPLOYEE, Keys.EMPLOYEES__FK_CHIEF_EMPLOYEE);
    }

    public Organizations organizations() {
        return new Organizations(this, Keys.EMPLOYEES__FK_ORGANIZATION_EMPLOYEE);
    }

    public Employees employees() {
        return new Employees(this, Keys.EMPLOYEES__FK_CHIEF_EMPLOYEE);
    }

    @Override
    public Employees as(String alias) {
        return new Employees(DSL.name(alias), this);
    }

    @Override
    public Employees as(Name alias) {
        return new Employees(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Employees rename(String name) {
        return new Employees(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Employees rename(Name name) {
        return new Employees(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<UUID, String, UUID, UUID> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
