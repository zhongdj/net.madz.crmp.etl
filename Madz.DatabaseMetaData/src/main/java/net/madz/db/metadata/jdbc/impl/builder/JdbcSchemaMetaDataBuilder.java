package net.madz.db.metadata.jdbc.impl.builder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.db.metadata.jdbc.impl.JdbcSchemaMetaDataImpl;
import net.madz.db.metadata.jdbc.impl.enums.JdbcImportKeyDbMetaDataEnum;
import net.madz.db.metadata.jdbc.impl.enums.JdbcTableDbMetaDataEnum;
import net.madz.db.metadata.jdbc.type.JdbcTableType;

public class JdbcSchemaMetaDataBuilder implements JdbcSchemaMetaData {

    protected DottedPath schemaPath;
    protected Map<String, JdbcTableMetaDataBuilder> tableBuilderList = new TreeMap<String, JdbcTableMetaDataBuilder>(String.CASE_INSENSITIVE_ORDER);

    public JdbcSchemaMetaDataBuilder(final DottedPath schemaPath) throws SQLException {
        super();
        this.schemaPath = schemaPath;
    }

    public void build(final Connection connection) throws SQLException {
        System.out.println("Jdbc schema metadata builder");
        final DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet jdbcRs = databaseMetaData.getTables(getCatalogName(), getSchemaName(), "%", new String[] { JdbcTableType.table.getJdbcValue() });
        JdbcMetaDataResultSet<JdbcTableDbMetaDataEnum> rs = new JdbcMetaDataResultSet<JdbcTableDbMetaDataEnum>(jdbcRs, JdbcTableDbMetaDataEnum.values());
        try {
            while ( rs.next() ) {
                JdbcTableMetaDataBuilder tableBuilder = newTableMetaDataBuilder(databaseMetaData, (JdbcSchemaMetaData) this, rs);
                tableBuilder.build(connection);
                this.addTable(tableBuilder);
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
                    fkMetaDataBuilder.build(connection);
                    fkMap.put(key, fkMetaDataBuilder);
                }
                fkMetaDataBuilder.addEntry(rsFk);
            }
            for ( JdbcForeignKeyMetaDataBuilder fkBuilder : fkMap.values() ) {
                fkBuilder.getCopy();
            }
        }
    }

    public void addTable(JdbcTableMetaDataBuilder tableBuilder) {
        tableBuilderList.put(tableBuilder.getName().getName(), tableBuilder);
    }

    public JdbcSchemaMetaData getCopy() throws SQLException {
        Map<String, JdbcTableMetaData> tables = new HashMap<String, JdbcTableMetaData>();
        for ( JdbcTableMetaDataBuilder b : this.tableBuilderList.values() ) {
            JdbcTableMetaData tableMetaData = b.getCopy();
            tables.put(tableMetaData.getTableName(), tableMetaData);
        }
        return newSchemaMetaData(schemaPath, Collections.unmodifiableMap(tables));
    }

    public JdbcSchemaMetaData newSchemaMetaData(DottedPath schemaPath, Map<String, JdbcTableMetaData> tables) {
        return new JdbcSchemaMetaDataImpl(schemaPath, tables);
    }

    protected JdbcTableMetaDataBuilder newTableMetaDataBuilder(DatabaseMetaData dbMetaData, JdbcSchemaMetaData schema,
            JdbcMetaDataResultSet<JdbcTableDbMetaDataEnum> rs) throws SQLException {
        return new JdbcTableMetaDataBuilder(dbMetaData, schema, rs);
    }

    public JdbcForeignKeyMetaDataBuilder newJdbcForeignKeyMetaDataBuilder(JdbcSchemaMetaDataBuilder jdbcSchemaMetaDataBuilder,
            JdbcMetaDataResultSet<JdbcImportKeyDbMetaDataEnum> rsFk) throws SQLException {
        return new JdbcForeignKeyMetaDataBuilder(jdbcSchemaMetaDataBuilder, rsFk);
    }

    @Override
    public DottedPath getSchemaPath() {
        return this.schemaPath;
    }

    @Override
    public Collection getTables() {
        return this.tableBuilderList.values();
    }

    @Override
    public JdbcTableMetaDataBuilder getTable(String name) {
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

    @Override
    public String toString() {
        return "JdbcSchemaMetaDataBuilder [schemaPath=" + schemaPath + ", tableBuilderList=" + tableBuilderList + "]";
    }
    
}
