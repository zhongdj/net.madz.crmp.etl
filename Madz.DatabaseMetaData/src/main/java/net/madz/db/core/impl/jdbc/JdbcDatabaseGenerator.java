package net.madz.db.core.impl.jdbc;

import java.sql.Connection;

import net.madz.db.core.AbsDatabaseGenerator;
import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;

public class JdbcDatabaseGenerator extends AbsDatabaseGenerator {
	// TODO [Jan 22, 2013][barry] Reconsider resource lifecycle
	public JdbcDatabaseGenerator(Connection conn) {
		super(conn);
	}

	@Override
	public String generateDatabase(JdbcSchemaMetaData metadata,
			String targetDatabaseName) {

		System.out.println("to be generated" + metadata);
		return null;
	}
}
