package net.madz.db.core;

import java.sql.SQLException;
import java.util.List;

import javax.xml.bind.JAXBException;

import net.madz.db.core.impl.validation.mysql.ErrorEntry;

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
     * @throws SQLException
     */
    boolean databaseExists(String databaseName, boolean isCopy) throws SQLException;

    /**
     * This method compares schema of two databases. True will be returned when
     * only database names different.
     * 
     * @param sourceDatabaseName
     * @param targetDatabaseName
     * @return
     */
    List<ErrorEntry> compareDatabaseSchema(String sourceDatabaseName, String targetDatabaseName) throws SQLException;

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
     * @throws JAXBException
     */
    String cloneDatabaseSchema(String sourceDatabaseName, String targetDatabaseName) throws IllegalOperationException, SQLException, JAXBException;

    /**
     * This method drop database according to specified database name and isCopy
     * flag. Some exception will be thrown, check when implementation.
     * 
     * @param databaseName
     * @return
     * @throws JAXBException
     * @throws SQLException
     */
    boolean dropDatabase(String databaseName) throws JAXBException, SQLException;
}
