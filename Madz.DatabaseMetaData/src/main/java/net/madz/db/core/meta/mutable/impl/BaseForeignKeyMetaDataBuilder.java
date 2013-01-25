package net.madz.db.core.meta.mutable.impl;

import java.util.List;

import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.type.CascadeRule;
import net.madz.db.core.meta.immutable.type.KeyDeferrability;
import net.madz.db.core.meta.mutable.ColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.ForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.IndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.SchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.TableMetaDataBuilder;

public abstract class BaseForeignKeyMetaDataBuilder<SMDB extends SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, TMDB extends TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, CMDB extends ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, FMDB extends ForeignKeyMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IMDB extends IndexMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements ForeignKeyMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD> {

    @Override
    public String getForeignKeyName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMD getForeignKeyIndex() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TMD getForeignKeyTable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMD getPrimaryKeyIndex() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TMD getPrimaryKeyTable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CascadeRule getDeleteCascadeRule() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CascadeRule getUpdateCascadeRule() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyDeferrability getKeyDeferrability() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Entry<SMD, TMD, CMD, FMD, IMD>> getEntrySet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer size() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addEntry(Entry<SMD, TMD, CMD, FMD, IMD> entry) {
        // TODO Auto-generated method stub
    }
}
