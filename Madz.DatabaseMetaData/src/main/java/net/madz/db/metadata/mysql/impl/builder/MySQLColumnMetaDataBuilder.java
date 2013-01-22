package net.madz.db.metadata.mysql.impl.builder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.madz.db.metadata.jdbc.JdbcColumnMetaData;
import net.madz.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.db.metadata.jdbc.impl.builder.JdbcColumnMetaDataBuilder;
import net.madz.db.metadata.jdbc.impl.builder.JdbcTableMetaDataBuilder;
import net.madz.db.metadata.mysql.MySQLColumnMetaData;
import net.madz.db.metadata.mysql.MySQLColumnTypeEnum;
import net.madz.db.metadata.mysql.impl.MySQLColumnMetaDataImpl;

public class MySQLColumnMetaDataBuilder extends JdbcColumnMetaDataBuilder implements MySQLColumnMetaData {

    private String charSet;
    private String collation;
    private MySQLColumnTypeEnum columnType;

    public MySQLColumnMetaDataBuilder(JdbcTableMetaDataBuilder tableBuilder, JdbcMetaDataResultSet colRs) throws SQLException {
        super(tableBuilder, colRs);
    }

    @Override
    public void build(Connection conn) throws SQLException {
        System.out.println("Mysql column builder");
        super.build(conn);
        Statement stmt = conn.createStatement();
        stmt.executeQuery("use information_schema;");
        ResultSet result = stmt.executeQuery("select * from COLUMNS WHERE TABLE_SCHEMA= '" + super.name.getParent().getParent().getName()
                + "' and TABLE_NAME='" + super.name.getParent().getName() + "' and  COLUMN_NAME = '" + super.getColumnName() + "'");
        while ( result.next() ) {
            charSet = result.getString("CHARACTER_SET_NAME");
            collation = result.getString("COLLATION_NAME");
        }
    }

    @Override
    public JdbcColumnMetaData getCopy() {
        super.getCopy();
        return new MySQLColumnMetaDataImpl(this);
    }

    @Override
    public String getCharacterSet() {
        return this.charSet;
    }

    @Override
    public String getCollation() {
        return this.collation;
    }

    @Override
    public MySQLColumnTypeEnum getColumnType() {
        // TODO Auto-generated method stub
        return null;
    }
}
