package net.madz.db.core.meta.immutable.jdbc;

import net.madz.db.core.meta.immutable.ColumnMetaData;

public interface JdbcColumnMetaData extends
        ColumnMetaData<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {

    /** Sql Type from java.sql.Types */
    Integer getSqlType();
    
    /**/
    /** Number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable */
    Integer getDecimalDigits();
}
