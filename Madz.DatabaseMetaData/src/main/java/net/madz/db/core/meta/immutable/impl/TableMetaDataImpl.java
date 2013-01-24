package net.madz.db.core.meta.immutable.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.type.TableType;

public class TableMetaDataImpl<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements TableMetaData<SMD, TMD, CMD, FMD, IMD> {

    protected final DottedPath name; // catalog.schema.name
    protected final TableType type;
    protected final String remarks;
    protected final String idCol, idGeneration;
    protected final Map<String, CMD> columnMap;
    protected final List<CMD> orderedColumns;
    protected final Map<String, IMD> indexMap;
    protected final List<FMD> fkList = new LinkedList<FMD>();
    protected IMD primaryKey;

    public TableMetaDataImpl(final TMD tableMetaData) {
        this.name = tableMetaData.getTablePath();
        this.type = tableMetaData.getType();
        this.remarks = tableMetaData.getRemarks();
        this.idCol = tableMetaData.getIdCol();
        this.idGeneration = tableMetaData.getIdGeneration();
        TreeMap<String, CMD> columnMap = new TreeMap<String, CMD>(String.CASE_INSENSITIVE_ORDER);
        for ( CMD column : tableMetaData.getColumns() ) {
            columnMap.put(column.getColumnName(), column);
        }
        this.columnMap = Collections.unmodifiableMap(columnMap);
        this.orderedColumns = tableMetaData.getColumns();
        Collection<IMD> indexSet = tableMetaData.getIndexSet();
        TreeMap<String, IMD> indexMap = new TreeMap<String, IMD>();
        for ( IMD index : indexSet ) {
            indexMap.put(index.getIndexName(), index);
        }
        this.indexMap = Collections.unmodifiableMap(indexMap);
        // this.fkList = tableBuilder.getFkList();
        this.primaryKey = tableMetaData.getPrimaryKey();
    }

    @Override
    public DottedPath getTablePath() {
        return name;
    }

    @Override
    public String getTableName() {
        return name.getName();
    }

    @Override
    public IMD getPrimaryKey() {
        return this.primaryKey;
    }

    @Override
    public List<CMD> getColumns() {
        return this.orderedColumns;
    }

    @Override
    public CMD getColumn(String columnName) {
        return this.columnMap.get(columnName);
    }

    @Override
    public Collection<FMD> getForeignKeySet() {
        return this.fkList;
    }

    @Override
    public Collection<IMD> getIndexSet() {
        return this.indexMap.values();
    }

    @Override
    public IMD getIndex(String indexName) {
        return this.indexMap.get(indexName);
    }

    public String getCatalogName() {
        if ( name.size() > 2 ) {
            return this.name.getParent().getParent().getName();
        } else {
            return null;
        }
    }

    public String getSchemaName() {
        if ( name.size() > 1 ) {
            return this.name.getParent().getName();
        } else {
            return null;
        }
    }

    void addForeignKey(FMD fk) {
        this.fkList.add(fk);
    }

    @Override
    public TableType getType() {
        return this.type;
    }

    @Override
    public String getRemarks() {
        return this.remarks;
    }

    @Override
    public String getIdCol() {
        return this.idCol;
    }

    @Override
    public String getIdGeneration() {
        return this.idGeneration;
    }

    @Override
    public String toString() {
        return "TableMetaDataImpl [name=" + name + ", type=" + type + ", remarks=" + remarks + ", idCol=" + idCol + ", idGeneration=" + idGeneration
                + ", columnMap=" + columnMap + ", orderedColumns=" + orderedColumns + ", indexMap=" + indexMap + ", fkList=" + fkList + ", primaryKey="
                + primaryKey + "]";
    }
}