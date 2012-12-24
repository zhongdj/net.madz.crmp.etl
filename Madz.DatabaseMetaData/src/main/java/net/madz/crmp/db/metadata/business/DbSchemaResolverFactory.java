package net.madz.crmp.db.metadata.business;

import java.sql.Connection;

/**
 * This factory generates pair of connection and DBSchemaResolver of different
 * databases.
 * 
 * @author tracy
 * 
 */
public interface DbSchemaResolverFactory {

	/**
	 * @param dbName
	 * @return
	 */
	Connection createConnection(String dbName);

	/**
	 * @param dbName
	 * @return
	 */
	DBSchemaResolver createDBSchemaResolver(String dbName);
}
