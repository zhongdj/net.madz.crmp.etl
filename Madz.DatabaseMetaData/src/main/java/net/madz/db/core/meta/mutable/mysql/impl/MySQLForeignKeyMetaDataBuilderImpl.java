package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.type.CascadeRule;
import net.madz.db.core.meta.immutable.type.KeyDeferrability;
import net.madz.db.core.meta.mutable.impl.BaseForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;

public class MySQLForeignKeyMetaDataBuilderImpl
extends BaseForeignKeyMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
implements MySQLForeignKeyMetaDataBuilder {

    public MySQLForeignKeyMetaDataBuilderImpl(MySQLTableMetaDataBuilder table) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public MySQLForeignKeyMetaDataBuilder build(Connection conn) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getForeignKeyName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLIndexMetaData getForeignKeyIndex() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLTableMetaData getForeignKeyTable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLIndexMetaData getPrimaryKeyIndex() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLTableMetaData getPrimaryKeyTable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CascadeRule getDeleteCascadeRule() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CascadeRule getUpdateCascadeRule() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyDeferrability getKeyDeferrability() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<net.madz.db.core.meta.immutable.ForeignKeyMetaData.Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> getEntrySet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer size() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addEntry(
            net.madz.db.core.meta.immutable.ForeignKeyMetaData.Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public MySQLForeignKeyMetaData getMetaData() {
        // TODO Auto-generated method stub
        return null;
    }

    
}

