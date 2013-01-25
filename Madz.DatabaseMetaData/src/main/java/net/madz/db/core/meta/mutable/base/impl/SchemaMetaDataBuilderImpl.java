package net.madz.db.core.meta.mutable.base.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.mutable.ColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.ForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.IndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.SchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.TableMetaDataBuilder;

public class SchemaMetaDataBuilderImpl<SMDB extends SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, TMDB extends TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, CMDB extends ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, FMDB extends ForeignKeyMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IMDB extends IndexMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD> {

    // TODO [Jan 22, 2013][barry][Done] Use modifier final with immutable fields
    protected final DottedPath schemaPath;
    protected final Map<String, TMDB> tableBuilderList = new TreeMap<String, TMDB>(String.CASE_INSENSITIVE_ORDER);
    
    public SchemaMetaDataBuilderImpl(final DottedPath schemaPath) throws SQLException {
        super();
        this.schemaPath = schemaPath;
    }

    @Override
    public DottedPath getSchemaPath() {
        return this.schemaPath;
    }

    @Override
    public Collection<TMD> getTables() {
        return (Collection<TMD>) this.tableBuilderList.values();
    }

    @Override
    public TMD getTable(String name) {
        return (TMD) this.tableBuilderList.get(name);
    }

    @Override
    public void appendTable(TMDB table) {
        tableBuilderList.put(table.getTableName(), table);
    }

}
