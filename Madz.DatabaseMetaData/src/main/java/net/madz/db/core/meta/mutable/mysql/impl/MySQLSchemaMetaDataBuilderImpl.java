package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;
import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.impl.MySQLSchemaMetaDataImpl;
import net.madz.db.core.meta.immutable.mysql.impl.MySQLTableMetaDataImpl;
import net.madz.db.core.meta.mutable.impl.BasedSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;
import net.madz.db.utils.MessageConsts;
import net.madz.db.utils.ResourceManagementUtils;
import net.madz.db.utils.Utilities;

public class MySQLSchemaMetaDataBuilderImpl
        extends
        BasedSchemaMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLSchemaMetaDataBuilder {

    private String charSet;
    private String collation;
    private Integer lowerCaseTableNames = 2; // default value is 2 under mac

    public MySQLSchemaMetaDataBuilderImpl(final String databaseName) throws SQLException {
        super(databaseName);
    }

    public MySQLSchemaMetaDataBuilder build(JdbcSchemaMetaData jMetaData) {
        // As Access in on windows, so it is case insensitive
        this.tableBuilderMap = new TreeMap<String, MySQLTableMetaDataBuilder>(String.CASE_INSENSITIVE_ORDER);
        // [TODO]Set CharacterSet and Collation from configuration file
        // For database without tables, just return
        if ( 0 >= jMetaData.getTables().size() ) {
            return this;
        }
        // Build all tables
        final Map<String, MySQLTableMetaDataBuilder> tableBuilders = new HashMap<String, MySQLTableMetaDataBuilder>();
        for ( final JdbcTableMetaData tMetadata : jMetaData.getTables() ) {
            final MySQLTableMetaDataBuilder table = new MySQLTableMetaDataBuilderImpl(this, tMetadata.getTableName()).build(tMetadata);
            tableBuilders.put(tMetadata.getTableName(), table);
            appendTableMetaDataBuilder(table);
        }
        for ( JdbcTableMetaData tMetadata : jMetaData.getTables() ) {
            Collection<JdbcForeignKeyMetaData> foreignKeySet = tMetadata.getForeignKeySet();
            for ( JdbcForeignKeyMetaData jFk : foreignKeySet ) {
                final MySQLForeignKeyMetaDataBuilder fkBuilder = new MySQLForeignKeyMetaDataBuilderImpl(tableBuilders.get(tMetadata.getTableName()),
                        jFk.getForeignKeyName()).build(jFk);
                tableBuilders.get(tMetadata.getTableName()).appendForeignKeyMetaDataBuilder(fkBuilder);
            }
        }
        return this;
    }

    public MySQLSchemaMetaDataBuilder build(Connection conn) throws SQLException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = null;
            try {
                rs = stmt.executeQuery("SHOW VARIABLES LIKE 'LOWER_CASE_TABLE_NAMES';");
                if ( rs.next() ) {
                    lowerCaseTableNames = rs.getInt("Value");
                    if ( lowerCaseTableNames == 0 ) {
                        this.tableBuilderMap = new TreeMap<String, MySQLTableMetaDataBuilder>();
                    } else {
                        this.tableBuilderMap = new TreeMap<String, MySQLTableMetaDataBuilder>(String.CASE_INSENSITIVE_ORDER);
                    }
                }
            } finally {
                ResourceManagementUtils.closeResultSet(rs);
            }
            // We should keep the context of querying in information_schema
            // database.
            stmt.executeQuery("USE information_schema;");
            setCharacterSetAndCollation(stmt);
            // Build all tables
            final Map<String, MySQLTableMetaDataBuilder> tableBuilders = new HashMap<String, MySQLTableMetaDataBuilder>();
            rs = stmt.executeQuery("SELECT * FROM tables INNER JOIN collations ON  table_collation = collation_name WHERE table_schema = '"
                    + Utilities.handleSpecialCharacters(this.schemaName) + "'");
            final MetaDataResultSet<MySQLTableDbMetaDataEnum> mysqlRs = new MetaDataResultSet<MySQLTableDbMetaDataEnum>(rs, MySQLTableDbMetaDataEnum.values());
            while ( mysqlRs.next() ) {
                final MySQLTableMetaDataBuilder table = new MySQLTableMetaDataBuilderImpl(this, mysqlRs).build(conn);
                tableBuilders.put(table.getTableName(), table);
                appendTableMetaDataBuilder(table);
            }
            // Build all foreign keys
            rs = stmt
                    .executeQuery("SELECT * FROM referential_constraints join key_column_usage on referential_constraints.constraint_schema=key_column_usage.constraint_schema AND referential_constraints.constraint_name=key_column_usage.constraint_name where referential_constraints.constraint_schema='"
                            + Utilities.handleSpecialCharacters(this.schemaName)
                            + "' AND key_column_usage.referenced_table_name IS NOT NULL AND key_column_usage.referenced_column_name IS NOT NULL;");
            final MetaDataResultSet<MySQLForeignKeyDbMetaDataEnum> fkRs = new MetaDataResultSet<MySQLForeignKeyDbMetaDataEnum>(rs,
                    MySQLForeignKeyDbMetaDataEnum.values());
            while ( fkRs.next() ) {
                final String constraintName = fkRs.get(MySQLForeignKeyDbMetaDataEnum.CONSTRAINT_NAME);
                final MySQLTableMetaDataBuilder tableBuilder = this.getTableBuilder(fkRs.get(MySQLForeignKeyDbMetaDataEnum.TABLE_NAME));
                MySQLForeignKeyMetaDataBuilder foreignKeyBuilder = tableBuilder.getForeignKeyBuilder(constraintName);
                if ( null == foreignKeyBuilder ) {
                    foreignKeyBuilder = new MySQLForeignKeyMetaDataBuilderImpl(tableBuilder, fkRs);
                    tableBuilder.appendForeignKeyMetaDataBuilder(foreignKeyBuilder);
                }
                foreignKeyBuilder.addEntry(fkRs);
            }
            return this;
        } finally {
            stmt.close();
        }
    }

    @Override
    public String getCharSet() {
        return this.charSet;
    }

    @Override
    public String getCollation() {
        return this.collation;
    }

    @Override
    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    @Override
    public void setCollation(String collation) {
        this.collation = collation;
    }

    @Override
    protected MySQLSchemaMetaData createMetaData() {
        MySQLSchemaMetaDataImpl result = new MySQLSchemaMetaDataImpl(this);
        this.constructedMetaData = result;
        if ( 0 >= this.tableList.size() ) {
            return this.constructedMetaData;
        }
        final List<MySQLTableMetaData> tables = new LinkedList<MySQLTableMetaData>();
        for ( MySQLTableMetaDataBuilder tableBuilder : this.tableList ) {
            tables.add(tableBuilder.getMetaData());
        }
        // Bind relations
        result.addAllTables(tables);
        for ( MySQLTableMetaDataBuilder tableBuilder : this.tableList ) {
            final List<MySQLForeignKeyMetaData> fks = new LinkedList<MySQLForeignKeyMetaData>();
            final Collection<MySQLForeignKeyMetaDataBuilder> foreignKeyBuilderSet = tableBuilder.getForeignKeyBuilderSet();
            final MySQLTableMetaDataImpl table = (MySQLTableMetaDataImpl) result.getTable(tableBuilder.getTableName());
            for ( MySQLForeignKeyMetaDataBuilder fkBuilder : foreignKeyBuilderSet ) {
                fks.add(fkBuilder.getMetaData());
            }
            table.addAllFks(fks);
        }
        return constructedMetaData;
    }

    @Override
    public MySQLTableMetaDataBuilder getTableBuilder(String tableName) {
        return this.tableBuilderMap.get(tableName);
    }

    public Map<String, LinkedList<String>> getFkNamesOfTables(final Statement stmt, final List<String> tableNames) throws SQLException {
        ResultSet rs = null;
        final Map<String, LinkedList<String>> fkNamesOfTables = new HashMap<String, LinkedList<String>>();
        for ( final String tableName : tableNames ) {
            final LinkedList<String> fks = new LinkedList<String>();
            try {
                rs = stmt.executeQuery("SELECT * FROM referential_constraints WHERE constraint_schema = '" + this.schemaPath.getName() + "' AND table_name = '"
                        + tableName + "';");
                while ( rs.next() ) {
                    fks.add(rs.getString("constraint_name"));
                }
            } finally {
                ResourceManagementUtils.closeResultSet(rs);
            }
            if ( fks.size() > 0 ) {
                fkNamesOfTables.put(tableName, fks);
            }
        }
        return fkNamesOfTables;
    }

    public List<String> getTableNames(final Statement stmt) throws SQLException {
        ResultSet rs = null;
        final List<String> tableNames = new LinkedList<String>();
        try {
            // Construct Table builders and append
            rs = stmt.executeQuery("SELECT * FROM tables WHERE table_schema = '" + schemaPath.getName() + "'");
            while ( rs.next() ) {
                tableNames.add(rs.getString("table_name"));
            }
        } finally {
            ResourceManagementUtils.closeResultSet(rs);
        }
        return tableNames;
    }

    private void setCharacterSetAndCollation(final Statement stmt) throws SQLException {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT * FROM schemata WHERE schema_name = '" + schemaPath.getName() + "'");
            if ( rs.next() ) {
                setCharSet(rs.getString("default_character_set_name"));
                setCollation(rs.getString("default_collation_name"));
            } else {
                throw new IllegalStateException(MessageConsts.DATABASE_NOT_EXISTS_IN_DB_SERVER);
            }
        } finally {
            ResourceManagementUtils.closeResultSet(rs);
        }
    }

    @Override
    public Integer getLowerCaseTableNames() {
        return this.lowerCaseTableNames;
    }
}
