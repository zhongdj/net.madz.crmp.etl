package net.madz.db.core.meta.mutable.mysql.impl;

import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLIndexMethod;
import net.madz.db.core.meta.mutable.base.impl.IndexMetaDataBuilderImpl;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;

public class MySQLIndexMetaDataBuilderImpl
        extends
        IndexMetaDataBuilderImpl<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLIndexMetaDataBuilder {

    @Override
    public int getSubPart() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isNull() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public MySQLIndexMethod getIndexMethod() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsColumn(MySQLColumnMetaData column) {
        // TODO Auto-generated method stub
        return false;
    }

}
