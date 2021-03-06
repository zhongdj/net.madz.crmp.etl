package net.madz.db.core.meta.immutable.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;

public class SchemaMetaDataImpl<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements SchemaMetaData<SMD, TMD, CMD, FMD, IMD> {

    protected final String schemaName;
    protected final DottedPath schemaPath;
    protected TreeMap<String, TMD> tablesMap = new TreeMap<String, TMD>();

    public SchemaMetaDataImpl(SMD metaData) {
        this.schemaName = metaData.getSchemaName();
        this.schemaPath = metaData.getSchemaPath();
    }

    public SchemaMetaDataImpl(DottedPath schemaPath) {
        super();
        this.schemaName = schemaPath.getName();
        this.schemaPath = schemaPath;
    }

    @Override
    public String getSchemaName() {
        return this.schemaName;
    }

    @Override
    public DottedPath getSchemaPath() {
        return schemaPath;
    }

    @Override
    public Collection<TMD> getTables() {
        return Collections.unmodifiableCollection(this.tablesMap.values());
    }

    @Override
    public TMD getTable(String name) {
        return this.tablesMap.get(name);
    }

    public void appendTable(TMD table) {
        this.tablesMap.put(table.getTableName(), table);
    }

    @Override
    public String toString() {
        return "SchemaMetaDataImpl [name=" + schemaPath + ", tables=" + tablesMap.values() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( schemaPath == null ) ? 0 : schemaPath.hashCode() );
        result = prime * result + ( ( tablesMap == null ) ? 0 : tablesMap.hashCode() );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        SchemaMetaDataImpl other = (SchemaMetaDataImpl) obj;
        if ( schemaPath == null ) {
            if ( other.schemaPath != null ) return false;
        } else if ( !schemaPath.equals(other.schemaPath) ) return false;
        if ( tablesMap == null ) {
            if ( other.tablesMap != null ) return false;
        } else if ( !tablesMap.equals(other.tablesMap) ) return false;
        return true;
    }

}
