package net.madz.db.metadata.mysql.impl.builder;

import java.sql.Connection;
import java.sql.SQLException;

import net.madz.db.metadata.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.db.metadata.jdbc.impl.builder.JdbcForeignKeyMetaDataBuilder;
import net.madz.db.metadata.jdbc.impl.builder.JdbcSchemaMetaDataBuilder;
import net.madz.db.metadata.mysql.MySQLForeignKeyMetaData;
import net.madz.db.metadata.mysql.impl.MySQLForeignKeyMetaDataImpl;

public class MySQLForeignKeyMetaDataBuilder extends JdbcForeignKeyMetaDataBuilder implements MySQLForeignKeyMetaData {

    public MySQLForeignKeyMetaDataBuilder(JdbcSchemaMetaDataBuilder schema, JdbcMetaDataResultSet rsFk) throws SQLException {
        super(schema, rsFk);
    }

    @Override
    public void build(Connection conn) throws SQLException {
        System.out.println("MySql foreign key builder");
        super.build(conn);
    }

    @Override
    public JdbcForeignKeyMetaData getCopy() throws SQLException {
        super.getCopy();
        return new MySQLForeignKeyMetaDataImpl(this);
    }
}
