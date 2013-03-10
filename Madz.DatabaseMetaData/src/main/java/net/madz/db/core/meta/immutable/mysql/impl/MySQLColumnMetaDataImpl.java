package net.madz.db.core.meta.immutable.mysql.impl;

import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData.Entry;
import net.madz.db.core.meta.immutable.impl.ColumnMetaDataImpl;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;

public final class MySQLColumnMetaDataImpl extends
        ColumnMetaDataImpl<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> implements
        MySQLColumnMetaData {

    private String characterSet;
    private String columnType;
    private long characterMaximumLength;
    private Integer numericPrecision;
    private Integer numericScale;
    private String collationName;
    private String columnKey;
    private String extra;
    private boolean isUnsigned;
    private boolean isZeroFill;
    private boolean isCollationWithBin;
    private LinkedList<String> typeValues = new LinkedList<String>();

    public MySQLColumnMetaDataImpl(MySQLTableMetaData parent, MySQLColumnMetaData metaData) {
        super(parent, metaData);
        this.characterSet = metaData.getCharacterSet();
        this.columnType = metaData.getColumnType();
        this.characterMaximumLength = metaData.getCharacterMaximumLength();
        this.numericPrecision = metaData.getNumericPrecision();
        this.numericScale = metaData.getNumericScale();
        this.collationName = metaData.getCollationName();
        this.columnKey = metaData.getColumnKey();
        this.extra = metaData.getExtra();
        this.isUnsigned = metaData.isUnsigned();
        this.isZeroFill = metaData.isZeroFill();
        this.isCollationWithBin = metaData.isCollationWithBin();
        for ( String value : metaData.getTypeValues() ) {
            this.typeValues.add(value);
        }
    }

    @Override
    public String getCharacterSet() {
        return characterSet;
    }

    @Override
    public String getColumnType() {
        return columnType;
    }

    @Override
    public String getExtra() {
        return this.extra;
    }

    @Override
    public String getColumnKey() {
        return this.columnKey;
    }

    @Override
    public String getCollationName() {
        return this.collationName;
    }

    @Override
    public Integer getNumericScale() {
        return this.numericScale;
    }

    @Override
    public Integer getNumericPrecision() {
        return this.numericPrecision;
    }

    @Override
    public long getCharacterMaximumLength() {
        return this.characterMaximumLength;
    }

    public void addForeignKey(ForeignKeyMetaData.Entry entry) {
        this.fkList.add(entry);
    }

    public void addUniqueIndexEntry(Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry) {
        this.uniqueIndexList.add(entry);
    }

    public void addNonUniqueIndexEntry(Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry) {
        this.nonUniqueIndexList.add(entry);
    }

    public void setPrimaryKey(Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry) {
        this.primaryKey = entry;
    }

    @Override
    public boolean isUnsigned() {
        return this.isUnsigned;
    }

    @Override
    public boolean isZeroFill() {
        return this.isZeroFill;
    }

    @Override
    public boolean isCollationWithBin() {
        return this.isCollationWithBin;
    }

    @Override
    public List<String> getTypeValues() {
        return this.typeValues;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (int) ( characterMaximumLength ^ ( characterMaximumLength >>> 32 ) );
        result = prime * result + ( ( characterSet == null ) ? 0 : characterSet.hashCode() );
        result = prime * result + ( ( collationName == null ) ? 0 : collationName.hashCode() );
        result = prime * result + ( ( columnKey == null ) ? 0 : columnKey.hashCode() );
        result = prime * result + ( ( columnType == null ) ? 0 : columnType.hashCode() );
        result = prime * result + ( ( extra == null ) ? 0 : extra.hashCode() );
        result = prime * result + ( isCollationWithBin ? 1231 : 1237 );
        result = prime * result + ( isUnsigned ? 1231 : 1237 );
        result = prime * result + ( isZeroFill ? 1231 : 1237 );
        result = prime * result + ( ( numericPrecision == null ) ? 0 : numericPrecision.hashCode() );
        result = prime * result + ( ( numericScale == null ) ? 0 : numericScale.hashCode() );
        result = prime * result + ( ( typeValues == null ) ? 0 : typeValues.hashCode() );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( !super.equals(obj) ) return false;
        if ( getClass() != obj.getClass() ) return false;
        MySQLColumnMetaDataImpl other = (MySQLColumnMetaDataImpl) obj;
        if ( characterMaximumLength != other.characterMaximumLength ) return false;
        if ( characterSet == null ) {
            if ( other.characterSet != null ) return false;
        } else if ( !characterSet.equals(other.characterSet) ) return false;
        if ( collationName == null ) {
            if ( other.collationName != null ) return false;
        } else if ( !collationName.equals(other.collationName) ) return false;
        if ( columnKey == null ) {
            if ( other.columnKey != null ) return false;
        } else if ( !columnKey.equals(other.columnKey) ) return false;
        if ( columnType == null ) {
            if ( other.columnType != null ) return false;
        } else if ( !columnType.equals(other.columnType) ) return false;
        if ( extra == null ) {
            if ( other.extra != null ) return false;
        } else if ( !extra.equals(other.extra) ) return false;
        if ( isCollationWithBin != other.isCollationWithBin ) return false;
        if ( isUnsigned != other.isUnsigned ) return false;
        if ( isZeroFill != other.isZeroFill ) return false;
        if ( numericPrecision == null ) {
            if ( other.numericPrecision != null ) return false;
        } else if ( !numericPrecision.equals(other.numericPrecision) ) return false;
        if ( numericScale == null ) {
            if ( other.numericScale != null ) return false;
        } else if ( !numericScale.equals(other.numericScale) ) return false;
        if ( typeValues == null ) {
            if ( other.typeValues != null ) return false;
        } else if ( !typeValues.equals(other.typeValues) ) return false;
        return true;
    }
}
