package net.madz.crmp.db.core.impl.jdbc;

import java.sql.Connection;

import net.madz.crmp.db.core.AbsDatabaseGenerator;
import net.madz.crmp.db.metadata.jdbc.JdbcSchemaMetaData;


public class JdbcDatabaseGenerator extends AbsDatabaseGenerator {

    public JdbcDatabaseGenerator(Connection conn) {
        super(conn);
    }

    @Override
    public String generateDatabase(JdbcSchemaMetaData metadata, String targetDatabaseName) {
        
        System.out.println("to be generated" + metadata);
        return null;
    }
}
