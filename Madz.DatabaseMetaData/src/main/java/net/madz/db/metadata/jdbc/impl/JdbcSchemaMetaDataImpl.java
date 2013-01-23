package net.madz.db.metadata.jdbc.impl;

import java.util.Collection;
import java.util.Map;

import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;

public class JdbcSchemaMetaDataImpl implements JdbcSchemaMetaData {

    protected final DottedPath name;
    protected final Map<String, ? extends JdbcTableMetaData> tables;

    public JdbcSchemaMetaDataImpl(DottedPath schemaPath, Map tables) {
        this.name = schemaPath;
        this.tables = tables;
    }

    @Override
    public DottedPath getSchemaPath() {
        return name;
    }

    @Override
    public Collection<? extends JdbcTableMetaData> getTables() {
        return this.tables.values();
    }

    @Override
    public <DB_DEPENDENT_TABLE_META extends JdbcTableMetaData> DB_DEPENDENT_TABLE_META getTable(String name) {
        return (DB_DEPENDENT_TABLE_META) this.tables.get(name);
    }

    @Override
    public String toString() {
        return "JdbcSchemaMetaDataImpl [name=" + name + ", tables=" + tables + "]";
    }
}
