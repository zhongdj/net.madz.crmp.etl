package net.madz.db.core.meta.mutable.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.DottedPathImpl;
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

public abstract class BasedSchemaMetaDataBuilder<SMDB extends SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, TMDB extends TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, CMDB extends ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, FMDB extends ForeignKeyMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IMDB extends IndexMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        extends BaseMetaDataBuilder<SMD> implements SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>,
        SchemaMetaData<SMD, TMD, CMD, FMD, IMD> {

    // TODO [Jan 22, 2013][barry][Done] Use modifier final with immutable fields
    protected final DottedPath schemaPath;
    protected Map<String, TMDB> tableBuilderMap = new TreeMap<String, TMDB>(String.CASE_INSENSITIVE_ORDER);
    protected final Collection<TMDB> tableList = new LinkedList<TMDB>();

    public BasedSchemaMetaDataBuilder(final String databaseName) throws SQLException {
        super();
        this.schemaPath = new DottedPathImpl(databaseName);
    }

    @SuppressWarnings("unchecked")
    public SMDB appendTableMetaDataBuilder(TMDB table) {
        tableList.add(table);
        tableBuilderMap.put(table.getTablePath().getName(), table);
        return (SMDB) this;
    }

    public DottedPath getSchemaPath() {
        return this.schemaPath;
    }

    public Collection<TMD> getTables() {
        final Collection<TMD> result = new LinkedList<TMD>();
        for ( final TMDB tableBuilder : tableBuilderMap.values() ) {
            result.add(tableBuilder.getMetaData());
        }
        return result;
    }

    public TMD getTable(String name) {
        return this.tableBuilderMap.get(name).getMetaData();
    }
}
