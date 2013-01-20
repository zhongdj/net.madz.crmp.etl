package net.madz.crmp.db.metadata.jdbc.impl.builder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.madz.crmp.db.metadata.DottedPath;
import net.madz.crmp.db.metadata.jdbc.JdbcForeignKeyMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.crmp.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.crmp.db.metadata.jdbc.impl.JdbcSchemaMetaDataImpl;
import net.madz.crmp.db.metadata.jdbc.impl.enums.JdbcImportKeyDbMetaDataEnum;
import net.madz.crmp.db.metadata.jdbc.impl.enums.JdbcTableDbMetaDataEnum;
import net.madz.crmp.db.metadata.jdbc.type.JdbcTableType;

public class JdbcSchemaMetaDataBuilder<M extends JdbcTableMetaData<?, ?, ?>, B extends JdbcTableMetaDataBuilder<M>> implements JdbcSchemaMetaData<B> {

    protected final Connection connection;
    private DottedPath schemaPath;
    // private Map<String, M> tableMetaDataList = new TreeMap<String,
    // M>(String.CASE_INSENSITIVE_ORDER);
    private Map<String, B> tableBuilderList = new TreeMap<String, B>(String.CASE_INSENSITIVE_ORDER);

    public JdbcSchemaMetaDataBuilder(final Connection connection, final DottedPath schemaPath) throws SQLException {
        super();
        this.connection = connection;
        this.schemaPath = schemaPath;
        final DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet jdbcRs = databaseMetaData.getTables(getCatalogName(), getSchemaName(), "%", new String[] { JdbcTableType.table.getJdbcValue() });
        JdbcMetaDataResultSet<JdbcTableDbMetaDataEnum> rs = new JdbcMetaDataResultSet<JdbcTableDbMetaDataEnum>(jdbcRs, JdbcTableDbMetaDataEnum.values());
        try {
            while ( rs.next() ) {
                B tableBuilder = newTableMetaDataBuilder(databaseMetaData, (JdbcSchemaMetaData<M>) this, rs);
                // M table = tableBuilder.build();
                // tableMetaDataList.put(table.getTableName(), table);
                tableBuilderList.put(tableBuilder.getName().getName(), tableBuilder);
            }
        } finally {
            rs.close();
        }
        HashMap<DottedPath, JdbcForeignKeyMetaDataBuilder> fkMap = new HashMap<DottedPath, JdbcForeignKeyMetaDataBuilder>();
        for ( JdbcTableMetaDataBuilder tableBuilder : this.tableBuilderList.values() ) {
            fkMap.clear();
            jdbcRs = databaseMetaData.getImportedKeys(getCatalogName(), getSchemaName(), tableBuilder.getTableName());
            JdbcMetaDataResultSet<JdbcImportKeyDbMetaDataEnum> rsFk = new JdbcMetaDataResultSet<JdbcImportKeyDbMetaDataEnum>(jdbcRs,
                    JdbcImportKeyDbMetaDataEnum.values());
            while ( rsFk.next() ) {
                DottedPath key = JdbcForeignKeyMetaDataBuilder.getKey(rsFk);
                JdbcForeignKeyMetaDataBuilder fkMetaDataBuilder = fkMap.get(key);
                if ( null == fkMetaDataBuilder ) {
                    fkMetaDataBuilder = newJdbcForeignKeyMetaDataBuilder(this, rsFk);
                    fkMap.put(key, fkMetaDataBuilder);
                }
                fkMetaDataBuilder.addEntry(rsFk);
            }
            for ( JdbcForeignKeyMetaDataBuilder fkBuilder : fkMap.values() ) {
                fkBuilder.build();
            }
        }
        for ( JdbcTableMetaDataBuilder tableBuilder : this.tableBuilderList.values() ) {
            tableBuilder.build();
        }
    }

    public JdbcSchemaMetaData<M> build() throws SQLException {
        System.out.println("Jdbc schema metadata builder");
        Map<String, M> tables = new HashMap<String, M>();
        for ( B b : this.tableBuilderList.values() ) {
            M tableMetaData = b.build();
            tables.put(tableMetaData.getTableName(), tableMetaData);
        }
        return newSchemaMetaData(schemaPath, Collections.unmodifiableMap(tables));
    }

    public JdbcSchemaMetaData<M> newSchemaMetaData(DottedPath schemaPath, Map<String, M> tables) {
        return new JdbcSchemaMetaDataImpl<M>(schemaPath, tables);
    }

    @SuppressWarnings("unchecked")
    protected B newTableMetaDataBuilder(DatabaseMetaData dbMetaData, JdbcSchemaMetaData<M> schema, JdbcMetaDataResultSet<JdbcTableDbMetaDataEnum> rs)
            throws SQLException {
        return ( (B) new JdbcTableMetaDataBuilder<M>(connection, dbMetaData, schema, rs) );
    }

    public JdbcForeignKeyMetaDataBuilder newJdbcForeignKeyMetaDataBuilder(JdbcSchemaMetaDataBuilder<M, B> jdbcSchemaMetaDataBuilder,
            JdbcMetaDataResultSet<JdbcImportKeyDbMetaDataEnum> rsFk) throws SQLException {
        return new JdbcForeignKeyMetaDataBuilder<JdbcForeignKeyMetaData>(jdbcSchemaMetaDataBuilder, rsFk);
    }

    @Override
    public DottedPath getSchemaPath() {
        return this.schemaPath;
    }

    @Override
    public Collection<B> getTables() {
        return this.tableBuilderList.values();
    }

    @Override
    public B getTable(String name) {
        return this.tableBuilderList.get(name);
    }

    private String getSchemaName() {
        return schemaPath.getName();
    }

    private String getCatalogName() {
        if ( null == schemaPath || null == schemaPath.getParent() ) {
            return null;
        }
        return schemaPath.getParent().getName();
    }
}
