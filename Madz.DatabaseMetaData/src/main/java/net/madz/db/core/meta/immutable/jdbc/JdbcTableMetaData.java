package net.madz.db.core.meta.immutable.jdbc;

import net.madz.db.core.meta.immutable.TableMetaData;

public interface JdbcTableMetaData
		extends
		TableMetaData<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {

}
