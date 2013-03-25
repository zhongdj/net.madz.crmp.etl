package net.madz.db.core.meta.immutable.jdbc;

import net.madz.db.core.meta.immutable.IndexEntry;
import net.madz.db.core.meta.immutable.IndexMetaData;

public interface JdbcIndexMetaData extends IndexMetaData<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {

    public interface Entry extends IndexEntry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {
    }
}
