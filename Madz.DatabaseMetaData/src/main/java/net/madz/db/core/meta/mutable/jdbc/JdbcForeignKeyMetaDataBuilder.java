package net.madz.db.core.meta.mutable.jdbc;

import java.sql.SQLException;

import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.impl.enums.AccessForeignKeyDbMetaDataEnum;
import net.madz.db.core.meta.immutable.impl.enums.ImportKeyDbMetaDataEnum;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;
import net.madz.db.core.meta.mutable.ForeignKeyMetaDataBuilder;

public interface JdbcForeignKeyMetaDataBuilder
        extends
        JdbcForeignKeyMetaData,
        ForeignKeyMetaDataBuilder<JdbcSchemaMetaDataBuilder, JdbcTableMetaDataBuilder, JdbcColumnMetaDataBuilder, JdbcForeignKeyMetaDataBuilder, JdbcIndexMetaDataBuilder, JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {

    void addEntry(MetaDataResultSet<AccessForeignKeyDbMetaDataEnum> importedKeys) throws SQLException;
}
