package net.madz.crmp.db.metadata.jdbc.impl.builder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import net.madz.crmp.db.metadata.DottedPath;
import net.madz.crmp.db.metadata.jdbc.JdbcColumnMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcForeignKeyMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcIndexMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.crmp.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.crmp.db.metadata.jdbc.impl.JdbcTableMetaDataImpl;
import net.madz.crmp.db.metadata.jdbc.impl.enums.JdbcColumnDbMetaDataEnum;
import net.madz.crmp.db.metadata.jdbc.impl.enums.JdbcIndexDbMetaDataEnum;
import net.madz.crmp.db.metadata.jdbc.impl.enums.JdbcPrimaryKeyDbMetaDataEnum;
import net.madz.crmp.db.metadata.jdbc.impl.enums.JdbcTableDbMetaDataEnum;
import net.madz.crmp.db.metadata.jdbc.type.JdbcTableType;

public class JdbcTableMetaDataBuilder<M extends JdbcTableMetaData<?, ?, ?>> implements JdbcTableMetaData {

    private final Connection conn;
    private final DatabaseMetaData dbMetaData;
    private final JdbcSchemaMetaData<M> schema;
    private final JdbcMetaDataResultSet<JdbcTableDbMetaDataEnum> rs;
    private DottedPath tablePath; // catalog.schema.name
    private JdbcTableType type;
    private String remarks;
    private String idCol, idGeneration;
    private Map<String, JdbcColumnMetaDataBuilder> columnMap;
    private List<JdbcColumnMetaDataBuilder> orderedColumns;
    private Map<String, JdbcIndexMetaDataBuilder> indexMap;
    private List<JdbcForeignKeyMetaData> fkList = new LinkedList<JdbcForeignKeyMetaData>();
    private JdbcIndexMetaDataBuilder primaryKey;

