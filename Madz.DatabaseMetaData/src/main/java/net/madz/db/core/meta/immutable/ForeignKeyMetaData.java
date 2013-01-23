package net.madz.db.core.meta.immutable;

public interface ForeignKeyMetaData <
SMD extends SchemaMetaData<?, TMD, CMD, FMD, IMD>,
TMD extends TableMetaData<SMD, ?, CMD, FMD, IMD>,
CMD extends ColumnMetaData<SMD, TMD, ?, FMD, IMD>,
FMD extends ForeignKeyMetaData<SMD, TMD, CMD, ?, IMD>,
IMD extends IndexMetaData<SMD, TMD, CMD, FMD, ?>
> {

}
