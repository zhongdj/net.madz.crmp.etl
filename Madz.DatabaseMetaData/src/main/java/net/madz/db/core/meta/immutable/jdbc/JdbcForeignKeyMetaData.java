package net.madz.db.core.meta.immutable.jdbc;

import net.madz.db.core.meta.immutable.ForeignKeyEntry;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;

public interface JdbcForeignKeyMetaData extends
        ForeignKeyMetaData<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {

    public interface Entry extends ForeignKeyEntry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {
    }
}
