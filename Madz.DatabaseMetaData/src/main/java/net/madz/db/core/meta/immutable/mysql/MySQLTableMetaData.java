package net.madz.db.core.meta.immutable.mysql;

import net.madz.db.core.meta.immutable.TableMetaData;

public interface MySQLTableMetaData
		extends
		TableMetaData<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

}
