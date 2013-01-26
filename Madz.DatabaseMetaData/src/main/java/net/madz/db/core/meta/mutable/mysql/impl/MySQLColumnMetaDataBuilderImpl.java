package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.IndexMetaData.Entry;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLColumnTypeEnum;
import net.madz.db.core.meta.mutable.impl.BaseColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;

public class MySQLColumnMetaDataBuilderImpl
        extends
        BaseColumnMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLColumnMetaDataBuilder {

    public MySQLColumnMetaDataBuilderImpl(DottedPath name) {
        super(name);
    }

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
    public DottedPath getColumnPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLTableMetaData getTableMetaData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getColumnName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getSqlType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSqlTypeName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getSize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getRadix() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRemarks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasDefaultValue() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getDefaultValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getCharOctetLength() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Short getOrdinalPosition() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isNullable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAutoIncremented() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> getPrimaryKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isMemberOfPrimaryKey() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMemberOfIndex() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMemberOfForeignKey(MySQLForeignKeyMetaData fk) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection<Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> getUniqueIndexSet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isMemberOfUniqueIndex() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection<Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> getNonUniqueIndexSet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLColumnMetaData getMetaData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLColumnMetaDataBuilder appendNonUniqueIndexEntry(
            Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLColumnMetaDataBuilder appendUniqueIndexEntry(
            Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLColumnMetaDataBuilder build(Connection conn) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
