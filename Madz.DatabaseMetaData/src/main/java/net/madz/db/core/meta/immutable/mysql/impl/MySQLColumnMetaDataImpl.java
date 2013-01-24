package net.madz.db.core.meta.immutable.mysql.impl;

import net.madz.db.core.meta.immutable.impl.ColumnMetaDataImpl;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLColumnTypeEnum;

public final class MySQLColumnMetaDataImpl extends
        ColumnMetaDataImpl<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> implements
        MySQLColumnMetaData {

    private String characterSet;
    private String collation;
    private MySQLColumnTypeEnum columnType;

    public MySQLColumnMetaDataImpl(MySQLColumnMetaData metaData) {
        super(metaData);
        this.characterSet = metaData.getCharacterSet();
        this.collation = metaData.getCollation();
        this.columnType = metaData.getColumnType();
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
    public MySQLColumnTypeEnum getColumnType() {
        return columnType;
    }
}
