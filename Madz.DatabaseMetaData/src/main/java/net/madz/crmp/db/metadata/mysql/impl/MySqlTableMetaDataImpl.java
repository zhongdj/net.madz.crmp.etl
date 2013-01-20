package net.madz.crmp.db.metadata.mysql.impl;

import net.madz.crmp.db.metadata.jdbc.impl.JdbcTableMetaDataImpl;
import net.madz.crmp.db.metadata.mysql.MySqlEngine;
import net.madz.crmp.db.metadata.mysql.MySqlTableMetaData;
import net.madz.crmp.db.metadata.mysql.MySqlTableType;

public class MySqlTableMetaDataImpl extends JdbcTableMetaDataImpl implements MySqlTableMetaData {

    private final MySqlTableType tableType;
    private final MySqlEngine engine;
    private final String characterSet;
    private final String collation;

    public MySqlTableMetaDataImpl(MySqlTableMetaData metaData) {
        super(metaData);
        this.tableType = metaData.getTableType();
        this.engine = metaData.getEngine();
        this.characterSet = metaData.getCharacterSet();
        this.collation = metaData.getCollation();
    }

    @Override
    public MySqlTableType getTableType() {
        return this.tableType;
    }

    @Override
    public MySqlEngine getEngine() {
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
