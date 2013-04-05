package net.madz.db.core.meta.immutable.impl;

import java.util.Collection;
import java.util.LinkedList;

import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexEntry;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.types.IndexTypeEnum;
import net.madz.db.core.meta.immutable.types.KeyTypeEnum;
import net.madz.db.core.meta.immutable.types.SortDirectionEnum;

public class IndexMetaDataImpl<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements IndexMetaData<SMD, TMD, CMD, FMD, IMD> {

    protected final TMD table;
    protected final String indexName;
    protected final IndexTypeEnum indexType;
    protected final SortDirectionEnum sortDirection;
    protected final Integer cardinatlity;
    protected final Integer pages;
    protected final Collection<IndexEntry<SMD, TMD, CMD, FMD, IMD>> entryList = new LinkedList<IndexEntry<SMD, TMD, CMD, FMD, IMD>>();
    // TODO [Jan 22, 2013][barry][Done] ONLY keyType can be re-assign?
    protected final KeyTypeEnum keyType;

    public class Entry implements IndexEntry<SMD, TMD, CMD, FMD, IMD> {

        private final Short position;
        private final CMD column;
        private final Integer subPart;

        public Entry(CMD column, Short position, Integer subPart) {
            this.position = position;
            this.column = column;
            this.subPart = subPart;
        }

        @SuppressWarnings("unchecked")
        public IMD getKey() {
            return (IMD) IndexMetaDataImpl.this;
        }

        public CMD getColumn() {
            return this.column;
        }

        public Short getPosition() {
            return this.position;
        }

        @Override
        public String toString() {
            return indexName + "." + position;
        }

        @Override
        public Integer getSubPart() {
            return subPart;
        }

        @Override
        public boolean isNull() {
            return column.isNullable();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().getIndexName().hashCode();
            result = prime * result + ( ( position == null ) ? 0 : position.hashCode() );
            result = prime * result + ( ( subPart == null ) ? 0 : subPart.hashCode() );
            result = prime * result + ( ( column == null ) ? 0 : column.hashCode() );
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if ( this == obj ) return true;
            if ( obj == null ) return false;
            if ( getClass() != obj.getClass() ) return false;
            Entry other = (Entry) obj;
            if ( !getOuterType().getIndexName().equals(other.getOuterType().getIndexName()) ) return false;
            if ( position == null ) {
                if ( other.position != null ) return false;
            } else if ( !position.equals(other.position) ) return false;
            if ( subPart == null ) {
                if ( other.subPart != null ) return false;
            } else if ( !subPart.equals(other.subPart) ) return false;
            if ( column == null ) {
                if ( other.column != null ) return false;
            } else if ( !column.equals(other.column) ) return false;
            return true;
        }

        private IndexMetaDataImpl getOuterType() {
            return IndexMetaDataImpl.this;
        }

        @Override
        public String getColumnName() {
            return column.getColumnName();
        }
    }

    public IndexMetaDataImpl(TMD parent, IMD metaData) {
        this.table = parent;
        this.indexName = metaData.getIndexName();
        this.indexType = metaData.getIndexType();
        this.cardinatlity = metaData.getCardinality();
        this.pages = metaData.getPageCount();
        this.sortDirection = metaData.getSortDirection();
        this.keyType = metaData.getKeyType();
        for ( IndexEntry<SMD, TMD, CMD, FMD, IMD> entry : metaData.getEntrySet() ) {
            CMD column = parent.getColumn(entry.getColumn().getColumnName());
            Short position = entry.getPosition();
            Integer subPart = entry.getSubPart();
            this.entryList.add(new Entry(column, position, subPart));
        }
    }

    public String getIndexName() {
        return this.indexName;
    }

    public boolean isUnique() {
        return this.keyType.isUnique();
    }

    public KeyTypeEnum getKeyType() {
        return this.keyType;
    }

    /** Type of index */
    public IndexTypeEnum getIndexType() {
        return this.indexType;
    }

    /** Ascending/descending order */
    public SortDirectionEnum getSortDirection() {
        return this.sortDirection;
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
        for ( IndexEntry<SMD, TMD, CMD, FMD, IMD> entry : entryList ) {
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
    public Collection<IndexEntry<SMD, TMD, CMD, FMD, IMD>> getEntrySet() {
        return entryList;
    }

    @Override
    public TMD getTable() {
        return this.table;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( table.getTablePath() == null ) ? 0 : table.getTablePath().hashCode() );
        result = prime * result + ( ( indexName == null ) ? 0 : indexName.hashCode() );
        result = prime * result + ( ( keyType == null ) ? 0 : keyType.hashCode() );
        result = prime * result + ( ( indexType == null ) ? 0 : indexType.hashCode() );
        result = prime * result + ( ( sortDirection == null ) ? 0 : sortDirection.hashCode() );
        result = prime * result + ( ( entryList == null ) ? 0 : entryList.hashCode() );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        IndexMetaDataImpl other = (IndexMetaDataImpl) obj;
        if ( table.getTablePath() == null ) {
            if ( other.table.getTablePath() != null ) return false;
        } else if ( !table.getTablePath().equals(other.table.getTablePath()) ) return false;
        if ( indexName == null ) {
            if ( other.indexName != null ) return false;
        } else if ( !indexName.equals(other.indexName) ) return false;
        if ( keyType != other.keyType ) return false;
        if ( indexType != other.indexType ) return false;
        if ( sortDirection != other.sortDirection ) return false;
        if ( entryList == null ) {
            if ( other.entryList != null ) return false;
        } else if ( !entryList.equals(other.entryList) ) return false;
        return true;
    }
}
