package net.madz.db.core.meta.mutable.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.types.TableType;
import net.madz.db.core.meta.mutable.ColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.ForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.IndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.SchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.TableMetaDataBuilder;

public abstract class BaseTableMetaDataBuilder<SMDB extends SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, TMDB extends TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, CMDB extends ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, FMDB extends ForeignKeyMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IMDB extends IndexMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        extends BaseMetaDataBuilder<TMD> implements TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>,
        TableMetaData<SMD, TMD, CMD, FMD, IMD> {

    protected final SMDB schema;
    // protected MetaDataResultSet<TableDbMetaDataEnum> rs;
    private final String tableName;
    protected final DottedPath tablePath; // catalog.schema.name
    protected TableType type;
    protected String remarks;
    protected String idCol, idGeneration;
    protected final Map<String, CMDB> columnMap = new HashMap<String, CMDB>();
    protected final List<CMDB> orderedColumns = new LinkedList<CMDB>();
    protected final Map<String, IMDB> indexMap = new HashMap<String, IMDB>();
    protected List<FMDB> fkList = new LinkedList<FMDB>();
    protected IMDB primaryKey;

    public BaseTableMetaDataBuilder(SMDB schema, String tableName) {
        this.schema = schema;
        this.tableName = tableName;
        this.tablePath = schema.getSchemaPath().append(tableName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public TMDB appendColumnMetaDataBuilder(CMDB column) {
        this.orderedColumns.add(column);
        this.columnMap.put(column.getColumnName().toLowerCase(), column);
        return (TMDB) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TMDB appendIndexMetaDataBuilder(IMDB index) {
        this.indexMap.put(index.getIndexName(), index);
        return (TMDB) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TMDB appendForeignKeyMetaDataBuilder(FMDB foreignKey) {
        this.fkList.add(foreignKey);
        return (TMDB) this;
    }

    @Override
    public DottedPath getTablePath() {
        return this.tablePath;
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

    @Override
    public TableType getType() {
        return this.type;
    }

    @Override
    public String getRemarks() {
        return this.remarks;
    }

    @Override
    public String getIdCol() {
        return this.idCol;
    }

    @Override
    public String getIdGeneration() {
        return this.idGeneration;
    }

    @Override
    public IMD getPrimaryKey() {
        return this.primaryKey.getMetaData();
    }

    @Override
    public CMD getColumn(String columnName) {
        return this.columnMap.get(columnName).getMetaData();
    }

    @Override
    public List<CMD> getColumns() {
        final List<CMD> result = new LinkedList<CMD>();
        for ( CMDB builder : this.orderedColumns ) {
            result.add(builder.getMetaData());
        }
        return result;
    }

    @Override
    public Collection<FMD> getForeignKeySet() {
        final Collection<FMD> result = new LinkedList<FMD>();
        for ( FMDB builder : this.fkList ) {
            result.add(builder.getMetaData());
        }
        return result;
    }

    @Override
    public Collection<IMD> getIndexSet() {
        final Collection<IMD> result = new LinkedList<IMD>();
        for ( IMDB builder : this.indexMap.values() ) {
            result.add(builder.getMetaData());
        }
        return result;
    }

    @Override
    public IMD getIndex(String indexName) {
        return this.indexMap.get(indexName).getMetaData();
    }

    @Override
    public SMDB getSchema() {
        return this.schema;
    }
}
