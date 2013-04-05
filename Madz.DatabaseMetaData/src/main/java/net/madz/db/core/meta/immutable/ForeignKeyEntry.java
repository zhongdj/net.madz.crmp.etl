package net.madz.db.core.meta.immutable;

public interface ForeignKeyEntry<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>> {

    CMD getForeignKeyColumn();
    
    String getForeignKeyColumnName();

    CMD getPrimaryKeyColumn();
    
    String getPrimaryKeyColumnName();

    FMD getKey();

    Short getSeq();
}