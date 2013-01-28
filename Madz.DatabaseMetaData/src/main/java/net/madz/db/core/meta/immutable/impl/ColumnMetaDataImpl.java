package net.madz.db.core.meta.immutable.impl;

import java.util.Collection;
import java.util.Collections;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
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
    protected final IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD> primaryKey;
    protected final Collection<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> uniqueIndexList;
    protected final Collection<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> nonUniqueIndexList;
    protected final Short ordinalPosition;

    public ColumnMetaDataImpl(CMD metaData) {
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
        this.primaryKey = metaData.getPrimaryKey();
        this.uniqueIndexList = Collections.unmodifiableCollection(metaData.getUniqueIndexSet());
        this.nonUniqueIndexList = Collections.unmodifiableCollection(metaData.getNonUniqueIndexSet());
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
    public IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD> getPrimaryKey() {
        return this.primaryKey;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isMemberOfPrimaryKey() {
        IMD primaryKey = this.table.getPrimaryKey();
        if ( primaryKey.containsColumn((CMD) this) ) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isMemberOfIndex() {
        Collection<IMD> indexSet = this.table.getIndexSet();
        for ( IMD index : indexSet ) {
            if ( index.containsColumn((CMD) this) ) {
                return true;
            }
        }
        return false;
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
    public Collection<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> getUniqueIndexSet() {
        return this.uniqueIndexList;
    }

    @Override
    public boolean isMemberOfUniqueIndex() {
        return this.uniqueIndexList.contains(this);
    }

    @Override
    public Collection<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> getNonUniqueIndexSet() {
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
}
