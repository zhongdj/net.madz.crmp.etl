package net.madz.crmp.db.core.impl;

import java.lang.reflect.Constructor;

import net.madz.crmp.db.core.AbsDatabaseGenerator;
import net.madz.crmp.db.core.AbsSchemaMetaDataParser;
import net.madz.crmp.db.core.DbOperatorFactory;

public class DbOperatorFactoryImpl implements DbOperatorFactory {

    private static final DbOperatorFactoryImpl instance = new DbOperatorFactoryImpl();

    private DbOperatorFactoryImpl() {
    }

    public static DbOperatorFactoryImpl getInstance() {
        return instance;
    }

    @Override
    public AbsSchemaMetaDataParser createSchemaParser(String databaseName, boolean isCopy) {
        String schemaMetaDataPaser = DbConfigurationManagement.getSchemaMetaDataPaser(databaseName, isCopy);
        try {
            Class<?> parserClassObj = Class.forName(schemaMetaDataPaser);
            Constructor<?> constructor = parserClassObj.getConstructor(String.class, java.sql.Connection.class);
            return (AbsSchemaMetaDataParser) constructor.newInstance(databaseName, DbConfigurationManagement.createConnection(databaseName, isCopy));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public AbsDatabaseGenerator createDatabaseGenerator(String targetDatabaseName) {
        String databaseGeneratorClass = DbConfigurationManagement.getDatabaseGeneratorClass();
        try {
            Class<?> generatorClassObj = Class.forName(databaseGeneratorClass);
            Constructor<?> constructor = generatorClassObj.getConstructor(java.sql.Connection.class);
            return (AbsDatabaseGenerator) constructor.newInstance(DbConfigurationManagement.createConnection(targetDatabaseName, true));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
