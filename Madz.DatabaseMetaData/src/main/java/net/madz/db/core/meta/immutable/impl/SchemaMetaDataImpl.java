package net.madz.db.core.meta.immutable.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    protected final Collection<TMD> orderedTables = new ArrayList<TMD>();
    protected final Map<String, TMD> tablesMap = new HashMap<String, TMD>();

    public SchemaMetaDataImpl(SMD metaData) {
        this.name = metaData.getSchemaPath();
    }

    @Override
    public DottedPath getSchemaPath() {
        return name;
    }

    @Override
    public Collection<TMD> getTables() {
        return Collections.unmodifiableCollection(this.orderedTables);
    }

    @Override
    public TMD getTable(String name) {
        return this.tablesMap.get(name);
    }

    public void appendTable(TMD table) {
        this.tablesMap.put(table.getTableName(), table);
        this.orderedTables.add(table);
    }

    @Override
    public String toString() {
        return "SchemaMetaDataImpl [name=" + name + ", tables=" + orderedTables + "]";
    }
}
