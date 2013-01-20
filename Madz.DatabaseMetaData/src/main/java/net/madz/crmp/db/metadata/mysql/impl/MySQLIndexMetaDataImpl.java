package net.madz.crmp.db.metadata.mysql.impl;

import net.madz.crmp.db.metadata.jdbc.impl.JdbcIndexMetaDataImpl;
import net.madz.crmp.db.metadata.mysql.MySQLIndexMetaData;
import net.madz.crmp.db.metadata.mysql.MySqlIndexMethod;

public class MySQLIndexMetaDataImpl extends JdbcIndexMetaDataImpl implements MySQLIndexMetaData {

    private final int subPart;
    private final boolean isNull;
    private final MySqlIndexMethod indexMethod;

    public MySQLIndexMetaDataImpl(MySQLIndexMetaData metaData) {
        super(metaData);
        this.subPart = metaData.getSubPart();
        this.isNull = metaData.isNull();
        this.indexMethod = metaData.getIndexMethod();
    }

    @Override
    public int getSubPart() {
        return this.subPart;
    }

    @Override
    public boolean isNull() {
        return this.isNull;
    }

    @Override
    public MySqlIndexMethod getIndexMethod() {
        return this.indexMethod;
    }
}
