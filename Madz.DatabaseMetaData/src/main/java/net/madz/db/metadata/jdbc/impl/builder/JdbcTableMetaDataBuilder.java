package net.madz.db.metadata.jdbc.impl.builder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.JdbcColumnMetaData;
import net.madz.db.metadata.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.db.metadata.jdbc.impl.JdbcTableMetaDataImpl;
import net.madz.db.metadata.jdbc.impl.enums.JdbcColumnDbMetaDataEnum;
import net.madz.db.metadata.jdbc.impl.enums.JdbcIndexDbMetaDataEnum;
import net.madz.db.metadata.jdbc.impl.enums.JdbcPrimaryKeyDbMetaDataEnum;
import net.madz.db.metadata.jdbc.impl.enums.JdbcTableDbMetaDataEnum;
import net.madz.db.metadata.jdbc.type.JdbcTableType;

public class JdbcTableMetaDataBuilder implements JdbcTableMetaData {

    // TODO [Jan 22, 2013][barry] Use modifier carefully, all of them are open
    // for subclasses?
    protected final DatabaseMetaData dbMetaData;
    protected final JdbcSchemaMetaData schema;
    protected final JdbcMetaDataResultSet<JdbcTableDbMetaDataEnum> rs;
    protected DottedPath tablePath; // catalog.schema.name
    protected JdbcTableType type;
    protected String remarks;
    protected String idCol, idGeneration;
    protected Map<String, JdbcColumnMetaDataBuilder> columnMap;
    protected List<JdbcColumnMetaDataBuilder> orderedColumns;
    protected Map<String, JdbcIndexMetaDataBuilder> indexMap;
    protected List<JdbcForeignKeyMetaData> fkList = new LinkedList<JdbcForeignKeyMetaData>();
    protected JdbcIndexMetaDataBuilder primaryKey;

    public JdbcTableMetaDataBuilder(DatabaseMetaData dbMetaData, JdbcSchemaMetaData schema, JdbcMetaDataResultSet<JdbcTableDbMetaDataEnum> rs)
            throws SQLException {
        this.dbMetaData = dbMetaData;
        this.schema = schema;
        this.rs = rs;
    }

    public void build(Connection connection) throws SQLException {
        // TODO [Jan 22, 2013][barry][Done] Use modifier final with immutable
        // variables
        this.tablePath = schema.getSchemaPath().append(rs.get(JdbcTableDbMetaDataEnum.table_name));
        this.type = JdbcTableType.getTableType(rs.get(JdbcTableDbMetaDataEnum.table_type));
        this.remarks = rs.get(JdbcTableDbMetaDataEnum.remarks);
        this.idCol = rs.get(JdbcTableDbMetaDataEnum.self_referencing_col_name);
        this.idGeneration = rs.get(JdbcTableDbMetaDataEnum.ref_generation);
        final TreeSet<JdbcColumnMetaDataBuilder> orderedColumns = new TreeSet<JdbcColumnMetaDataBuilder>(JdbcColumnMetaData.ORDINAL_COMPARATOR);
        final TreeMap<String, JdbcColumnMetaDataBuilder> columns = new TreeMap<String, JdbcColumnMetaDataBuilder>(String.CASE_INSENSITIVE_ORDER);
        this.columnMap = Collections.unmodifiableMap(columns);
        ResultSet jdbcRs = dbMetaData.getColumns(getCatalogName(), getSchemaName(), getTableName(), "%");
        final JdbcMetaDataResultSet<JdbcColumnDbMetaDataEnum> colRs = new JdbcMetaDataResultSet<JdbcColumnDbMetaDataEnum>(jdbcRs,
                JdbcColumnDbMetaDataEnum.values());
        try {
            while ( colRs.next() ) {
                final JdbcColumnMetaDataBuilder columnBuilder = newJdbcColumnMetaDataBuilder(this, colRs);
                columnBuilder.build(connection);
                orderedColumns.add(columnBuilder);
                columns.put(columnBuilder.getColumnName(), columnBuilder);
            }
        } finally {
            colRs.close();
        }
        this.orderedColumns = Collections.unmodifiableList(new ArrayList<JdbcColumnMetaDataBuilder>(orderedColumns));
        final TreeMap<String, JdbcIndexMetaDataBuilder> indexMap = new TreeMap<String, JdbcIndexMetaDataBuilder>(String.CASE_INSENSITIVE_ORDER);
        this.indexMap = Collections.unmodifiableMap(indexMap);
        jdbcRs = dbMetaData.getIndexInfo(getCatalogName(), getSchemaName(), getTableName(), false, true);
        final JdbcMetaDataResultSet<JdbcIndexDbMetaDataEnum> ixRs = new JdbcMetaDataResultSet<JdbcIndexDbMetaDataEnum>(jdbcRs, JdbcIndexDbMetaDataEnum.values());
        try {
            while ( ixRs.next() ) {
                final String name = JdbcIndexMetaDataBuilder.getName(ixRs);
                JdbcIndexMetaDataBuilder ix = indexMap.get(name);
                if ( null == ix ) {
                    ix = newJdbcIndexMetaDataBuilder(this, ixRs);
                    ix.build(connection);
                    indexMap.put(name, ix);
                }
                ix.addEntry(ixRs);
            }
        } finally {
            ixRs.close();
        }
        // PK
        //
        jdbcRs = dbMetaData.getPrimaryKeys(getCatalogName(), getSchemaName(), getTableName());
        final JdbcMetaDataResultSet<JdbcPrimaryKeyDbMetaDataEnum> pkRs = new JdbcMetaDataResultSet<JdbcPrimaryKeyDbMetaDataEnum>(jdbcRs,
                JdbcPrimaryKeyDbMetaDataEnum.values());
        try {
            while ( pkRs.next() && null == primaryKey ) {
                final String pkName = pkRs.get(JdbcPrimaryKeyDbMetaDataEnum.PK_NAME);
                primaryKey = indexMap.get(pkName);
                if ( null != primaryKey ) {
                    primaryKey.setPrimaryKey();
                }
            }
        } finally {
            pkRs.close();
        }
    }

