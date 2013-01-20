package net.madz.crmp.db.metadata.jdbc.impl;

import java.util.Collection;
import java.util.Map;

import net.madz.crmp.db.metadata.DottedPath;
import net.madz.crmp.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcTableMetaData;

public class JdbcSchemaMetaDataImpl<M extends JdbcTableMetaData<?, ?, ?>> implements JdbcSchemaMetaData<M> {

    private final DottedPath name;
    private final Map<String, M> tables;

    public JdbcSchemaMetaDataImpl(DottedPath schemaPath, Map<String, M> tables) {
        this.name = schemaPath;
        this.tables = tables;
    }

    @Override
    public DottedPath getSchemaPath() {
        return name;
    }

    @Override
    public Collection<M> getTables() {
        return this.tables.values();
    }

    @Override
    public M getTable(String name) {
        return this.tables.get(name);
    }

    @Override
    public String toString() {
        return "JdbcSchemaMetaDataImpl [name=" + name + ", tables=" + tables + "]";
    }
}
