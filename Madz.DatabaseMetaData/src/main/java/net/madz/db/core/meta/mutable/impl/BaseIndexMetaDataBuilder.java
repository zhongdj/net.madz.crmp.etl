package net.madz.db.core.meta.mutable.impl;

import java.util.Collection;
import java.util.List;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.types.IndexTypeEnum;
import net.madz.db.core.meta.immutable.types.KeyTypeEnum;
import net.madz.db.core.meta.immutable.types.SortDirectionEnum;
import net.madz.db.core.meta.mutable.ColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.ForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.IndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.SchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.TableMetaDataBuilder;

public abstract class BaseIndexMetaDataBuilder<SMDB extends SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, TMDB extends TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, CMDB extends ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, FMDB extends ForeignKeyMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IMDB extends IndexMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements IndexMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IndexMetaData<SMD, TMD, CMD, FMD, IMD> {

    protected final TMDB table;
    protected DottedPath indexPath;
    protected boolean isUnique;
    protected KeyTypeEnum keyType;
    protected IndexTypeEnum indexType;
    protected SortDirectionEnum sortDirection;
    protected Integer cardinatlity;
    protected Integer pages;
    protected List<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> entryList;

    public BaseIndexMetaDataBuilder(TMDB table, DottedPath indexPath) {
        super();
        this.table = table;
        this.indexPath = indexPath;
    }

    public class Entry implements IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD> {

        private IMD key;
        private int subPart;
        private CMD column;
        private int position;

        public Entry(IMD key, int subPart, CMD column, int position) {
            super();
            this.key = key;
            this.subPart = subPart;
            this.column = column;
            this.position = position;
        }

        @Override
        public IMD getKey() {
            return this.key;
        }

        @Override
        public Integer getSubPart() {
            return this.subPart;
        }

        @Override
        public CMD getColumn() {
            return this.column;
        }

        @Override
        public Integer getPosition() {
            return this.position;
        }
    }

    @Override
    public String getIndexName() {
        return this.indexPath.getName();
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public KeyTypeEnum getKeyType() {
        return this.keyType;
    }

    @Override
    public IndexTypeEnum getIndexType() {
        return this.indexType;
    }

    @Override
    public SortDirectionEnum getSortDirection() {
        return this.sortDirection;
    }

    @Override
    public Integer getCardinality() {
        return this.cardinatlity;
    }

    @Override
    public Integer getPageCount() {
        return this.pages;
    }

    @Override
    public boolean containsColumn(CMD column) {
        for ( IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD> entry : this.entryList ) {
            if ( entry.getColumn().equals(column) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> getEntrySet() {
        return this.entryList;
    }

    @Override
    public TMD getTable() {
        return this.table.getMetaData();
    }

    @Override
    public void addEntry(IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD> entry) {
        this.entryList.add(entry);
    }
}
