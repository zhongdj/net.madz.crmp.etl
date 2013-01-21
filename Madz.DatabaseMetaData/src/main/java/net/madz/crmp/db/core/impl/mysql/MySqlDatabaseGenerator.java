package net.madz.crmp.db.core.impl.mysql;

import java.sql.Connection;

import net.madz.crmp.db.core.AbsDatabaseGenerator;
import net.madz.crmp.db.metadata.jdbc.JdbcSchemaMetaData;

public class MySQLDatabaseGenerator extends AbsDatabaseGenerator {

    public MySQLDatabaseGenerator(Connection conn) {
        super(conn);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String generateDatabase(JdbcSchemaMetaData metadata, String targetDatabaseName) {
        return null;
    }
}
