package net.madz.db.core.meta.mutable.base.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.type.TableType;
import net.madz.db.core.meta.mutable.ColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.ForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.IndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.SchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.TableMetaDataBuilder;

public class TableMetaDataBuilderImpl<SMDB extends SchemaMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, TMDB extends TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, CMDB extends ColumnMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, FMDB extends ForeignKeyMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, IMDB extends IndexMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD>, SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements TableMetaDataBuilder<SMDB, TMDB, CMDB, FMDB, IMDB, SMD, TMD, CMD, FMD, IMD> {

    private final SMD schema;
    // protected MetaDataResultSet<TableDbMetaDataEnum> rs;
    protected DottedPath tablePath; // catalog.schema.name
    protected TableType type;
    protected String remarks;
    protected String idCol, idGeneration;
    protected Map<String, CMDB> columnMap;
    protected List<CMDB> orderedColumns;
    protected Map<String, IMDB> indexMap;
    protected List<FMDB> fkList = new LinkedList<FMDB>();
    protected IMD primaryKey;

    public TableMetaDataBuilderImpl(SMD schema) {
        super();
        this.schema = schema;
    }

    @Override
    public DottedPath getTablePath() {
        return this.tablePath;
    }

    @Override
    public String getTableName() {
        return this.tablePath.getName();
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
        return this.primaryKey;
    }

    @Override
    public CMD getColumn(String columnName) {
        return (CMD) this.columnMap.get(columnName);
    }

    @Override
    public List<CMD> getColumns() {
        return (List<CMD>) this.orderedColumns;
    }

    @Override
    public Collection<FMD> getForeignKeySet() {
        return (Collection<FMD>) this.fkList;
    }

    @Override
    public Collection<IMD> getIndexSet() {
        return (Collection<IMD>) this.indexMap.values();
    }

    @Override
    public IMD getIndex(String indexName) {
        return (IMD) this.indexMap.get(indexName);
    }

    @Override
    public void appendColumn(CMDB column) {
        this.columnMap.put(column.getColumnName(), column);
    }

    @Override
    public void appendIndex(IMDB index) {
        this.indexMap.put(index.getIndexName(), index);
    }

    @Override
    public void appendForeignKey(FMDB foreignKey) {
        this.fkList.add(foreignKey);
    }
}