    public JdbcTableMetaDataBuilder(Connection conn, DatabaseMetaData dbMetaData, JdbcSchemaMetaData<M> schema,
            JdbcMetaDataResultSet<JdbcTableDbMetaDataEnum> rs) throws SQLException {
        this.conn = conn;
        this.dbMetaData = dbMetaData;
        this.schema = schema;
        this.rs = rs;
        this.tablePath = schema.getSchemaPath().append(rs.get(JdbcTableDbMetaDataEnum.table_name));
        this.type = JdbcTableType.getTableType(rs.get(JdbcTableDbMetaDataEnum.table_type));
        this.remarks = rs.get(JdbcTableDbMetaDataEnum.remarks);
        this.idCol = rs.get(JdbcTableDbMetaDataEnum.self_referencing_col_name);
        this.idGeneration = rs.get(JdbcTableDbMetaDataEnum.ref_generation);
        TreeSet<JdbcColumnMetaDataBuilder> orderedColumns = new TreeSet<JdbcColumnMetaDataBuilder>(JdbcColumnMetaData.ORDINAL_COMPARATOR);
        TreeMap<String, JdbcColumnMetaDataBuilder> columns = new TreeMap<String, JdbcColumnMetaDataBuilder>(String.CASE_INSENSITIVE_ORDER);
        this.columnMap = Collections.unmodifiableMap(columns);
        ResultSet jdbcRs = dbMetaData.getColumns(getCatalogName(), getSchemaName(), getTableName(), "%");
        JdbcMetaDataResultSet<JdbcColumnDbMetaDataEnum> colRs = new JdbcMetaDataResultSet<JdbcColumnDbMetaDataEnum>(jdbcRs, JdbcColumnDbMetaDataEnum.values());
        try {
            while ( colRs.next() ) {
                JdbcColumnMetaDataBuilder columnBuilder = newJdbcColumnMetaDataBuilder(this, colRs);
                // JdbcColumnMetaData column = columnBuilder.build();
                orderedColumns.add(columnBuilder);
                columns.put(columnBuilder.getColumnName(), columnBuilder);
            }
        } finally {
            colRs.close();
        }
        // this.orderedColumns = Collections.unmodifiableList(new
        // ArrayList<JdbcColumnMetaDataImpl>(orderedColumns));
        TreeMap<String, JdbcIndexMetaDataBuilder> indexMap = new TreeMap<String, JdbcIndexMetaDataBuilder>(String.CASE_INSENSITIVE_ORDER);
        this.indexMap = Collections.unmodifiableMap(indexMap);
        jdbcRs = dbMetaData.getIndexInfo(getCatalogName(), getSchemaName(), getTableName(), false, true);
        JdbcMetaDataResultSet<JdbcIndexDbMetaDataEnum> ixRs = new JdbcMetaDataResultSet<JdbcIndexDbMetaDataEnum>(jdbcRs, JdbcIndexDbMetaDataEnum.values());
        try {
            while ( ixRs.next() ) {
                String name = JdbcIndexMetaDataBuilder.getName(ixRs);
                JdbcIndexMetaDataBuilder ix = indexMap.get(name);
                if ( null == ix ) {
                    ix = newJdbcIndexMetaDataBuilder(this, ixRs);
                    // ix = (JdbcIndexMetaDataBuilder) indexBuilder.build();
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
        JdbcMetaDataResultSet<JdbcPrimaryKeyDbMetaDataEnum> pkRs = new JdbcMetaDataResultSet<JdbcPrimaryKeyDbMetaDataEnum>(jdbcRs,
                JdbcPrimaryKeyDbMetaDataEnum.values());
        try {
            while ( pkRs.next() && null == primaryKey ) {
                String pkName = pkRs.get(JdbcPrimaryKeyDbMetaDataEnum.PK_NAME);
                primaryKey = indexMap.get(pkName);
                if ( null != primaryKey ) {
                    primaryKey.setPrimaryKey();
                }
            }
        } finally {
            pkRs.close();
        }
    }

    public M build() throws SQLException {
        System.out.println("Jdbc Table metadata builder");
        for ( JdbcColumnMetaDataBuilder columnBuilder : this.columnMap.values() ) {
            columnBuilder.build();
        }
        for ( JdbcIndexMetaDataBuilder indexBuilder : indexMap.values() ) {
            indexBuilder.build();
        }
        return (M) new JdbcTableMetaDataImpl(this);
    }

    protected JdbcIndexMetaDataBuilder newJdbcIndexMetaDataBuilder(JdbcTableMetaDataBuilder<M> jdbcTableMetaDataBuilder,
            JdbcMetaDataResultSet<JdbcIndexDbMetaDataEnum> ixRs) throws SQLException {
        return new JdbcIndexMetaDataBuilder((JdbcTableMetaDataBuilder<JdbcTableMetaData<?, ?, ?>>) jdbcTableMetaDataBuilder, ixRs);
    }

    protected JdbcColumnMetaDataBuilder newJdbcColumnMetaDataBuilder(JdbcTableMetaDataBuilder<M> jdbcTableMetaDataBuilder,
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
    public List<? extends JdbcColumnMetaData> getColumns() {
        return this.orderedColumns;
    }

    @Override
    public JdbcColumnMetaDataBuilder getColumn(String columnName) {
        return this.columnMap.get(columnName);
    }

    @Override
    public Collection<? extends JdbcForeignKeyMetaData> getForeignKeySet() {
        return this.fkList;
    }

    @Override
    public Collection<? extends JdbcIndexMetaData> getIndexSet() {
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

    public Connection getConn() {
        return conn;
    }

    public DatabaseMetaData getDbMetaData() {
        return dbMetaData;
    }

    public JdbcSchemaMetaData<M> getSchema() {
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

    public Map<String, JdbcColumnMetaDataBuilder> getColumnMap() {
        return Collections.unmodifiableMap(columnMap);
    }

    public List<JdbcColumnMetaDataBuilder> getOrderedColumns() {
        return orderedColumns;
    }

    public Map<String, JdbcIndexMetaDataBuilder> getIndexMap() {
        return Collections.unmodifiableMap(indexMap);
    }

    public List<JdbcForeignKeyMetaData> getFkList() {
        return fkList;
    }

    public void addForeignKey(JdbcForeignKeyMetaData metaData) {
        this.fkList.add(metaData);
    }
}
