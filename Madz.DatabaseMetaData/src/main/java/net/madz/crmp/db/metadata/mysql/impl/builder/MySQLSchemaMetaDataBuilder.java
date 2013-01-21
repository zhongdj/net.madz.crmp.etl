package net.madz.crmp.db.metadata.mysql.impl.builder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;

import net.madz.crmp.db.core.impl.DbConfigurationManagement;
import net.madz.crmp.db.metadata.DottedPath;
import net.madz.crmp.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.crmp.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.crmp.db.metadata.jdbc.impl.builder.JdbcForeignKeyMetaDataBuilder;
import net.madz.crmp.db.metadata.jdbc.impl.builder.JdbcSchemaMetaDataBuilder;
import net.madz.crmp.db.metadata.jdbc.impl.builder.JdbcTableMetaDataBuilder;
import net.madz.crmp.db.metadata.mysql.impl.MySQLSchemaMetaDataImpl;

public class MySQLSchemaMetaDataBuilder extends JdbcSchemaMetaDataBuilder {

    private Connection conn;
    private Map<String, JdbcTableMetaData> tables;
    private String charSet;
    private String collation;

    public MySQLSchemaMetaDataBuilder(Connection conn, DottedPath schemaPath) throws SQLException {
        super(conn, schemaPath);
        this.conn = conn;
        Statement stmt = conn.createStatement();
        stmt.executeQuery("use information_schema;");
        ResultSet rs = stmt.executeQuery("select * from SCHEMATA where schema_name = '" + schemaPath.getName() + "'");
        while ( rs.next() && rs.getRow() == 1 ) {
            charSet = rs.getString("DEFAULT_CHARACTER_SET_NAME");
            collation = rs.getString("DEFAULT_COLLATION_NAME");
        }
    }

    @Override
    public JdbcSchemaMetaData build() throws SQLException {
        System.out.println("Mysql schema builder");
        JdbcSchemaMetaData build = super.build();
        return build;
    }

    @Override
    public JdbcSchemaMetaData newSchemaMetaData(DottedPath schemaPath, Map tables) {
        MySQLSchemaMetaDataImpl metaData = new MySQLSchemaMetaDataImpl(schemaPath, tables, this.getCharSet(), this.getCollation());
        return metaData;
    }

    @Override
    protected JdbcTableMetaDataBuilder newTableMetaDataBuilder(DatabaseMetaData dbMetaData, JdbcSchemaMetaData schema, JdbcMetaDataResultSet rs)
            throws SQLException {
        return new MySQLTableMetaDataBuilder(connection, dbMetaData, schema, rs);
    }

    @Override
    public JdbcForeignKeyMetaDataBuilder newJdbcForeignKeyMetaDataBuilder(JdbcSchemaMetaDataBuilder jdbcSchemaMetaDataBuilder, JdbcMetaDataResultSet rsFk)
            throws SQLException {
        return new MySQLForeignKeyMetaDataBuilder(jdbcSchemaMetaDataBuilder, rsFk);
    }

    public Connection getConn() {
        return conn;
    }

    public DottedPath getSchemaPath() {
        return super.getSchemaPath();
    }

    public Collection<JdbcTableMetaData> getTables() {
        return tables.values();
    }

    public String getCharSet() {
        return charSet;
    }

    public String getCollation() {
        return collation;
    }

    public static void main(String[] args) {
        try {
            MySQLSchemaMetaDataBuilder builder = new MySQLSchemaMetaDataBuilder(DbConfigurationManagement.createConnection("crmp", false), new DottedPath(
                    "crmp"));
            builder.build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
