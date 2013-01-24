package net.madz.db.core.meta.immutable.impl;

import java.util.Collection;
import java.util.Map;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;

public class SchemaMetaDataImpl<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements SchemaMetaData<SMD, TMD, CMD, FMD, IMD> {

    protected final DottedPath name;
    protected final Map<String, TMD> tables;

    public SchemaMetaDataImpl(DottedPath schemaPath, Map<String, TMD> tables) {
        this.name = schemaPath;
        this.tables = tables;
    }

    @Override
    public DottedPath getSchemaPath() {
        return name;
    }

    @Override
    public Collection<TMD> getTables() {
        return this.tables.values();
    }

    @Override
    public TMD getTable(String name) {
        return this.tables.get(name);
    }

    @Override
    public String toString() {
        return "SchemaMetaDataImpl [name=" + name + ", tables=" + tables + "]";
    }
}
