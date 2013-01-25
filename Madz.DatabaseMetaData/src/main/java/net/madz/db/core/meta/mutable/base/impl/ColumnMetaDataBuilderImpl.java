package net.madz.db.core.meta.mutable.base.impl;

import java.util.Collection;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData.Entry;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.mutable.ColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.ForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.IndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.SchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.TableMetaDataBuilder;


public class ColumnMetaDataBuilderImpl<SMDB extends SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, TMDB extends TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, CMDB extends ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, FMDB extends ForeignKeyMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IMDB extends IndexMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
implements ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD> 
{

    @Override
    public DottedPath getColumnPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TMD getTableMetaData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getColumnName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getSqlType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSqlTypeName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getSize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getRadix() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRemarks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasDefaultValue() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getDefaultValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getCharOctetLength() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Short getOrdinalPosition() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isNullable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAutoIncremented() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Entry getPrimaryKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isMemberOfPrimaryKey() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMemberOfIndex() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMemberOfForeignKey(ForeignKeyMetaData fk) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection getUniqueIndexSet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isMemberOfUniqueIndex() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection getNonUniqueIndexSet() {
        // TODO Auto-generated method stub
        return null;
    }
}
