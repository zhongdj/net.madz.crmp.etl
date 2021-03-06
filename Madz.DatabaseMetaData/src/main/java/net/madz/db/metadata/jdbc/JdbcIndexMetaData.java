package net.madz.db.metadata.jdbc;

import java.util.Collection;

import net.madz.db.metadata.jdbc.impl.JdbcTableMetaDataImpl;
import net.madz.db.metadata.jdbc.type.JdbcIndexType;
import net.madz.db.metadata.jdbc.type.JdbcKeyType;
import net.madz.db.metadata.jdbc.type.JdbcSortDirection;


public interface JdbcIndexMetaData extends JdbcMetaData{

    public interface Entry {

        /** Index this entry belongs to */
        public JdbcIndexMetaData getKey();

        /** Column definition */
        public JdbcColumnMetaData getColumn();

        /** Column position in index */
        public Integer getPosition();
    }

    /** Name of the index */
    String getIndexName();

    /** Is this a unique index */
    boolean isUnique();

    /** KeyType of index */
    JdbcKeyType getKeyType();

    /** Type of the index */
    JdbcIndexType getIndexType();

    /** Ascending/Descending order */
    JdbcSortDirection getSortDirection();

    /** Index cardinality, if known */
    Integer getCardinality();

    /** Number of pages used by the index, if known */
    Integer getPageCount();

    /** Does this index use the specified column? */
    boolean containsColumn(JdbcColumnMetaData column);

    /** All columns in index */
    Collection<? extends JdbcIndexMetaData.Entry> getEntrySet();

    JdbcTableMetaData getTable();
}
