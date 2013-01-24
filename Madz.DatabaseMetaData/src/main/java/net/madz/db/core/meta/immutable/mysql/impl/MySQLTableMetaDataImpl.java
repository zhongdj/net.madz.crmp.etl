package net.madz.db.core.meta.immutable.mysql.impl;

import net.madz.db.core.meta.immutable.impl.TableMetaDataImpl;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLTableTypeEnum;

public final class MySQLTableMetaDataImpl extends
        TableMetaDataImpl<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> implements
        MySQLTableMetaData {

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
