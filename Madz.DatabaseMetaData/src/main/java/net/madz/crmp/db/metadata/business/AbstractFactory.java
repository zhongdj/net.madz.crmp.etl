package net.madz.crmp.db.metadata.business;

import net.madz.crmp.db.metadata.connection.Connection;

/**
 * This factory generates pair of connection and DBSchemaResolver of different
 * databases.
 * 
 * @author tracy
 * 
 */
public abstract class AbstractFactory {

	/**
	 * @param dbName
	 * @return
	 */
	public abstract Connection createConnection(String dbName);

	/**
	 * @param dbName
	 * @return
	 */
	public abstract AbstractDBSchemaResolver createDBSchemaResolver(
			String dbName);
}
