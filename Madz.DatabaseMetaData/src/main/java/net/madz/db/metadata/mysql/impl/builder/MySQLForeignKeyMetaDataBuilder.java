package net.madz.db.metadata.mysql.impl.builder;

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
    public JdbcForeignKeyMetaData build() throws SQLException {
        System.out.println("MySql foreign key builder");
        super.build();
        return new MySQLForeignKeyMetaDataImpl(this);
    }
}
