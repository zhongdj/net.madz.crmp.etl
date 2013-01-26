package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
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
        stmt.executeQuery("use information_schema;");
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("select * from SCHEMATA where schema_name = '" + schemaPath.getName() + "'");
            while ( rs.next() && rs.getRow() == 1 ) {
                charSet = rs.getString("DEFAULT_CHARACTER_SET_NAME");
                collation = rs.getString("DEFAULT_COLLATION_NAME");
            }
            // Construct Table builders and append
            rs = stmt.executeQuery("SELECT * FROM tables WHERE schema_name = '"
                    + schemaPath.getName() + "'");
//            rs = stmt.executeQuery("SELECT * FROM tables INNER JOIN character_sets ON default_collate_name = table_collation WHERE schema_name = '"
//                    + schemaPath.getName() + "'");
            List<String> tableNames = new LinkedList<String>();
            while (rs.next()) {
                tableNames.add(rs.getString("table_name"));
            }
//            MetaDataResultSet<MySQLTableDbMetaDataEnum> rsMd = new MetaDataResultSet<MySQLTableDbMetaDataEnum>(rs, MySQLTableDbMetaDataEnum.values());
            for ( String name : tableNames ) {
                final MySQLTableMetaDataBuilder table = new MySQLTableMetaDataBuilderImpl(this, name).build(conn);
                appendTableMetaDataBuilder(table);
            }
            // Construct foreignKeys
            for ( MySQLTableMetaDataBuilder table : this.tableBuilderList.values() ) {
                rs = stmt.executeQuery("SELECT * FROM key_column_usage WHERE table_schema = '" + table.getTablePath().getParent().getName()
                        + "' AND table_name = '" + table.getTableName() + "';");
                while ( rs.next() ) {
                    MySQLForeignKeyMetaDataBuilder fkBuilder = new MySQLForeignKeyMetaDataBuilderImpl(table).build(conn);
                    table.appendForeignKeyMetaDataBuilder(fkBuilder);
                }
            }
        } finally {
            rs.close();
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
        return new MySQLSchemaMetaDataImpl(this);
    }
}
