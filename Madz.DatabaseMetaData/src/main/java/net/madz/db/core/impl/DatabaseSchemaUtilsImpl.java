package net.madz.db.core.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.madz.db.core.AbsDatabaseGenerator;
import net.madz.db.core.AbsSchemaMetaDataParser;
import net.madz.db.core.DatabaseSchemaUtils;
import net.madz.db.core.IllegalOperationException;
import net.madz.db.metadata.SchemaMetaData;
import net.madz.db.metadata.jdbc.JdbcMetaData;
import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;

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
    public boolean compareDatabaseSchema(String sourceDatabaseName, String targetDatabaseName) throws SQLException {
        AbsSchemaMetaDataParser sourceDbParser = DbOperatorFactoryImpl.getInstance().createSchemaParser(sourceDatabaseName, false);
        AbsSchemaMetaDataParser targetDbParser = DbOperatorFactoryImpl.getInstance().createSchemaParser(targetDatabaseName, true);
        JdbcSchemaMetaData sourceSchemaMetaData = sourceDbParser.parseSchemaMetaData();
        JdbcSchemaMetaData targetSchemaMetaData = targetDbParser.parseSchemaMetaData();
        return sourceSchemaMetaData.equals(targetSchemaMetaData);
    }

    @Override
    public String cloneDatabaseSchema(String sourceDatabaseName, String targetDatabaseName) throws IllegalOperationException, SQLException {
        if ( !databaseExists(sourceDatabaseName, false) ) {
            throw new IllegalOperationException("Please make sure configure souce database information.");
        }
        if ( databaseExists(targetDatabaseName, true) ) {
            dropDatabase(targetDatabaseName);
        }
        AbsSchemaMetaDataParser sourceDbParser = DbOperatorFactoryImpl.getInstance().createSchemaParser(sourceDatabaseName, false);
        JdbcSchemaMetaData schemaMetaData = sourceDbParser.parseSchemaMetaData();
        // Upload schemaMetaData to to writer (local or remote)
        //
        AbsDatabaseGenerator databaseGenerator = DbOperatorFactoryImpl.getInstance().createDatabaseGenerator(targetDatabaseName);
        String databaseName = databaseGenerator.generateDatabase(schemaMetaData, targetDatabaseName);
        return databaseName;
    }

    @Override
    public boolean dropDatabase(String databaseName) {
        if ( databaseExists(databaseName, true) ) {
            Connection conn = DbConfigurationManagement.createConnection(databaseName, true);
            Statement stmt;
            try {
                stmt = conn.createStatement();
                stmt.execute("drop database " + databaseName + ";");
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            DbConfigurationManagement.removeDatabaseInfo(databaseName);
            return true;
        }
        DbConfigurationManagement.removeDatabaseInfo(databaseName);
        return false;
    }

    public static void main(String[] args) {
        DatabaseSchemaUtilsImpl impl = new DatabaseSchemaUtilsImpl();
        // boolean result = impl.compareDatabaseSchema("crmp", "crmp2");
        // System.out.println(result);
        try {
            impl.cloneDatabaseSchema("c3", "crmp2000");
        } catch (IllegalOperationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
