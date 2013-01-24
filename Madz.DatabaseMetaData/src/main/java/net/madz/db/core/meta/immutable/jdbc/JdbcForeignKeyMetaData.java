package net.madz.db.core.meta.immutable.jdbc;

import net.madz.db.core.meta.immutable.ForeignKeyMetaData;

public interface JdbcForeignKeyMetaData extends
        ForeignKeyMetaData<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {

    public interface Entry extends
            ForeignKeyMetaData.Entry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {
    }
}
