package net.madz.db.metadata.jdbc.impl;

import java.util.Collection;
import java.util.Collections;

import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.JdbcColumnMetaData;
import net.madz.db.metadata.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.metadata.jdbc.JdbcIndexMetaData;
import net.madz.db.metadata.jdbc.JdbcIndexMetaData.Entry;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;

public class JdbcColumnMetaDataImpl implements JdbcColumnMetaData {

    protected final DottedPath name;
    protected final JdbcTableMetaData table;
    protected final Integer sqlType;
    protected final String typeName;
    protected final Integer size;
    protected final boolean isNullable;
    protected final boolean isAutoIncremented;
    protected final Integer radix, charOctetLength;
    protected final String remarks, defaultValue;
    protected final JdbcIndexMetaData.Entry primaryKey;
    protected final Collection<JdbcIndexMetaData.Entry> uniqueIndexList;
    protected final Collection<JdbcIndexMetaData.Entry> nonUniqueIndexList;
    protected final Short ordinalPosition;

    // private JdbcColumnMetaData jdbcColumnMetaData;
    public JdbcColumnMetaDataImpl(JdbcColumnMetaData metaData) {
        // this.jdbcColumnMetaData = metaData;
        this.name = metaData.getColumnPath();
        this.table = metaData.getTableMetaData();
        this.sqlType = metaData.getSqlType();
        this.typeName = metaData.getSqlTypeName();
        this.size = metaData.getSize();
        this.radix = metaData.getRadix();
        this.charOctetLength = metaData.getCharOctetLength();
        this.remarks = metaData.getRemarks();
        this.defaultValue = metaData.getDefaultValue();
        this.ordinalPosition = metaData.getOrdinalPosition();
        this.isNullable = metaData.isNullable();
        this.isAutoIncremented = metaData.isAutoIncremented();
        this.primaryKey = metaData.getPrimaryKey();
        this.uniqueIndexList = Collections.unmodifiableCollection(metaData.getUniqueIndexSet());
        this.nonUniqueIndexList = Collections.unmodifiableCollection(metaData.getNonUniqueIndexSet());
    }

    @Override
    public DottedPath getColumnPath() {
        return this.name;
    }

    @Override
    public JdbcTableMetaData getTableMetaData() {
        return this.table;
    }

    @Override
    public String getColumnName() {
        return this.name.getName();
    }

    @Override
    public Integer getSqlType() {
        return this.sqlType;
    }

    @Override
    public String getSqlTypeName() {
        return this.typeName;
    }

    @Override
    public boolean isNullable() {
        return this.isNullable;
    }

    @Override
    public Entry getPrimaryKey() {
        return this.primaryKey;
    }

    @Override
    public boolean isMemberOfPrimaryKey() {
        JdbcIndexMetaData primaryKey = this.table.getPrimaryKey();
        if ( primaryKey.containsColumn(this) ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isMemberOfIndex() {
        Collection<? extends JdbcIndexMetaData> indexSet = this.table.getIndexSet();
        for ( JdbcIndexMetaData index : indexSet ) {
            if ( index.containsColumn(this) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMemberOfForeignKey(JdbcForeignKeyMetaData fk) {
        Collection<? extends JdbcForeignKeyMetaData> foreignKeySet = this.table.getForeignKeySet();
        for ( JdbcForeignKeyMetaData fkMetaData : foreignKeySet ) {
            if ( fkMetaData.getEntrySet().contains(this) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<? extends Entry> getUniqueIndexSet() {
        return this.uniqueIndexList;
    }

    @Override
    public boolean isMemberOfUniqueIndex() {
        return this.uniqueIndexList.contains(this);
    }

    @Override
    public Collection<? extends Entry> getNonUniqueIndexSet() {
        return this.nonUniqueIndexList;
    }

    @Override
    public Integer getRadix() {
        return this.radix;
    }

    @Override
    public boolean isAutoIncremented() {
        return this.isAutoIncremented;
    }

    @Override
    public Integer getSize() {
        return this.size;
    }

    @Override
    public Short getOrdinalPosition() {
        return this.ordinalPosition;
    }

    @Override
    public boolean hasDefaultValue() {
        return null != defaultValue;
    }

    @Override
    public Integer getCharOctetLength() {
        return this.charOctetLength;
    }

    @Override
    public String getRemarks() {
        return this.remarks;
    }

    @Override
    public String getDefaultValue() {
        return this.defaultValue;
    }
}
