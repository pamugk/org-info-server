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
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import ru.psu.org_info_server.model.persistence.Info;
import ru.psu.org_info_server.model.persistence.Keys;
import ru.psu.org_info_server.model.persistence.tables.records.OrganizationsRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Organizations extends TableImpl<OrganizationsRecord> {

    private static final long serialVersionUID = 1473606874;

    /**
     * The reference instance of <code>info.organizations</code>
     */
    public static final Organizations ORGANIZATIONS = new Organizations();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OrganizationsRecord> getRecordType() {
        return OrganizationsRecord.class;
    }

    /**
     * The column <code>info.organizations.id</code>.
     */
    public final TableField<OrganizationsRecord, UUID> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.UUID.nullable(false).defaultValue(org.jooq.impl.DSL.field("uuid_generate_v4()", org.jooq.impl.SQLDataType.UUID)), this, "");

    /**
     * The column <code>info.organizations.name</code>.
     */
    public final TableField<OrganizationsRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR.nullable(false), this, "");

    /**
     * The column <code>info.organizations.parent</code>.
     */
    public final TableField<OrganizationsRecord, UUID> PARENT = createField(DSL.name("parent"), org.jooq.impl.SQLDataType.UUID, this, "");

    /**
     * Create a <code>info.organizations</code> table reference
     */
    public Organizations() {
        this(DSL.name("organizations"), null);
    }

    /**
     * Create an aliased <code>info.organizations</code> table reference
     */
    public Organizations(String alias) {
        this(DSL.name(alias), ORGANIZATIONS);
    }

    /**
     * Create an aliased <code>info.organizations</code> table reference
     */
    public Organizations(Name alias) {
        this(alias, ORGANIZATIONS);
    }

    private Organizations(Name alias, Table<OrganizationsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Organizations(Name alias, Table<OrganizationsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Organizations(Table<O> child, ForeignKey<O, OrganizationsRecord> key) {
        super(child, key, ORGANIZATIONS);
    }

    @Override
    public Schema getSchema() {
        return Info.INFO;
    }

    @Override
    public UniqueKey<OrganizationsRecord> getPrimaryKey() {
        return Keys.ORGANIZATIONS_PKEY;
    }

    @Override
    public List<UniqueKey<OrganizationsRecord>> getKeys() {
        return Arrays.<UniqueKey<OrganizationsRecord>>asList(Keys.ORGANIZATIONS_PKEY);
    }

    @Override
    public List<ForeignKey<OrganizationsRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<OrganizationsRecord, ?>>asList(Keys.ORGANIZATIONS__FK_PARENT_ORGANIZATION);
    }

    public Organizations organizations() {
        return new Organizations(this, Keys.ORGANIZATIONS__FK_PARENT_ORGANIZATION);
    }

    @Override
    public Organizations as(String alias) {
        return new Organizations(DSL.name(alias), this);
    }

    @Override
    public Organizations as(Name alias) {
        return new Organizations(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Organizations rename(String name) {
        return new Organizations(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Organizations rename(Name name) {
        return new Organizations(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<UUID, String, UUID> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
