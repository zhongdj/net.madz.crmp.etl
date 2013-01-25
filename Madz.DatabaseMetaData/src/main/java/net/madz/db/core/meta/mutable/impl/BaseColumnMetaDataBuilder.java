package net.madz.db.core.meta.mutable.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData.Entry;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.mutable.ColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.ForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.IndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.SchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.TableMetaDataBuilder;

public abstract class BaseColumnMetaDataBuilder<SMDB extends SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, TMDB extends TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, CMDB extends ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, FMDB extends ForeignKeyMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IMDB extends IndexMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, ColumnMetaData<SMD, TMD, CMD, FMD, IMD> {

    protected DottedPath columnPath;
    protected TMDB table;
    protected Integer sqlType;
    protected String sqlTypeName;
    protected Integer size;
    protected boolean isNullable;
    protected boolean isAutoIncremented;
    protected Integer radix, charOctetLength;
    protected String remarks, defaultValue;
    protected IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD> primaryKey;
    // TODO [Jan 22, 2013][barry][Done] Can this field be re-assign?
    protected final List<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> uniqueIndexList = new LinkedList<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>>();
    protected final List<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> nonUniqueIndexList = new LinkedList<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>>();
    protected final List<ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> fkList = new LinkedList<ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD>>();
    protected Short ordinalPosition;

    // protected final TMDB jdbcTableMetaDataBuilder;
    // protected final MetaDataResultSet<ColumnDbMetaDataEnum> colRs;
    public BaseColumnMetaDataBuilder(DottedPath name) {
        super();
        this.columnPath = name;
    }

    @Override
    public DottedPath getColumnPath() {
        return this.columnPath;
    }

    @Override
    public TMD getTableMetaData() {
        return this.table.getMetaData();
    }

    @Override
    public String getColumnName() {
        return this.columnPath.getName();
    }

    @Override
    public Integer getSqlType() {
        return this.sqlType;
    }

    @Override
    public String getSqlTypeName() {
        return this.sqlTypeName;
    }

    @Override
    public Integer getSize() {
        return this.size;
    }

    @Override
    public Integer getRadix() {
        return this.radix;
    }

    @Override
    public String getRemarks() {
        return this.remarks;
    }

    @Override
    public boolean hasDefaultValue() {
        return null != this.defaultValue;
    }

    @Override
    public String getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public Integer getCharOctetLength() {
        return this.charOctetLength;
    }

    @Override
    public Short getOrdinalPosition() {
        return this.ordinalPosition;
    }

    @Override
    public boolean isNullable() {
        return this.isNullable;
    }

    @Override
    public boolean isAutoIncremented() {
        return this.isAutoIncremented;
    }

    @Override
    public Entry<SMD, TMD, CMD, FMD, IMD> getPrimaryKey() {
        return this.primaryKey;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isMemberOfPrimaryKey() {
        // TODO [Jan 22, 2013][barry][Done] Use modifier final with immutable
        // variables
        final IMD primaryKey = this.table.getMetaData().getPrimaryKey();
        // TODO [Jan 22, 2013][barry] Is there any constraint that every table
        // has a primaryKey?
        if ( primaryKey.containsColumn((CMD) this) ) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isMemberOfIndex() {
        // TODO [Jan 22, 2013][barry][Done] Use modifier final with immutable
        // variables
        final Collection<IMD> indexSet = this.table.getMetaData().getIndexSet();
        for ( IMD index : indexSet ) {
            if ( index.containsColumn((CMD) this) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMemberOfForeignKey(FMD fk) {
        // TODO [Jan 22, 2013][barry][Done] Use modifier final with immutable
        // variables
        final Collection<FMD> foreignKeySet = this.table.getMetaData().getForeignKeySet();
        for ( FMD fkMetaData : foreignKeySet ) {
            if ( fkMetaData.getEntrySet().contains(this) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<Entry<SMD, TMD, CMD, FMD, IMD>> getUniqueIndexSet() {
        return this.uniqueIndexList;
    }

    @Override
    public boolean isMemberOfUniqueIndex() {
        return this.uniqueIndexList.contains(this);
    }

    @Override
    public Collection<Entry<SMD, TMD, CMD, FMD, IMD>> getNonUniqueIndexSet() {
        return this.nonUniqueIndexList;
    }
}