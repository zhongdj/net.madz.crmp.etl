package net.madz.db.core.meta.immutable;

public interface IndexEntry<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>> {

    /** Index this entry belongs to */
    public IMD getKey();

    public Integer getSubPart();

    /** Column definition */
    public CMD getColumn();
    
    public String getColumnName();

    /** Column position in index */
    public Short getPosition();

    public boolean isNull();
}