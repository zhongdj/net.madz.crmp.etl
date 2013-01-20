package net.madz.crmp.db.metadata.mysql.impl.builder;

import java.sql.SQLException;

import net.madz.crmp.db.metadata.jdbc.JdbcForeignKeyMetaData;
import net.madz.crmp.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.crmp.db.metadata.jdbc.impl.builder.JdbcForeignKeyMetaDataBuilder;
import net.madz.crmp.db.metadata.jdbc.impl.builder.JdbcSchemaMetaDataBuilder;
import net.madz.crmp.db.metadata.mysql.MySqlForeignKeyMetaData;
import net.madz.crmp.db.metadata.mysql.impl.MySQLForeignKeyMetaDataImpl;

public class MySQLForeignKeyMetaDataBuilder extends JdbcForeignKeyMetaDataBuilder implements MySqlForeignKeyMetaData {

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
