package net.madz.db.core.meta.immutable.mysql.impl;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.impl.SchemaMetaDataImpl;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;

public final class MySQLSchemaMetaDataImpl extends
		SchemaMetaDataImpl<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> implements
		MySQLSchemaMetaData {

	@Override
	public DottedPath getSchemaPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCharSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCollation() {
		// TODO Auto-generated method stub
		return null;
	}

}
