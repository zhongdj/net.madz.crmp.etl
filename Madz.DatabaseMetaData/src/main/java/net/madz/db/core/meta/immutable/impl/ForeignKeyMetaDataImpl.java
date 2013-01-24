package net.madz.db.core.meta.immutable.impl;

import java.util.List;

import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.type.CascadeRule;
import net.madz.db.core.meta.immutable.type.KeyDeferrability;

public class ForeignKeyMetaDataImpl<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD> {

    public interface Entry<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
          extends ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD> {
        
    }
    
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
    public Integer size() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> getEntrySet() {
        return null;
    }
}
