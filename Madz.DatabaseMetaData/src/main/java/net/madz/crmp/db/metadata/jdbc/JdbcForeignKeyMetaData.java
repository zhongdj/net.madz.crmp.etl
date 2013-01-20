package net.madz.crmp.db.metadata.jdbc;

import java.util.List;

import net.madz.crmp.db.metadata.jdbc.type.JdbcCascadeRule;
import net.madz.crmp.db.metadata.jdbc.type.JdbcKeyDeferrability;


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
    JdbcIndexMetaData getForeignKeyIndex();

    JdbcTableMetaData getForeignKeyTable();

    /**
     * primary key index
     * 
     * This is the index that being referred to
     * 
     * @return SqlIndexMetaData or null if there is no primary key index; MySQL
     *         appears to always give us null
     */
    JdbcIndexMetaData getPrimaryKeyIndex();

    JdbcTableMetaData getPrimaryKeyTable();

    /**
     * Defines what is to happen to this record when the referenced record is
     * deleted.
     */
    JdbcCascadeRule getDeleteCascadeRule();

    /**
     * Defines what is to happen to this record when the referenced record's
     * primary key is updated.
     */
    JdbcCascadeRule getUpdateCascadeRule();

    /**
     * Deferrability rule on this key
     */
    JdbcKeyDeferrability getKeyDeferrability();

    List<? extends Entry> getEntrySet();

    /**
     * Number of relationships in the key
     */
    Integer size();

    public interface Entry {

        JdbcColumnMetaData getForeignKeyColumn();

        JdbcColumnMetaData getPrimaryKeyColumn();

        JdbcForeignKeyMetaData getKey();
    }
}
