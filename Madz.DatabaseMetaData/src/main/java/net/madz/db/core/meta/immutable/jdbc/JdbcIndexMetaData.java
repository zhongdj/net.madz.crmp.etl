package net.madz.db.core.meta.immutable.jdbc;

import net.madz.db.core.meta.immutable.IndexMetaData;

public interface JdbcIndexMetaData extends IndexMetaData<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {

    public interface Entry extends
            IndexMetaData.Entry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {
    }
}
