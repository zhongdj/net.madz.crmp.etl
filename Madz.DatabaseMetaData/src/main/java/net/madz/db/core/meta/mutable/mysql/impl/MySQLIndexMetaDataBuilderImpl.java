package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLIndexMethod;
import net.madz.db.core.meta.immutable.types.IndexType;
import net.madz.db.core.meta.immutable.types.KeyType;
import net.madz.db.core.meta.immutable.types.SortDirection;
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

    public MySQLIndexMetaDataBuilderImpl(MySQLTableMetaDataBuilder tableBuilder, DottedPath indexPath) {
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
    public String getIndexName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isUnique() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public KeyType getKeyType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IndexType getIndexType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SortDirection getSortDirection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getCardinality() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getPageCount() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsColumn(MySQLColumnMetaData column) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection<IndexMetaData.Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> getEntrySet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLTableMetaData getTable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addEntry(IndexMetaData.Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry) {
        // TODO Auto-generated method stub
        
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

    public void setPrimaryKey() {
        // TODO Auto-generated method stub
        
    }
    
}
