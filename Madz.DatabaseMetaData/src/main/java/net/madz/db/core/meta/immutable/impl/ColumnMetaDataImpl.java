package net.madz.db.core.meta.immutable.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;

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
    protected IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD> primaryKey;
    protected final Collection<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> uniqueIndexList = new LinkedList<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>>();
    protected final Collection<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> nonUniqueIndexList = new LinkedList<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>>();;
    protected final Short ordinalPosition;
    protected final List<ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> fkList = new LinkedList<ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD>>();

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
    public IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD> getPrimaryKey() {
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
    public Collection<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> getUniqueIndexSet() {
        return this.uniqueIndexList;
    }

    @Override
    public boolean isMemberOfUniqueIndex() {
        return ( this.uniqueIndexList.size() > 0 );
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
}
