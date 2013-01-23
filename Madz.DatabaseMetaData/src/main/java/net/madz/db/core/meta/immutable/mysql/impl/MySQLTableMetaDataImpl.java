package net.madz.db.core.meta.immutable.mysql.impl;

import net.madz.db.core.meta.immutable.impl.TableMetaDataImpl;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLTableTypeEnum;

public class MySQLTableMetaDataImpl
		extends
		TableMetaDataImpl<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
		implements MySQLTableMetaData {

	@Override
	public MySQLTableTypeEnum getTableType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MySQLEngineEnum getEngine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCharacterSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCollation() {
		// TODO Auto-generated method stub
		return null;
	}
}
