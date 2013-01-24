package net.madz.db.core.meta.immutable.impl;

import java.util.Collection;
import java.util.List;

import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.type.IndexType;
import net.madz.db.core.meta.immutable.type.KeyType;
import net.madz.db.core.meta.immutable.type.SortDirection;

public class IndexMetaDataImpl<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements IndexMetaData<SMD, TMD, CMD, FMD, IMD> {

    protected final TMD table;
    protected final String indexName;
    protected final IndexType indexType;
    protected final SortDirection ascending;
    protected final Integer cardinatlity;
    protected final Integer pages;
    protected final Collection<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> entryList;
    // TODO [Jan 22, 2013][barry][Done] ONLY keyType can be re-assign?
    protected final KeyType keyType;

    public class Entry implements IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD> {

        private final Integer position;
        private final CMD column;

        public Entry(CMD column, Integer position) {
            this.position = position;
            this.column = column;
        }

        @SuppressWarnings("unchecked")
		public IMD getKey() {
            return (IMD) IndexMetaDataImpl.this;
        }

        public CMD getColumn() {
            return this.column;
        }

        public Integer getPosition() {
            return this.position;
        }

        @Override
        public boolean equals(Object obj) {
            if ( obj instanceof IndexMetaDataImpl.Entry ) {
                IndexMetaDataImpl.Entry comp = (IndexMetaDataImpl.Entry) this;
                return this.getKey().equals(comp.getKey()) && this.column.getColumnName().equals(comp.getColumn().getColumnName());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return ( IndexMetaDataImpl.this.hashCode() * 3 ) + column.getColumnName().hashCode();
        }

        @Override
        public String toString() {
            return indexName + "." + position;
        }
    }

    

    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof IndexMetaDataImpl ) {
            IndexMetaDataImpl comp = (IndexMetaDataImpl) obj;
            return this.indexName.equals(comp.indexName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return indexName.hashCode();
    }

    public IndexMetaDataImpl(IMD metaData) {
        this.table = metaData.getTable();
        this.entryList = metaData.getEntrySet();
        this.indexName = metaData.getIndexName();
        this.indexType = metaData.getIndexType();
        this.cardinatlity = metaData.getCardinality();
        this.pages = metaData.getPageCount();
        this.ascending = metaData.getSortDirection();
        this.keyType = metaData.getKeyType();
    }

    public String getIndexName() {
        return this.indexName;
    }

    public boolean isUnique() {
        return this.keyType.isUnique();
    }

    public KeyType getKeyType() {
        return this.keyType;
    }

    /** Type of index */
    public IndexType getIndexType() {
        return this.indexType;
    }

    /** Ascending/descending order */
    public SortDirection getSortDirection() {
        return this.ascending;
    }

    /** Index cardinality, if known */
    public Integer getCardinality() {
        return this.cardinatlity;
    }

    /** Number of pages used by the index, if known */
    public Integer getPageCount() {
        return this.pages;
    }


    @Override
    public boolean containsColumn(CMD column) {
        for ( IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD> entry : entryList ) {
            if ( column.equals(entry.getColumn()) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(keyType.toString());
        sb.append(",");
        sb.append(indexName);
        return sb.toString();
    }

    @Override
    public Collection<IndexMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> getEntrySet() {
        return entryList;
    }

    @Override
    public TMD getTable() {
        return this.table;
    }
}
