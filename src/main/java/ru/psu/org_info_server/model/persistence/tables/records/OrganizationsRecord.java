/*
 * This file is generated by jOOQ.
 */
package ru.psu.org_info_server.model.persistence.tables.records;


import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;

import ru.psu.org_info_server.model.persistence.tables.Organizations;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OrganizationsRecord extends UpdatableRecordImpl<OrganizationsRecord> implements Record3<UUID, String, UUID> {

    private static final long serialVersionUID = 1148805291;

    /**
     * Setter for <code>info.organizations.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>info.organizations.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>info.organizations.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>info.organizations.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>info.organizations.parent</code>.
     */
    public void setParent(UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>info.organizations.parent</code>.
     */
    public UUID getParent() {
        return (UUID) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<UUID, String, UUID> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<UUID, String, UUID> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return Organizations.ORGANIZATIONS.ID;
    }

    @Override
    public Field<String> field2() {
        return Organizations.ORGANIZATIONS.NAME;
    }

    @Override
    public Field<UUID> field3() {
        return Organizations.ORGANIZATIONS.PARENT;
    }

    @Override
    public UUID component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public UUID component3() {
        return getParent();
    }

    @Override
    public UUID value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public UUID value3() {
        return getParent();
    }

    @Override
    public OrganizationsRecord value1(UUID value) {
        setId(value);
        return this;
    }

    @Override
    public OrganizationsRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public OrganizationsRecord value3(UUID value) {
        setParent(value);
        return this;
    }

    @Override
    public OrganizationsRecord values(UUID value1, String value2, UUID value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached OrganizationsRecord
     */
    public OrganizationsRecord() {
        super(Organizations.ORGANIZATIONS);
    }

    /**
     * Create a detached, initialised OrganizationsRecord
     */
    public OrganizationsRecord(UUID id, String name, UUID parent) {
        super(Organizations.ORGANIZATIONS);

        set(0, id);
        set(1, name);
        set(2, parent);
    }
}
