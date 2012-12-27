package net.madz.crmp.db.core;

/**
 * This interface is the facade of this module. It provides checking,comparing,
 * cloning, and dropping database schema features.
 * 
 * Before you invoke methods, please make sure that you configure Databases.xml
 * and Mapping.xml files.
 * 
 * @author Barry, Tracy
 * 
 */
public interface DatabaseSchemaUtils {

    /**
     * This method check whether the specified database exists.
     * 
     * @param databaseName
     * @param isCopy
     * @return
     */
    boolean databaseExists(String databaseName, boolean isCopy);

    /**
     * This method compares schema of two databases. True will be returned when
     * only database names different.
     * 
     * @param sourceDatabaseName
     * @param targetDatabaseName
     * @return
     */
    boolean compareDatabaseSchema(String sourceDatabaseName, String targetDatabaseName);

    /**
     * This method will clone database schema of source database to target
     * database. If target database already exists, IllegalOperationException
     * will be thrown.
     * 
     * @param sourceDatabaseName
     * @param targetDatabaseName
     *            , optional
     * @return
     * @throws IllegalOperationException
     */
    String cloneDatabaseSchema(String sourceDatabaseName, String targetDatabaseName) throws IllegalOperationException;

    /**
     * This method drop database according to specified database name and isCopy
     * flag. Some exception will be thrown, check when implementation.
     * 
     * @param databaseName
     * @return
     */
    boolean dropDatabase(String databaseName);
}
