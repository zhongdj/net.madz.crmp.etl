package net.madz.crmp.db.metadata.business;

import net.madz.crmp.db.metadata.business.impl.DataBaseMetaData;

/**
 * This interface is responsible for cloning the database metadata into mysql database.
 * 
 * @author tracy
 * 
 */
public interface DBSchemaConstructor {
	/**
	 * @param metadata
	 */
	void cloneDbSchema(DataBaseMetaData metadata);
}
