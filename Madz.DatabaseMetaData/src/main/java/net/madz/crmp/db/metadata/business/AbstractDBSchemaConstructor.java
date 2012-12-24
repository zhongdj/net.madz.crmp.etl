package net.madz.crmp.db.metadata.business;

import net.madz.crmp.db.metadata.business.impl.DataBaseMetaData;

/**
 * This class responsible for cloning the database metadata into mysql database.
 * 
 * @author tracy
 * 
 */
public abstract class AbstractDBSchemaConstructor {
	/**
	 * @param metadata
	 */
	public abstract void coloneDbSchema(DataBaseMetaData metadata);
}
