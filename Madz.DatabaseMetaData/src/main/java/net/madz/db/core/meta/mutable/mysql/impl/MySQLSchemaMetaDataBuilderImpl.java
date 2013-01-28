package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import net.madz.db.core.meta.mutable.impl.BasedSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;
import net.madz.db.utils.ResourceManagementUtils;

public class MySQLSchemaMetaDataBuilderImpl
        extends
        BasedSchemaMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLSchemaMetaDataBuilder {

    private String charSet;
    private String collation;

    public MySQLSchemaMetaDataBuilderImpl(DottedPath schemaPath) throws SQLException {
        super(schemaPath);
    }

    public MySQLSchemaMetaDataBuilder build(Connection conn) throws SQLException {
        System.out.println("Mysql schema builder");
        // Get MySql information
        final Statement stmt = conn.createStatement();
        stmt.executeQuery("USE information_schema;");
        ResultSet rs = null;
        final Map<String, MySQLTableMetaDataBuilder> tableBuilders = new HashMap<String, MySQLTableMetaDataBuilder>();
        try {
            rs = stmt.executeQuery("SELECT * FROM schemata WHERE schema_name = '" + schemaPath.getName() + "'");
            while ( rs.next() && rs.getRow() == 1 ) {
                charSet = rs.getString("default_character_set_name");
                collation = rs.getString("default_collation_name");
            }
        } finally {
            ResourceManagementUtils.closeResultSet(rs);
        }
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
        // For empty database
        if ( 0 >= tableNames.size() ) {
            return this;
        }
        for ( String name : tableNames ) {
            final MySQLTableMetaDataBuilder table = new MySQLTableMetaDataBuilderImpl(this, name).build(conn);
            tableBuilders.put(name, table);
            appendTableMetaDataBuilder(table);
        }
        // Construct foreignKeys
        final Map<String, LinkedList<String>> fkNames = new HashMap<String, LinkedList<String>>();
        for ( String name : tableNames ) {
            final LinkedList<String> fks = new LinkedList<String>();
            try {
                rs = stmt.executeQuery("SELECT * FROM referential_constraints WHERE constraint_schema = '" + this.schemaPath.getName() + "' AND table_name = '"
                        + name + "';");
                while ( rs.next() ) {
                    fks.add(rs.getString("constraint_name"));
                }
            } finally {
                ResourceManagementUtils.closeResultSet(rs);
            }
            fkNames.put(name, fks);
        }
        // For database with no foreign keys
        if ( 0 >= fkNames.size() ) {
            return this;
        }
        for ( String tableName : fkNames.keySet() ) {
            final LinkedList<String> fks = fkNames.get(tableName);
            for ( String name : fks ) {
                final MySQLTableMetaDataBuilder tableBuilder = tableBuilders.get(tableName);
                final MySQLForeignKeyMetaDataBuilder fkBuilder = new MySQLForeignKeyMetaDataBuilderImpl(tableBuilder, this.schemaPath.append(tableName).append(
                        name)).build(conn);
                tableBuilder.appendForeignKeyMetaDataBuilder(fkBuilder);
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
    public MySQLSchemaMetaData getMetaData() {
        LinkedList<MySQLTableMetaData> tables = new LinkedList<MySQLTableMetaData>();
        for ( MySQLTableMetaDataBuilder table : this.tableBuilderMap.values() ) {
            tables.add(table.getMetaData());
        }
        for ( MySQLTableMetaData table : tables ) {
            List<MySQLColumnMetaData> columns = table.getColumns();
            for ( MySQLColumnMetaData colMetaData : columns ) {
                colMetaData.setTable(table);
            }
        }
        return new MySQLSchemaMetaDataImpl(this, tables);
    }

    @Override
    public MySQLTableMetaDataBuilder getTableBuilder(String tableName) {
        return this.tableBuilderMap.get(tableName);
    }
}
