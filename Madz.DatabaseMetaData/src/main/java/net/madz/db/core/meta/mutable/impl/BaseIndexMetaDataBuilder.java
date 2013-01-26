package net.madz.db.core.meta.mutable.impl;

import java.util.Collection;

import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.types.IndexType;
import net.madz.db.core.meta.immutable.types.KeyType;
import net.madz.db.core.meta.immutable.types.SortDirection;
import net.madz.db.core.meta.mutable.ColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.ForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.IndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.SchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.TableMetaDataBuilder;

public abstract class BaseIndexMetaDataBuilder<SMDB extends SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, TMDB extends TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, CMDB extends ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, FMDB extends ForeignKeyMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IMDB extends IndexMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
implements IndexMetaDataBuilder 
<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IndexMetaData<SMD, TMD, CMD, FMD, IMD>
{

    @Override
    public String getIndexName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isUnique() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public KeyType getKeyType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IndexType getIndexType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SortDirection getSortDirection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getCardinality() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getPageCount() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsColumn(CMD column) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection<Entry<SMD, TMD, CMD, FMD, IMD>> getEntrySet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TMD getTable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addEntry(Entry<SMD, TMD, CMD, FMD, IMD> entry) {
        // TODO Auto-generated method stub
        
    }
}
