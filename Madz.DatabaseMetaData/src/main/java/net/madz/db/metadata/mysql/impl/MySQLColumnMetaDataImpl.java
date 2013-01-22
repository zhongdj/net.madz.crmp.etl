package net.madz.db.metadata.mysql.impl;

import net.madz.db.metadata.jdbc.impl.JdbcColumnMetaDataImpl;
import net.madz.db.metadata.mysql.MySQLColumnMetaData;
import net.madz.db.metadata.mysql.MySQLColumnTypeEnum;

public class MySQLColumnMetaDataImpl extends JdbcColumnMetaDataImpl implements MySQLColumnMetaData {

    private final String charSet;
    private final String collation;

    public MySQLColumnMetaDataImpl(MySQLColumnMetaData metaData) {
        super(metaData);
        this.charSet = metaData.getCharacterSet();
        this.collation = metaData.getCollation();
    }

    @Override
    public String getCharacterSet() {
        return this.charSet;
    }

    @Override
    public String getCollation() {
        return this.collation;
    }

    @Override
    public MySQLColumnTypeEnum getColumnType() {
        return null;
    }
}
