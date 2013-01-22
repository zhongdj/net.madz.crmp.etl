package net.madz.db.metadata.mysql.impl.builder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;

import net.madz.db.core.impl.DbConfigurationManagement;
import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.db.metadata.jdbc.impl.JdbcSchemaMetaDataImpl;
import net.madz.db.metadata.jdbc.impl.builder.JdbcForeignKeyMetaDataBuilder;
import net.madz.db.metadata.jdbc.impl.builder.JdbcSchemaMetaDataBuilder;
import net.madz.db.metadata.jdbc.impl.builder.JdbcTableMetaDataBuilder;
import net.madz.db.metadata.mysql.MySQLSchemaMetaData;
import net.madz.db.metadata.mysql.impl.MySQLSchemaMetaDataImpl;

public class MySQLSchemaMetaDataBuilder extends JdbcSchemaMetaDataBuilder implements MySQLSchemaMetaData {

    private String charSet;
    private String collation;

    public MySQLSchemaMetaDataBuilder(DottedPath schemaPath) throws SQLException {
        super(schemaPath);
    }

    @Override
    public void build(Connection conn) throws SQLException {
        System.out.println("Mysql schema builder");
        super.build(conn);
        Statement stmt = conn.createStatement();
        stmt.executeQuery("use information_schema;");
        ResultSet rs = stmt.executeQuery("select * from SCHEMATA where schema_name = '" + super.schemaPath.getName() + "'");
        while ( rs.next() && rs.getRow() == 1 ) {
            charSet = rs.getString("DEFAULT_CHARACTER_SET_NAME");
            collation = rs.getString("DEFAULT_COLLATION_NAME");
        }
    }

    @Override
    public MySQLSchemaMetaDataImpl getCopy() throws SQLException {
        super.getCopy();
        return new MySQLSchemaMetaDataImpl(schemaPath, super.tableBuilderList, charSet, collation);
    }

    @Override
    public JdbcSchemaMetaData newSchemaMetaData(DottedPath schemaPath, Map tables) {
        MySQLSchemaMetaDataImpl metaData = new MySQLSchemaMetaDataImpl(schemaPath, tables, this.getCharSet(), this.getCollation());
        return metaData;
    }

    @Override
    protected JdbcTableMetaDataBuilder newTableMetaDataBuilder(DatabaseMetaData dbMetaData, JdbcSchemaMetaData schema, JdbcMetaDataResultSet rs)
            throws SQLException {
        return new MySQLTableMetaDataBuilder(dbMetaData, schema, rs);
    }

    @Override
    public JdbcForeignKeyMetaDataBuilder newJdbcForeignKeyMetaDataBuilder(JdbcSchemaMetaDataBuilder jdbcSchemaMetaDataBuilder, JdbcMetaDataResultSet rsFk)
            throws SQLException {
        return new MySQLForeignKeyMetaDataBuilder(jdbcSchemaMetaDataBuilder, rsFk);
    }

    public DottedPath getSchemaPath() {
        return super.getSchemaPath();
    }

    @Override
    public String getCharSet() {
        return charSet;
    }

    @Override
    public String getCollation() {
        return collation;
    }

    @Override
    public String toString() {
        return super.toString() + "MySQLSchemaMetaDataBuilder [charSet=" + charSet + ", collation=" + collation + "]";
    }
    
}
