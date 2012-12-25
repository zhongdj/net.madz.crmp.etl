package net.madz.crmp.db.core.impl;

import net.madz.crmp.db.core.DatabaseSchemaUtils;
import net.madz.crmp.db.core.IllegalOperationException;

public class DatabaseSchemaUtilsImpl implements DatabaseSchemaUtils {

	@Override
	public boolean databaseExists(String databaseName, boolean isCopy) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean compareDatabaseSchema(String sourceDatabaseName,
			String targetDatabaseName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String cloneDatabaseSchema(String sourceDatabaseName,
			String targetDatabaseName) throws IllegalOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean dropDatabase(String databaseName, boolean isCopy) {
		// TODO Auto-generated method stub
		return false;
	}

}
