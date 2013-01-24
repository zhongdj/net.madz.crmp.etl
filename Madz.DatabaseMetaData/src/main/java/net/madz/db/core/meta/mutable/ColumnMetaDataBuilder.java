package net.madz.db.core.meta.mutable;

import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;

public interface ColumnMetaDataBuilder<SMDB extends SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, TMDB extends TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, CMDB extends ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, FMDB extends ForeignKeyMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IMDB extends IndexMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD> {
}
