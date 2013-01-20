package net.madz.crmp.db.metadata.jdbc.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.madz.crmp.db.metadata.DottedPath;
import net.madz.crmp.db.metadata.jdbc.JdbcColumnMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcForeignKeyMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcIndexMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcIndexMetaData.Entry;
import net.madz.crmp.db.metadata.jdbc.JdbcTableMetaData;

public class JdbcColumnMetaDataImpl implements JdbcColumnMetaData {

    private final DottedPath name;
    private final JdbcTableMetaData table;
    private final Integer sqlType;
    private final String typeName;
    private final Integer size;
    private final boolean isNullable;
    private final boolean isAutoIncremented;
    private final Integer radix, charOctetLength;
    private final String remarks, defaultValue;
    private final JdbcIndexMetaData.Entry primaryKey;
    private final Collection<JdbcIndexMetaData.Entry> uniqueIndexList;
    private final Collection<JdbcIndexMetaData.Entry> nonUniqueIndexList;
    private final Short ordinalPosition;

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
