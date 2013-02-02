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

import net.madz.db.core.meta.DottedPath;
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

public class MySQLSchemaMetaDataBuilderImpl
        extends
        BasedSchemaMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLSchemaMetaDataBuilder {

    private String charSet;
    private String collation;

    public MySQLSchemaMetaDataBuilderImpl(final DottedPath schemaPath) throws SQLException {
        super(schemaPath);
    }

    public MySQLSchemaMetaDataBuilder build(Connection conn) throws SQLException {
        final Statement stmt = conn.createStatement();
        // We should keep the context of querying in information_schema
        // database.
        stmt.executeQuery("USE information_schema;");
        setCharacterSetAndCollation(stmt);
        
        final List<String> tableNames = getTableNames(stmt);
        // For database without tables, just return
        if ( 0 >= tableNames.size() ) {
            return this;
        }
        // Build all tables
        final Map<String, MySQLTableMetaDataBuilder> tableBuilders = new HashMap<String, MySQLTableMetaDataBuilder>();
        for ( final String name : tableNames ) {
            final MySQLTableMetaDataBuilder table = new MySQLTableMetaDataBuilderImpl(this, name).build(conn);
            tableBuilders.put(name, table);
            appendTableMetaDataBuilder(table);
        }
        
        final Map<String, LinkedList<String>> fkNamesOfTables = getFkNamesOfTables(stmt, tableNames);
        // For database with no foreign keys
        if ( 0 >= fkNamesOfTables.size() ) {
            return this;
        }
        // Build all foreign keys
        for ( String tableName : fkNamesOfTables.keySet() ) {
            final LinkedList<String> fks = fkNamesOfTables.get(tableName);
            for ( String name : fks ) {
                final MySQLForeignKeyMetaDataBuilder fkBuilder = new MySQLForeignKeyMetaDataBuilderImpl(tableBuilders.get(tableName), this.schemaPath.append(
                        tableName).append(name)).build(conn);
                tableBuilders.get(tableName).appendForeignKeyMetaDataBuilder(fkBuilder);
            }
        }
        return this;
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

    public void setCharacterSetAndCollation(final Statement stmt) throws SQLException {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT * FROM schemata WHERE schema_name = '" + schemaPath.getName() + "'");
            if ( rs.next() && rs.getRow() == 1 ) {
                charSet = rs.getString("default_character_set_name");
                collation = rs.getString("default_collation_name");
            } else {
                throw new IllegalStateException(MessageConsts.ONLY_ONE_SCHEMA_INFORMATION_IS_OK);
            }
        } finally {
            ResourceManagementUtils.closeResultSet(rs);
        }
    }
}
