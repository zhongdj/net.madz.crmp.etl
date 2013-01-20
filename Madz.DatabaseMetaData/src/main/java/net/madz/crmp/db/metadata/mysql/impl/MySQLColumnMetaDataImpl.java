package net.madz.crmp.db.metadata.mysql.impl;

import net.madz.crmp.db.metadata.jdbc.impl.JdbcColumnMetaDataImpl;
import net.madz.crmp.db.metadata.mysql.MySqlColumnMetaData;

public class MySQLColumnMetaDataImpl extends JdbcColumnMetaDataImpl implements MySqlColumnMetaData {

    private final String charSet;
    private final String collation;

    public MySQLColumnMetaDataImpl(MySqlColumnMetaData metaData) {
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
}
