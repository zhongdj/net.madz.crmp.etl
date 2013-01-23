package net.madz.db.metadata.mysql.impl;

import java.util.Collection;
import java.util.Map;

import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.impl.JdbcSchemaMetaDataImpl;
import net.madz.db.metadata.mysql.MySQLSchemaMetaData;
import net.madz.db.metadata.mysql.MySQLTableMetaData;

public class MySQLSchemaMetaDataImpl extends JdbcSchemaMetaDataImpl implements
		MySQLSchemaMetaData {

	// TODO [Jan 22, 2013][barry][Done] Use modifier final with immutable fields
	private final String charSet;
	private final String collation;

	public MySQLSchemaMetaDataImpl(DottedPath schemaPath, Map tables,
			String charSet2, String collation2) {
		super(schemaPath, tables);
		this.charSet = charSet2;
		this.collation = collation2;
	}

	@Override
	public String getCharSet() {
		return charSet;
	}

	@Override
	public String getCollation() {
		return collation;
	}

	@Override
	public Collection<MySQLTableMetaData> getTables() {
		return this.getTables();
	}

	@SuppressWarnings("unchecked")
	@Override
	public MySQLTableMetaData getTable(String name) {
		return this.getTable(name);
	}
}
