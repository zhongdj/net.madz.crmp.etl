package net.madz.db.metadata.jdbc.impl.builder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.metadata.jdbc.JdbcColumnMetaData;
import net.madz.db.metadata.jdbc.JdbcIndexMetaData;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.db.metadata.jdbc.impl.JdbcIndexMetaDataImpl;
import net.madz.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.db.metadata.jdbc.impl.enums.JdbcIndexDbMetaDataEnum;
import net.madz.db.metadata.jdbc.type.JdbcIndexType;
import net.madz.db.metadata.jdbc.type.JdbcKeyType;
import net.madz.db.metadata.jdbc.type.JdbcSortDirection;

public class JdbcIndexMetaDataBuilder implements JdbcIndexMetaData {
    // TODO [Jan 22, 2013][barry] Use modifier final with immutable fields, and consider life cycle. 
    protected JdbcTableMetaDataBuilder table;
    protected String indexName;
    protected JdbcIndexType indexType;
    protected JdbcSortDirection ascending;
    protected Integer cardinatlity;
    protected Integer pages;
    protected List<Entry> entryList;
    protected JdbcKeyType keyType;
    private JdbcMetaDataResultSet<JdbcIndexDbMetaDataEnum> ixRs;

    public class Entry implements JdbcIndexMetaData.Entry {

        private final Integer position;
        private final JdbcColumnMetaDataBuilder column;

        public Entry(JdbcColumnMetaDataBuilder column, Integer position) {
            this.position = position;
            this.column = column;
        }

        public JdbcIndexMetaDataBuilder getKey() {
            return JdbcIndexMetaDataBuilder.this;
        }

        public JdbcColumnMetaDataBuilder getColumn() {
            return this.column;
        }

        public Integer getPosition() {
            return this.position;
        }

        @Override
        public boolean equals(Object obj) {
            if ( obj instanceof JdbcIndexMetaDataImpl.Entry ) {
                JdbcIndexMetaDataBuilder.Entry comp = (JdbcIndexMetaDataBuilder.Entry) this;
                return this.getKey().equals(comp.getKey()) && this.column.getColumnName().equals(comp.getColumn().getColumnName());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return ( JdbcIndexMetaDataBuilder.this.hashCode() * 3 ) + column.getColumnName().hashCode();
        }

        @Override
        public String toString() {
            return indexName + "." + position;
        }
    }

    public JdbcIndexMetaDataBuilder(JdbcTableMetaDataBuilder metaData, JdbcMetaDataResultSet<JdbcIndexDbMetaDataEnum> ixRs) throws SQLException {
        this.table = metaData;
        this.ixRs = ixRs;
    }

    public void build(Connection connection) throws SQLException {
        System.out.println("Jdbc index metadata builder");
        this.entryList = new LinkedList<Entry>();
        boolean unique = ixRs.getBoolean(JdbcIndexDbMetaDataEnum.NON_UNIQUE);
        this.indexName = ixRs.get(JdbcIndexDbMetaDataEnum.INDEX_NAME);
        this.indexType = JdbcIndexType.getIndexTypeFromJdbcType(ixRs.getInt(JdbcIndexDbMetaDataEnum.TYPE));
        this.cardinatlity = ixRs.getInt(JdbcIndexDbMetaDataEnum.CARDINALITY);
        this.pages = ixRs.getInt(JdbcIndexDbMetaDataEnum.PAGES);
        String ascdec = ixRs.get(JdbcIndexDbMetaDataEnum.ASC_OR_DESC);
        if ( "A".equalsIgnoreCase(ascdec) ) {
            this.ascending = JdbcSortDirection.ascending;
        } else if ( "D".equalsIgnoreCase(ascdec) ) {
            this.ascending = JdbcSortDirection.descending;
        } else {
            this.ascending = JdbcSortDirection.unknown;
        }
        if ( unique )
            keyType = JdbcKeyType.uniqueKey;
        else
            keyType = JdbcKeyType.index;
    }

    public JdbcIndexMetaData getCopy() {
        return new JdbcIndexMetaDataImpl(this);
    }

    void addEntry(JdbcMetaDataResultSet<JdbcIndexDbMetaDataEnum> rs) throws SQLException {
        String colName = rs.get(JdbcIndexDbMetaDataEnum.COLUMN_NAME);
        Integer pos = rs.getInt(JdbcIndexDbMetaDataEnum.ORDINAL_POSITION);
        JdbcColumnMetaDataBuilder column = table.getColumn(colName);
        if ( null != column ) {
            Entry entry = new Entry(column, pos);
            this.entryList.add(entry);
            column.addIndex(entry);
        }
    }

    public final static String getName(JdbcMetaDataResultSet<JdbcIndexDbMetaDataEnum> rs) throws SQLException {
        return rs.get(JdbcIndexDbMetaDataEnum.INDEX_NAME);
    }

    @Override
    public String getIndexName() {
        return this.indexName;
    }

    @Override
    public boolean isUnique() {
        return this.keyType.isUnique();
    }

    @Override
    public JdbcKeyType getKeyType() {
        return this.keyType;
    }

    @Override
    public JdbcIndexType getIndexType() {
        return this.indexType;
    }

    @Override
    public JdbcSortDirection getSortDirection() {
        return this.ascending;
    }

    @Override
    public Integer getCardinality() {
        return this.cardinatlity;
    }

    @Override
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
    public Collection<? extends Entry> getEntrySet() {
        return this.entryList;
    }

    @Override
    public JdbcTableMetaData getTable() {
        return this.table;
    }

    public void setPrimaryKey() {
        keyType = JdbcKeyType.primaryKey;
        for ( Entry entry : this.entryList ) {
            entry.column.setPrimaryKey(entry);
        }
    }
}
