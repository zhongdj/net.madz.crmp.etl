package net.madz.crmp.db.metadata.utils;

/**
 * This class is the facade of this module. It provides checking,comparing,
 * cloning, and dropping database metadata features.
 * 
 * @author tracy
 * 
 */
public interface DbMetadataUtils {
	/**
	 * This method check whether the specified database exists.
	 * 
	 * @param dbName
	 * @return
	 */
	boolean isDbExists(String dbName);

	/**
	 * This method compares schema of two databases. True will be returned when
	 * only database names different.
	 * 
	 * @param firstDbName
	 * @param secondDBName
	 * @return
	 */
	boolean compareDbsMetadata(String firstDbName, String secondDBName);

	/**
	 * This method will clone database schema of source database to target
	 * database. If target database already exists, DbExistsException will be
	 * thrown.
	 * 
	 * @param sourceDbName
	 * @param targetDbName
	 * @throws DbExistsException
	 */
	void cloneDbMetadata(String sourceDbName, String targetDbName)
			throws DbExistsException;

	/**
	 * This method drop database according to specified database name. Some
	 * exception will be thrown, check when implementation.
	 * 
	 * @param dbName
	 * @return
	 */
	boolean dropDb(String dbName);
}
