package net.madz.db.core.meta.immutable.mysql.impl;

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
    private String collation;
    private String columnType;
    private int characterMaximumLength;
    private int numericPrecision;
    private int numericScale;
    private String collationName;
    private String columnKey;
    private String extra;
    private String columnComment;

    public MySQLColumnMetaDataImpl(MySQLColumnMetaData metaData) {
        super(metaData);
        this.characterSet = metaData.getCharacterSet();
        this.collation = metaData.getCollation();
        this.columnType = metaData.getColumnType();
        this.characterMaximumLength = metaData.getCharacterMaximumLength();
        this.numericPrecision = metaData.getNumericPrecision();
        this.numericScale = metaData.getNumericScale();
        this.collationName = metaData.getCollationName();
        this.columnKey = metaData.getColumnKey();
        this.extra = metaData.getExtra();
        this.columnComment = metaData.getColumnComment();
    }

    @Override
    public String getCharacterSet() {
        return characterSet;
    }

    @Override
    public String getCollation() {
        return collation;
    }

    @Override
    public String getColumnType() {
        return columnType;
    }

    @Override
    public String getColumnComment() {
        return this.columnComment;
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
    public int getNumericScale() {
        return this.numericScale;
    }

    @Override
    public int getNumericPrecision() {
        return this.numericPrecision;
    }

    @Override
    public int getCharacterMaximumLength() {
        return this.characterMaximumLength;
    }
}
