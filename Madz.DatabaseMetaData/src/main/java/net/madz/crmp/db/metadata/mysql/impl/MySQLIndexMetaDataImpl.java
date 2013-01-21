package net.madz.crmp.db.metadata.mysql.impl;

import net.madz.crmp.db.metadata.jdbc.impl.JdbcIndexMetaDataImpl;
import net.madz.crmp.db.metadata.mysql.MySQLIndexMetaData;
import net.madz.crmp.db.metadata.mysql.MySQLIndexMethod;

public class MySQLIndexMetaDataImpl extends JdbcIndexMetaDataImpl implements MySQLIndexMetaData {

    private final int subPart;
    private final boolean isNull;
    private final MySQLIndexMethod indexMethod;

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
    public MySQLIndexMethod getIndexMethod() {
        return this.indexMethod;
    }
}
