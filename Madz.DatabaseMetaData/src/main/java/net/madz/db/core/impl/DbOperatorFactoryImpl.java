package net.madz.db.core.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.madz.db.core.AbsDatabaseGenerator;
import net.madz.db.core.AbsSchemaMetaDataParser;
import net.madz.db.core.SchemaMetaDataComparator;
import net.madz.db.core.DbOperatorFactory;
import net.madz.db.utils.MessageConsts;

// TODO [Jan 22, 2013][barry][Done] Is there any chance to extend this class with subClass?
public final class DbOperatorFactoryImpl implements DbOperatorFactory {

    // TODO [Jan 22, 2013][barry][Done] What's the naming convention of const?
    private static final DbOperatorFactoryImpl INSTANCE = new DbOperatorFactoryImpl();

    private DbOperatorFactoryImpl() {
    }

    public static DbOperatorFactoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public AbsSchemaMetaDataParser createSchemaParser(String databaseName, boolean isCopy) {
        // TODO [Jan 22, 2013][barry][Done] Use modifier final to immutable
        // variables
        final String schemaMetaDataPaser = DbConfigurationManagement.getSchemaMetaDataPaser(databaseName, isCopy);
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
        // TODO [Jan 22, 2013][barry][Done] Use modifier final to immutable
        // variables
        final String databaseGeneratorClass = DbConfigurationManagement.getDatabaseGeneratorClass();
        try {
            Class<?> generatorClassObj = Class.forName(databaseGeneratorClass);
            Constructor<?> constructor = generatorClassObj.getConstructor();
            return (AbsDatabaseGenerator) constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public SchemaMetaDataComparator createDatabaseComparator(String databaseName) {
        if ( null == databaseName || 0 >= databaseName.trim().length() ) {
            throw new IllegalArgumentException(MessageConsts.DATABASE_NAME_SHOULD_NOT_BE_NULL);
        }
        final String databaseComparatorClass = DbConfigurationManagement.getDatabaseComparatorClass(databaseName);
        try {
            Class<?> comparatorClassObj = Class.forName(databaseComparatorClass);
            Constructor<?> constructor = comparatorClassObj.getConstructor();
            return (SchemaMetaDataComparator) constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
