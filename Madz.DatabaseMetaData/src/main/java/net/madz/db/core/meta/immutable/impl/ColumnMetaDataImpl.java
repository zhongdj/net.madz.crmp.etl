package net.madz.db.core.meta.immutable.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyEntry;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexEntry;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;

public abstract class ColumnMetaDataImpl<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements ColumnMetaData<SMD, TMD, CMD, FMD, IMD> {

    protected final DottedPath name;
    // For table: Initiated in subclass
    protected TMD table;
    protected final String typeName;
    protected final Integer size;
    protected final boolean isNullable;
    protected final boolean isAutoIncremented;
    protected final Integer radix;
    protected final long charOctetLength;
    protected final String remarks, defaultValue;
    protected IndexEntry<SMD, TMD, CMD, FMD, IMD> primaryKey;
    protected final Collection<IndexEntry<SMD, TMD, CMD, FMD, IMD>> uniqueIndexList = new LinkedList<IndexEntry<SMD, TMD, CMD, FMD, IMD>>();
    protected final Collection<IndexEntry<SMD, TMD, CMD, FMD, IMD>> nonUniqueIndexList = new LinkedList<IndexEntry<SMD, TMD, CMD, FMD, IMD>>();;
    protected final Short ordinalPosition;
    protected final List<ForeignKeyEntry<SMD, TMD, CMD, FMD, IMD>> fkList = new LinkedList<ForeignKeyEntry<SMD, TMD, CMD, FMD, IMD>>();

    public ColumnMetaDataImpl(TMD parent, CMD metaData) {
        this.table = parent;
        this.name = metaData.getColumnPath();
        this.typeName = metaData.getSqlTypeName();
        this.size = metaData.getSize();
        this.radix = metaData.getRadix();
        this.charOctetLength = metaData.getCharOctetLength();
        this.remarks = metaData.getRemarks();
        this.defaultValue = metaData.getDefaultValue();
        this.ordinalPosition = metaData.getOrdinalPosition();
        this.isNullable = metaData.isNullable();
        this.isAutoIncremented = metaData.isAutoIncremented();
    }

    void setTable(TMD table) {
        if ( this.table != null ) this.table.getColumns().remove(this);
        this.table = table;
        if ( this.table != null ) this.table.getColumns().add((CMD) this);
    }

    @Override
    public DottedPath getColumnPath() {
        return this.name;
    }

    @Override
    public TMD getTableMetaData() {
        return this.table;
    }

    @Override
    public String getColumnName() {
        return this.name.getName();
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
    public IndexEntry<SMD, TMD, CMD, FMD, IMD> getPrimaryKey() {
        return this.primaryKey;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isMemberOfPrimaryKey() {
        return null != this.primaryKey;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isMemberOfIndex() {
        return isMemberOfPrimaryKey() || isMemberOfUniqueIndex() || this.nonUniqueIndexList.size() > 0;
    }

    @Override
    public boolean isMemberOfForeignKey(FMD fk) {
        Collection<FMD> foreignKeySet = this.table.getForeignKeySet();
        for ( FMD fkMetaData : foreignKeySet ) {
            if ( fkMetaData.getEntrySet().contains(this) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<IndexEntry<SMD, TMD, CMD, FMD, IMD>> getUniqueIndexSet() {
        return this.uniqueIndexList;
    }

    @Override
    public boolean isMemberOfUniqueIndex() {
        return ( this.uniqueIndexList.size() > 0 );
    }

    @Override
    public Collection<IndexEntry<SMD, TMD, CMD, FMD, IMD>> getNonUniqueIndexSet() {
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
        return null != defaultValue && 0 < defaultValue.length();
    }

    @Override
    public Long getCharOctetLength() {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( defaultValue == null ) ? 0 : defaultValue.hashCode() );
        result = prime * result + (int) ( charOctetLength ^ ( charOctetLength >>> 32 ) );
        result = prime * result + ( isAutoIncremented ? 1231 : 1237 );
        result = prime * result + ( isNullable ? 1231 : 1237 );
        result = prime * result + ( ( ordinalPosition == null ) ? 0 : ordinalPosition.hashCode() );
        result = prime * result + ( ( radix == null ) ? 0 : radix.hashCode() );
        result = prime * result + ( ( remarks == null ) ? 0 : remarks.hashCode() );
        result = prime * result + ( ( size == null ) ? 0 : size.hashCode() );
        result = prime * result + ( ( typeName == null ) ? 0 : typeName.hashCode() );
        result = prime * result + ( ( table.getTablePath() == null ) ? 0 : table.getTablePath().hashCode() );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        ColumnMetaDataImpl other = (ColumnMetaDataImpl) obj;
        if ( name == null ) {
            if ( other.name != null ) return false;
        } else if ( !name.equals(other.name) ) return false;
        if ( defaultValue == null ) {
            if ( other.defaultValue != null ) return false;
        } else if ( !defaultValue.equals(other.defaultValue) ) return false;
        if ( charOctetLength != other.charOctetLength ) return false;
        if ( isAutoIncremented != other.isAutoIncremented ) return false;
        if ( isNullable != other.isNullable ) return false;
        if ( ordinalPosition == null ) {
            if ( other.ordinalPosition != null ) return false;
        } else if ( !ordinalPosition.equals(other.ordinalPosition) ) return false;
        if ( radix == null ) {
            if ( other.radix != null ) return false;
        } else if ( !radix.equals(other.radix) ) return false;
        if ( remarks == null ) {
            if ( other.remarks != null ) return false;
        } else if ( !remarks.equals(other.remarks) ) return false;
        if ( size == null ) {
            if ( other.size != null ) return false;
        } else if ( !size.equals(other.size) ) return false;
        if ( typeName == null ) {
            if ( other.typeName != null ) return false;
        } else if ( !typeName.equals(other.typeName) ) return false;
        if ( table.getTablePath() == null ) {
            if ( other.table.getTablePath() != null ) return false;
        } else if ( !table.getTablePath().equals(other.table.getTablePath()) ) return false;
        return true;
    }
}
