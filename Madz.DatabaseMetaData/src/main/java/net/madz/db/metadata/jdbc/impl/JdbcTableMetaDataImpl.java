package net.madz.db.metadata.jdbc.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.JdbcColumnMetaData;
import net.madz.db.metadata.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.metadata.jdbc.JdbcIndexMetaData;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.db.metadata.jdbc.type.JdbcTableType;

public class JdbcTableMetaDataImpl implements JdbcTableMetaData {

    private final DottedPath name; // catalog.schema.name
    private final JdbcTableType type;
    private final String remarks;
    private final String idCol, idGeneration;
    private final Map<String, JdbcColumnMetaData> columnMap;
    private final List<JdbcColumnMetaData> orderedColumns;
    private final Map<String, JdbcIndexMetaData> indexMap;
    private final List<JdbcForeignKeyMetaData> fkList = new LinkedList<JdbcForeignKeyMetaData>();
    private JdbcIndexMetaData primaryKey;

    public JdbcTableMetaDataImpl(final JdbcTableMetaData tableMetaData) {
        this.name = tableMetaData.getTablePath();
        this.type = tableMetaData.getType();
        this.remarks = tableMetaData.getRemarks();
        this.idCol = tableMetaData.getIdCol();
        this.idGeneration = tableMetaData.getIdGeneration();
        TreeMap<String, JdbcColumnMetaData> columnMap = new TreeMap<String, JdbcColumnMetaData>(String.CASE_INSENSITIVE_ORDER);
        for ( JdbcColumnMetaData column : tableMetaData.getColumns() ) {
            columnMap.put(column.getColumnName(), column);
        }
        this.columnMap = Collections.unmodifiableMap(columnMap);
        this.orderedColumns = tableMetaData.getColumns();
        Collection<JdbcIndexMetaData> indexSet = tableMetaData.getIndexSet();
        TreeMap<String, JdbcIndexMetaData> indexMap = new TreeMap<String, JdbcIndexMetaData>();
        for ( JdbcIndexMetaData index : indexSet ) {
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
    public JdbcIndexMetaData getPrimaryKey() {
        return this.primaryKey;
    }

    @Override
    public List getColumns() {
        return this.orderedColumns;
    }

    @Override
    public JdbcColumnMetaData getColumn(String columnName) {
        return this.columnMap.get(columnName);
    }

    @Override
    public Collection getForeignKeySet() {
        return this.fkList;
    }

    @Override
    public Collection getIndexSet() {
        return this.indexMap.values();
    }

    @Override
    public JdbcIndexMetaData getIndex(String indexName) {
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

    void addForeignKey(JdbcForeignKeyMetaDataImpl fk) {
        this.fkList.add(fk);
    }

    @Override
    public JdbcTableType getType() {
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
        return "JdbcTableMetaDataImpl [name=" + name + ", type=" + type + ", remarks=" + remarks + ", idCol=" + idCol + ", idGeneration=" + idGeneration
                + ", columnMap=" + columnMap + ", orderedColumns=" + orderedColumns + ", indexMap=" + indexMap + ", fkList=" + fkList + ", primaryKey="
                + primaryKey + "]";
    }
}
