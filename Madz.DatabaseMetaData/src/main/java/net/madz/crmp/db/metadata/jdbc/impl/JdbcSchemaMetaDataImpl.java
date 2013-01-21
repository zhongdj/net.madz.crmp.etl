package net.madz.crmp.db.metadata.jdbc.impl;

import java.util.Collection;
import java.util.Map;

import net.madz.crmp.db.metadata.DottedPath;
import net.madz.crmp.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcTableMetaData;

public class JdbcSchemaMetaDataImpl implements JdbcSchemaMetaData {

    private final DottedPath name;
    private final Map<String, JdbcTableMetaData> tables;

    public JdbcSchemaMetaDataImpl(DottedPath schemaPath, Map tables) {
        this.name = schemaPath;
        this.tables = tables;
    }

    @Override
    public DottedPath getSchemaPath() {
        return name;
    }

    @Override
    public Collection getTables() {
        return this.tables.values();
    }

    @Override
    public JdbcTableMetaData getTable(String name) {
        return this.tables.get(name);
    }

    @Override
    public String toString() {
        return "JdbcSchemaMetaDataImpl [name=" + name + ", tables=" + tables + "]";
    }
}
