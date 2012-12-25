package net.madz.crmp.db.core;

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
     * @param isCopy
     * @return
     */
    AbsSchemaMetaDataParser createSchemaParser(String dbName, boolean isCopy);

    AbsDatabaseGenerator createDatabaseGenerator();
}
