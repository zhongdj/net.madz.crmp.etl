package net.madz.db.core;

/**
 * This factory generates DBSchemaResolvers and DBSchemaConstrutors of different
 * databases.
 * 
 * @author tracy
 * 
 */
public interface DbOperatorFactory {

    /**
     * @param databaseName
     * @param isCopy
     * @return
     */
    AbsSchemaMetaDataParser createSchemaParser(String databaseName, boolean isCopy);

    AbsDatabaseGenerator createDatabaseGenerator(String databaseName);
}
