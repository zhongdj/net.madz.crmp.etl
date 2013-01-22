package net.madz.db.metadata.mysql.impl.builder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.db.metadata.jdbc.impl.builder.JdbcColumnMetaDataBuilder;
import net.madz.db.metadata.jdbc.impl.builder.JdbcIndexMetaDataBuilder;
import net.madz.db.metadata.jdbc.impl.builder.JdbcTableMetaDataBuilder;
import net.madz.db.metadata.mysql.MySQLEngineEnum;
import net.madz.db.metadata.mysql.MySQLTableMetaData;
import net.madz.db.metadata.mysql.MySQLTableTypeEnum;
import net.madz.db.metadata.mysql.impl.MySQLTableMetaDataImpl;

public class MySQLTableMetaDataBuilder extends JdbcTableMetaDataBuilder implements MySQLTableMetaData {

    private MySQLTableTypeEnum type;
    private MySQLEngineEnum engine;
    private String charSet;
    private String collation;

    public MySQLTableMetaDataBuilder(DatabaseMetaData dbMetaData, JdbcSchemaMetaData schema, JdbcMetaDataResultSet rs) throws SQLException {
        super(dbMetaData, schema, rs);
    }

    @Override
    public void build(Connection conn) throws SQLException {
        System.out.println("Mysql table builder");
        super.build(conn);
        Statement stmt = conn.createStatement();
        stmt.executeQuery("use information_schema");
        ResultSet mysqlRs = stmt.executeQuery("select * from TABLES where table_name='" + super.getTableName() + "' and table_schema='" + super.getSchemaName()
                + "';");
        while ( mysqlRs.next() && mysqlRs.getRow() == 1 ) {
            engine = MySQLEngineEnum.valueOf(mysqlRs.getString("ENGINE"));
            type = MySQLTableTypeEnum.getType(mysqlRs.getString("TABLE_TYPE"));
            collation = mysqlRs.getString("TABLE_COLLATION");
            String[] result = collation.split("_");
            collation = result[0];
        }
    }

    @Override
    public JdbcTableMetaData getCopy() throws SQLException {
        super.getCopy();
        return new MySQLTableMetaDataImpl(this);
    }

    @Override
    protected JdbcIndexMetaDataBuilder newJdbcIndexMetaDataBuilder(JdbcTableMetaDataBuilder jdbcTableMetaDataBuilder, JdbcMetaDataResultSet ixRs)
            throws SQLException {
        return new MySQLIndexMetaDataBuilder(jdbcTableMetaDataBuilder, ixRs);
    }

    @Override
    protected JdbcColumnMetaDataBuilder newJdbcColumnMetaDataBuilder(JdbcTableMetaDataBuilder tableBuilder, JdbcMetaDataResultSet colRs) throws SQLException {
        return new MySQLColumnMetaDataBuilder(tableBuilder, colRs);
    }

    @Override
    public MySQLTableTypeEnum getTableType() {
        return this.type;
    }

    @Override
    public MySQLEngineEnum getEngine() {
        return this.engine;
    }

    @Override
    public String getCharacterSet() {
        return this.charSet;
    }

    @Override
    public String getCollation() {
        return this.collation;
    }
}
