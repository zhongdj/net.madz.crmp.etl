package net.madz.crmp.db.core.impl.mysql;

import java.sql.Connection;

import net.madz.crmp.db.core.AbsDatabaseGenerator;
import net.madz.crmp.db.metadata.jdbc.JdbcSchemaMetaData;

public class MySqlDatabaseGenerator extends AbsDatabaseGenerator {

    public MySqlDatabaseGenerator(Connection conn) {
        super(conn);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String generateDatabase(JdbcSchemaMetaData metadata, String targetDatabaseName) {
        return null;
    }
}
