package net.madz.crmp.db.core.impl;

import net.madz.crmp.db.core.AbsDatabaseGenerator;
import net.madz.crmp.db.core.AbsSchemaMetaDataParser;
import net.madz.crmp.db.core.DbOperatorFactory;

public class DbOperatorFactoryImpl implements DbOperatorFactory {
	

	@Override
	public AbsSchemaMetaDataParser createSchemaParser(String databaseName, boolean isCopy) {
		return null;
	}

	@Override
	public AbsDatabaseGenerator createDatabaseGenerator() {
		return null;
	}

}
