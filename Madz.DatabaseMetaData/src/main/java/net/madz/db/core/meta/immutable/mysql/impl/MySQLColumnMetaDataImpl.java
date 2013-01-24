package net.madz.db.core.meta.immutable.mysql.impl;

import net.madz.db.core.meta.immutable.impl.ColumnMetaDataImpl;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLColumnTypeEnum;

public class MySQLColumnMetaDataImpl extends
        ColumnMetaDataImpl<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> implements
        MySQLColumnMetaData {

    public MySQLColumnMetaDataImpl(MySQLColumnMetaData metaData) {
        super(metaData);
    }

    @Override
    public String getCharacterSet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCollation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLColumnTypeEnum getColumnType() {
        // TODO Auto-generated method stub
        return null;
    }
}
