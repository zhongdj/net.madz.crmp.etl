package net.madz.db.core.meta.immutable.mysql.impl;

import net.madz.db.core.meta.immutable.impl.IndexMetaDataImpl;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLIndexMethod;

public final class MySQLIndexMetaDataImpl extends
        IndexMetaDataImpl<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> implements
        MySQLIndexMetaData {

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
