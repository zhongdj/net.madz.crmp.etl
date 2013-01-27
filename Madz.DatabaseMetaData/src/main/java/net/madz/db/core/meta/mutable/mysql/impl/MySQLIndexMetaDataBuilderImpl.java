package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLIndexMethod;
import net.madz.db.core.meta.mutable.impl.BaseIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;

public class MySQLIndexMetaDataBuilderImpl
        extends
        BaseIndexMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLIndexMetaDataBuilder {

    public MySQLIndexMetaDataBuilderImpl(MySQLTableMetaDataBuilderImpl mySQLTableMetaDataBuilderImpl, DottedPath append) {
        // TODO Auto-generated constructor stub
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
    public MySQLIndexMetaData getMetaData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLIndexMetaDataBuilder build(Connection conn) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

}
