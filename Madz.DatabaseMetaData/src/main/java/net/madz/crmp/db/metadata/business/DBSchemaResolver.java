package net.madz.crmp.db.metadata.business;

import net.madz.crmp.db.metadata.business.impl.DataBaseMetaData;

/**
 * This class is responsible for creating DatabaseMetadata instance of specified
 * database.
 * 
 * @author tracy
 * 
 */
public interface DBSchemaResolver {
	/**
	 * @param dbName
	 * @return
	 */
	DataBaseMetaData createDBMetadata(String dbName);
}
