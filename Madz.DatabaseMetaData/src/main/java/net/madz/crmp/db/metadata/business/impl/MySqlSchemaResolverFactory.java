package net.madz.crmp.db.metadata.business.impl;

import java.sql.Connection;

import net.madz.crmp.db.metadata.business.DBSchemaResolver;
import net.madz.crmp.db.metadata.business.DbSchemaResolverFactory;
import net.madz.crmp.db.metadata.connection.ConnectionFactory;

public class MySqlSchemaResolverFactory implements DbSchemaResolverFactory {

	@Override
	public Connection createConnection(String dbName) {
		return ConnectionFactory.createConnection(dbName);
	}

	@Override
	public DBSchemaResolver createDBSchemaResolver(String dbName) {
		// TODO Auto-generated method stub
		return null;
	}

}
