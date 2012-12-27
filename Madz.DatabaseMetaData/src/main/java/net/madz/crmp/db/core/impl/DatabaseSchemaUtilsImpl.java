package net.madz.crmp.db.core.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.madz.crmp.db.core.DatabaseSchemaUtils;
import net.madz.crmp.db.core.IllegalOperationException;

public class DatabaseSchemaUtilsImpl implements DatabaseSchemaUtils {

    @Override
    public boolean databaseExists(String databaseName, boolean isCopy) {
        Connection conn = null;
        if ( isCopy ) {
            conn = DbConfigurationManagement.createConnection(databaseName, true);
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("show databases");
                while ( rs.next() ) {
                    String dbName = rs.getString("Database");
                    if ( databaseName.equals(dbName) ) {
                        return true;
                    }
                }
                return false;
            } catch (SQLException e) {
                return false;
            }
        } else {
            conn = DbConfigurationManagement.createConnection(databaseName, false);
            return true;
        }
    }

    @Override
    public boolean compareDatabaseSchema(String sourceDatabaseName, String targetDatabaseName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String cloneDatabaseSchema(String sourceDatabaseName, String targetDatabaseName) throws IllegalOperationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean dropDatabase(String databaseName, boolean isCopy) {
        //TODO how to handle access db
        return false;
    }
}
