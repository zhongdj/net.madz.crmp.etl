package net.madz.db.core.meta.mutable.mysql.impl;

import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLColumnTypeEnum;
import net.madz.db.core.meta.mutable.base.impl.ColumnMetaDataBuilderImpl;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;

public class MySQLColumnMetaDataBuilderImpl
        extends
        ColumnMetaDataBuilderImpl<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLColumnMetaDataBuilder {

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

    @Override
    public boolean isMemberOfForeignKey(MySQLForeignKeyMetaData fk) {
        // TODO Auto-generated method stub
        return false;
    }
}
