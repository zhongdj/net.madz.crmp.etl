package net.madz.db.core.meta.immutable.mysql.impl;

import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData.Entry;
import net.madz.db.core.meta.immutable.impl.ColumnMetaDataImpl;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;

public final class MySQLColumnMetaDataImpl extends
        ColumnMetaDataImpl<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> implements
        MySQLColumnMetaData {

    private String characterSet;
    private String columnType;
    private long characterMaximumLength;
    private Integer numericPrecision;
    private Integer numericScale;
    private String collationName;
    private String columnKey;
    private String extra;

    public MySQLColumnMetaDataImpl(MySQLTableMetaData parent, MySQLColumnMetaData metaData) {
        super(parent, metaData);
        this.characterSet = metaData.getCharacterSet();
        this.columnType = metaData.getColumnType();
        this.characterMaximumLength = metaData.getCharacterMaximumLength();
        this.numericPrecision = metaData.getNumericPrecision();
        this.numericScale = metaData.getNumericScale();
        this.collationName = metaData.getCollationName();
        this.columnKey = metaData.getColumnKey();
        this.extra = metaData.getExtra();
    }

    @Override
    public String getCharacterSet() {
        return characterSet;
    }

    @Override
    public String getColumnType() {
        return columnType;
    }

    @Override
    public String getExtra() {
        return this.extra;
    }

    @Override
    public String getColumnKey() {
        return this.columnKey;
    }

    @Override
    public String getCollationName() {
        return this.collationName;
    }

    @Override
    public Integer getNumericScale() {
        return this.numericScale;
    }

    @Override
    public Integer getNumericPrecision() {
        return this.numericPrecision;
    }

    @Override
    public long getCharacterMaximumLength() {
        return this.characterMaximumLength;
    }

    public void addForeignKey(ForeignKeyMetaData.Entry entry) {
        this.fkList.add(entry);
    }

    public void addUniqueIndexEntry(Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry) {
        this.uniqueIndexList.add(entry);
    }

    public void addNonUniqueIndexEntry(Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry) {
        this.nonUniqueIndexList.add(entry);
    }

    public void setPrimaryKey(Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry) {
        this.primaryKey = entry;
    }
}
