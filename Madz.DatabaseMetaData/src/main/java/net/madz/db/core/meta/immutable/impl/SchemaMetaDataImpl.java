package net.madz.db.core.meta.immutable.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
    protected final Collection<TMD> tables = new ArrayList<TMD>();
    protected final Map<String, TMD> tablesIndex = new HashMap<String, TMD>();

    public SchemaMetaDataImpl(SMD metaData) {
        this.name = metaData.getSchemaPath();
        this.tables.addAll(metaData.getTables());
        for ( TMD t : metaData.getTables() ) {
            tablesIndex.put(t.getTableName(), t);
        }
    }

    @Override
    public DottedPath getSchemaPath() {
        return name;
    }

    @Override
    public Collection<TMD> getTables() {
        return this.tables;
    }

    @Override
    public TMD getTable(String name) {
        return this.tablesIndex.get(name);
    }

    @Override
    public String toString() {
        return "SchemaMetaDataImpl [name=" + name + ", tables=" + tables + "]";
    }
}
