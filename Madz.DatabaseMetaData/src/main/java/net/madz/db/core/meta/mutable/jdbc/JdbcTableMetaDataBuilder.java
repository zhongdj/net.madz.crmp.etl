package net.madz.db.core.meta.mutable.jdbc;

import java.util.Collection;

import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;
import net.madz.db.core.meta.mutable.TableMetaDataBuilder;

public interface JdbcTableMetaDataBuilder
        extends
        JdbcTableMetaData,
        TableMetaDataBuilder<JdbcSchemaMetaDataBuilder, JdbcTableMetaDataBuilder, JdbcColumnMetaDataBuilder, JdbcForeignKeyMetaDataBuilder, JdbcIndexMetaDataBuilder, JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {

    JdbcForeignKeyMetaDataBuilder getForeignKeyBuilder(String fkName);

    Collection<JdbcForeignKeyMetaDataBuilder> getForeignKeyBuilderSet();

    JdbcIndexMetaDataBuilder getIndexBuilder(String indexName);

    JdbcColumnMetaDataBuilder getColumnBuilder(String columnName);
}
