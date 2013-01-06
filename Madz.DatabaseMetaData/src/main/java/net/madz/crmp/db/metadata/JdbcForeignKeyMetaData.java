package net.madz.crmp.db.metadata;

import java.util.List;

/**
 * JDBC DatabaseMetaData about a foreign-key constraint
 */
public interface JdbcForeignKeyMetaData extends JdbcMetaData {

    /**
     * Foreign key index
     * 
     * This is the index that is referring to another table
     * 
     * @return SqlIndexMetaData or null if there is no foreign key index; MySQL
     *         appears to always give us a value
     */
    public JdbcIndexMetaData getForeignKeyIndex();

    public JdbcTableMetaData getForeignKeyTable();

    /**
     * primary key index
     * 
     * This is the index that being referred to
     * 
     * @return SqlIndexMetaData or null if there is no primary key index; MySQL
     *         appears to always give us null
     */
    public JdbcIndexMetaData getPrimaryKeyIndex();

    public JdbcTableMetaData getPrimaryKeyTable();

    /**
     * Defines what is to happen to this record when the referenced record is
     * deleted.
     */
    public JdbcCascadeRule getDeleteCascadeRule();

    /**
     * Defines what is to happen to this record when the referenced record's
     * primary key is updated.
     */
    public JdbcCascadeRule getUpdateCascadeRule();

    /**
     * Deferrability rule on this key
     */
    public JdbcKeyDeferrability getKeyDeferrability();

    public List<? extends Entry> getEntrySet();

    /**
     * Number of relationships in the key
     */
    public int size();

    public interface Entry {

        public JdbcColumnMetaData getForeignKeyColumn();

        public JdbcColumnMetaData getPrimaryKeyColumn();

        public JdbcForeignKeyMetaData getKey();
    }
}
