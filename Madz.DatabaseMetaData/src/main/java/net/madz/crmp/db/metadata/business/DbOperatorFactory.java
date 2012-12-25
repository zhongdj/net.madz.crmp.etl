package net.madz.crmp.db.metadata.business;


/**
 * This factory generates DBSchemaResolvers and DBSchemaConstrutors of different
 * databases.
 * 
 * @author tracy
 * 
 */
public interface DbOperatorFactory {

	/**
	 * @param dbName
	 * @return
	 */
	DBSchemaResolver createDBSchemaResolver(String dbName);
	/**
	 * @param dbName
	 * @return
	 */
	DBSchemaConstructor createDBSchemaConstructor(String dbName);
}