    public JdbcTableMetaData getCopy() throws SQLException {
        System.out.println("Jdbc Table metadata builder");
        for ( JdbcColumnMetaDataBuilder columnBuilder : this.columnMap.values() ) {
            columnBuilder.getCopy();
        }
        for ( JdbcIndexMetaDataBuilder indexBuilder : indexMap.values() ) {
            indexBuilder.getCopy();
        }
        return new JdbcTableMetaDataImpl(this);
    }

    protected JdbcIndexMetaDataBuilder newJdbcIndexMetaDataBuilder(JdbcTableMetaDataBuilder jdbcTableMetaDataBuilder,
            JdbcMetaDataResultSet<JdbcIndexDbMetaDataEnum> ixRs) throws SQLException {
        return new JdbcIndexMetaDataBuilder(jdbcTableMetaDataBuilder, ixRs);
    }

    protected JdbcColumnMetaDataBuilder newJdbcColumnMetaDataBuilder(JdbcTableMetaDataBuilder jdbcTableMetaDataBuilder,
            JdbcMetaDataResultSet<JdbcColumnDbMetaDataEnum> colRs) throws SQLException {
        return new JdbcColumnMetaDataBuilder(this, colRs);
    }

    @Override
    public DottedPath getTablePath() {
        return this.tablePath;
    }

    @Override
    public String getTableName() {
        return this.tablePath.getName();
    }

    @Override
    public JdbcIndexMetaDataBuilder getPrimaryKey() {
        return this.primaryKey;
    }

    @Override
    public List getColumns() {
        return this.orderedColumns;
    }

    @Override
    public JdbcColumnMetaDataBuilder getColumn(String columnName) {
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
    public JdbcIndexMetaDataBuilder getIndex(String indexName) {
        return this.indexMap.get(indexName);
    }

    public String getCatalogName() {
        if ( tablePath.size() > 2 ) {
            return this.tablePath.getParent().getParent().getName();
        } else {
            return null;
        }
    }

    public String getSchemaName() {
        if ( tablePath.size() > 1 ) {
            return this.tablePath.getParent().getName();
        } else {
            return null;
        }
    }

    public DatabaseMetaData getDbMetaData() {
        return dbMetaData;
    }

    public JdbcSchemaMetaData getSchema() {
        return schema;
    }

    public JdbcMetaDataResultSet<JdbcTableDbMetaDataEnum> getRs() {
        return rs;
    }

    public DottedPath getName() {
        return tablePath;
    }

    public JdbcTableType getType() {
        return type;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getIdCol() {
        return idCol;
    }

    public String getIdGeneration() {
        return idGeneration;
    }

    public List<JdbcForeignKeyMetaData> getFkList() {
        return fkList;
    }

    public void addForeignKey(JdbcForeignKeyMetaData metaData) {
        this.fkList.add(metaData);
    }

    @Override
    public String toString() {
        return "JdbcTableMetaDataBuilder [tablePath=" + tablePath + ", type=" + type + ", remarks=" + remarks + ", idCol=" + idCol + ", idGeneration="
                + idGeneration + ", orderedColumns=" + orderedColumns + ", indexMap=" + indexMap + ", primaryKey=" + primaryKey + "]";
    }
}
