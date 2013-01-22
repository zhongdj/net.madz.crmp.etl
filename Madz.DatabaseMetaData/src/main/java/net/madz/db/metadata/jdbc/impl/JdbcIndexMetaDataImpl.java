package net.madz.db.metadata.jdbc.impl;

import java.util.Collection;
import java.util.List;

import net.madz.db.metadata.jdbc.JdbcColumnMetaData;
import net.madz.db.metadata.jdbc.JdbcIndexMetaData;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.db.metadata.jdbc.type.JdbcIndexType;
import net.madz.db.metadata.jdbc.type.JdbcKeyType;
import net.madz.db.metadata.jdbc.type.JdbcSortDirection;

public class JdbcIndexMetaDataImpl implements JdbcIndexMetaData {

    protected final JdbcTableMetaData table;
    protected final String indexName;
    protected final JdbcIndexType indexType;
    protected final JdbcSortDirection ascending;
    protected final Integer cardinatlity;
    protected final Integer pages;
    protected final List<Entry> entryList;
    // TODO [Jan 22, 2013][barry] ONLY keyType can be re-assign?
    protected JdbcKeyType keyType;

    public class Entry implements JdbcIndexMetaData.Entry {

        private final Integer position;
        private final JdbcColumnMetaData column;

        public Entry(JdbcColumnMetaData column, Integer position) {
            this.position = position;
            this.column = column;
        }

        public JdbcIndexMetaData getKey() {
            return JdbcIndexMetaDataImpl.this;
        }

        public JdbcColumnMetaData getColumn() {
            return this.column;
        }

        public Integer getPosition() {
            return this.position;
        }

        @Override
        public boolean equals(Object obj) {
            if ( obj instanceof JdbcIndexMetaDataImpl.Entry ) {
                JdbcIndexMetaDataImpl.Entry comp = (JdbcIndexMetaDataImpl.Entry) this;
                return this.getKey().equals(comp.getKey()) && this.column.getColumnName().equals(comp.getColumn().getColumnName());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return ( JdbcIndexMetaDataImpl.this.hashCode() * 3 ) + column.getColumnName().hashCode();
        }

        @Override
        public String toString() {
            return indexName + "." + position;
        }
    }

    

    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof JdbcIndexMetaDataImpl ) {
            JdbcIndexMetaDataImpl comp = (JdbcIndexMetaDataImpl) obj;
            return this.indexName.equals(comp.indexName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return indexName.hashCode();
    }

    public JdbcIndexMetaDataImpl(JdbcIndexMetaData metaData) {
        this.table = metaData.getTable();
        this.entryList = (List<Entry>) metaData.getEntrySet();
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

    public JdbcKeyType getKeyType() {
        return this.keyType;
    }

    /** Type of index */
    public JdbcIndexType getIndexType() {
        return this.indexType;
    }

    /** Ascending/descending order */
    public JdbcSortDirection getSortDirection() {
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
    public boolean containsColumn(JdbcColumnMetaData column) {
        for ( Entry entry : entryList ) {
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
    public Collection<Entry> getEntrySet() {
        return entryList;
    }

    @Override
    public JdbcTableMetaData getTable() {
        return this.table;
    }
}
