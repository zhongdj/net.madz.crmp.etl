package net.madz.db.core.meta.immutable.jdbc.impl;

import net.madz.db.core.meta.immutable.IndexEntry;
import net.madz.db.core.meta.immutable.impl.ColumnMetaDataImpl;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;

public final class JdbcColumnMetaDataImpl extends
        ColumnMetaDataImpl<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> implements JdbcColumnMetaData {

    private final Integer sqlType;
    private final Integer decimalDigits;

    public JdbcColumnMetaDataImpl(JdbcTableMetaData parent, JdbcColumnMetaData metaData) {
        super(parent, metaData);
        this.sqlType = metaData.getSqlType();
        this.decimalDigits = metaData.getDecimalDigits();
    }

    @Override
    public Integer getSqlType() {
        return this.sqlType;
    }

    public void addUniqueIndexEntry(IndexEntry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> entry) {
        this.uniqueIndexList.add(entry);
    }

    public void addNonUniqueIndexEntry(IndexEntry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> entry) {
        this.nonUniqueIndexList.add(entry);
    }

    public void setPrimaryKey(IndexEntry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> entry) {
        this.primaryKey = entry;
    }

    @Override
    public Integer getDecimalDigits() {
        return this.decimalDigits;
    }
}
