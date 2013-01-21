package net.madz.crmp.db.metadata.mysql.impl;

import net.madz.crmp.db.metadata.jdbc.impl.JdbcTableMetaDataImpl;
import net.madz.crmp.db.metadata.mysql.MySQLEngineEnum;
import net.madz.crmp.db.metadata.mysql.MySQLTableMetaData;
import net.madz.crmp.db.metadata.mysql.MySQLTableTypeEnum;

public class MySQLTableMetaDataImpl extends JdbcTableMetaDataImpl implements MySQLTableMetaData {

    private final MySQLTableTypeEnum tableType;
    private final MySQLEngineEnum engine;
    private final String characterSet;
    private final String collation;

    public MySQLTableMetaDataImpl(MySQLTableMetaData metaData) {
        super(metaData);
        this.tableType = metaData.getTableType();
        this.engine = metaData.getEngine();
        this.characterSet = metaData.getCharacterSet();
        this.collation = metaData.getCollation();
    }

    @Override
    public MySQLTableTypeEnum getTableType() {
        return this.tableType;
    }

    @Override
    public MySQLEngineEnum getEngine() {
        return this.engine;
    }

    @Override
    public String getCharacterSet() {
        return this.characterSet;
    }

    @Override
    public String getCollation() {
        return this.collation;
    }
}
