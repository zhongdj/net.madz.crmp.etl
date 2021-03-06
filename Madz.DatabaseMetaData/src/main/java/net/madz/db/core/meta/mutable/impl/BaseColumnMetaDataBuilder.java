package net.madz.db.core.meta.mutable.impl;

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
import net.madz.db.core.meta.mutable.ColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.ForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.IndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.SchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.TableMetaDataBuilder;

public abstract class BaseColumnMetaDataBuilder<SMDB extends SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, TMDB extends TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, CMDB extends ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, FMDB extends ForeignKeyMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IMDB extends IndexMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        extends BaseMetaDataBuilder<CMD> implements ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>,
        ColumnMetaData<SMD, TMD, CMD, FMD, IMD> {

    protected final DottedPath columnPath;
    protected final TMDB tableBuilder;
    protected String sqlTypeName;
    protected Integer size;
    protected boolean isNullable;
    protected boolean isAutoIncremented;
    protected Integer radix;
    protected long characterOctetLength;
    protected String remarks, defaultValue;
    protected IndexEntry<SMD, TMD, CMD, FMD, IMD> primaryKey;
    // TODO [Jan 22, 2013][barry][Done] Can this field be re-assign?
    protected final List<IndexEntry<SMD, TMD, CMD, FMD, IMD>> uniqueIndexList = new LinkedList<IndexEntry<SMD, TMD, CMD, FMD, IMD>>();
    protected final List<IndexEntry<SMD, TMD, CMD, FMD, IMD>> nonUniqueIndexList = new LinkedList<IndexEntry<SMD, TMD, CMD, FMD, IMD>>();
    protected final List<ForeignKeyEntry<SMD, TMD, CMD, FMD, IMD>> fkList = new LinkedList<ForeignKeyEntry<SMD, TMD, CMD, FMD, IMD>>();
    protected Short ordinalPosition;

    // protected final TMDB jdbcTableMetaDataBuilder;
    // protected final MetaDataResultSet<ColumnDbMetaDataEnum> colRs;
    public BaseColumnMetaDataBuilder(TMDB tableBuilder, String name) {
        this.tableBuilder = tableBuilder;
        this.columnPath = tableBuilder.getTablePath().append(name);
    }

    @Override
    public DottedPath getColumnPath() {
        return this.columnPath;
    }

    @Override
    public TMD getTableMetaData() {
        return this.tableBuilder.getMetaData();
    }

    @Override
    public String getColumnName() {
        return this.columnPath.getName();
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
        return null != this.defaultValue && 0 > this.defaultValue.length();
    }

    @Override
    public String getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public Long getCharOctetLength() {
        return this.characterOctetLength;
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
    public void setSqlTypeName(String sqlTypeName) {
        this.sqlTypeName = sqlTypeName;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void setNullable(boolean isNullable) {
        this.isNullable = isNullable;
    }

    @Override
    public void setAutoIncremented(boolean isAutoIncremented) {
        this.isAutoIncremented = isAutoIncremented;
    }

    @Override
    public void setRadix(int radix) {
        this.radix = radix;
    }

    @Override
    public void setCharacterOctetLength(long characterOctetLength) {
        this.characterOctetLength = characterOctetLength;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void setOrdinalPosition(Short ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    @Override
    public IndexEntry<SMD, TMD, CMD, FMD, IMD> getPrimaryKey() {
        return this.primaryKey;
    }

    @Override
    public void setPrimaryKey(IndexEntry<SMD, TMD, CMD, FMD, IMD> entry) {
        this.primaryKey = entry;
        this.uniqueIndexList.remove(primaryKey);
    }

    @Override
    public boolean isMemberOfPrimaryKey() {
        return null != this.primaryKey;
    }

    @Override
    public boolean isMemberOfIndex() {
        return isMemberOfPrimaryKey() || isMemberOfUniqueIndex() || this.nonUniqueIndexList.size() > 0;
    }

    @Override
    public boolean isMemberOfForeignKey(FMD fk) {
        // TODO [Jan 22, 2013][barry][Done] Use modifier final with immutable
        // variables
        final Collection<FMD> foreignKeySet = this.tableBuilder.getMetaData().getForeignKeySet();
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
        return null != this.uniqueIndexList;
    }

    @Override
    public Collection<IndexEntry<SMD, TMD, CMD, FMD, IMD>> getNonUniqueIndexSet() {
        return this.nonUniqueIndexList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CMDB appendUniqueIndexEntry(IndexEntry<SMD, TMD, CMD, FMD, IMD> entry) {
        // todo
        this.uniqueIndexList.add(entry);
        return (CMDB) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CMDB appendNonUniqueIndexEntry(IndexEntry<SMD, TMD, CMD, FMD, IMD> entry) {
        this.nonUniqueIndexList.add(entry);
        return (CMDB) this;
    }
}
