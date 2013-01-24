package net.madz.db.core.meta.mutable.jdbc;

import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;
import net.madz.db.core.meta.mutable.SchemaMetaDataBuilder;

public interface JdbcSchemaMetaDataBuilder
        extends
        JdbcSchemaMetaData, 
        SchemaMetaDataBuilder<JdbcSchemaMetaDataBuilder, JdbcTableMetaDataBuilder, JdbcColumnMetaDataBuilder, JdbcForeignKeyMetaDataBuilder, JdbcIndexMetaDataBuilder, JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {
}
