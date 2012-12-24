package net.madz.crmp.db.metadata.business;

import net.madz.crmp.db.metadata.business.impl.DataBaseMetaData;

/**
 * This class is responsible for creating DatabaseMetadata instance of specified
 * database.
 * 
 * @author tracy
 * 
 */
public abstract class AbstractDBSchemaResolver {
	/**
	 * @param dbName
	 * @return
	 */
	public abstract DataBaseMetaData createDBMetadata(String dbName);
}
