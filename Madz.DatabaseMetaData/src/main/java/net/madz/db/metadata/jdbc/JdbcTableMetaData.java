package net.madz.db.metadata.jdbc;

import java.util.Collection;
import java.util.List;

import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.type.JdbcTableType;

public interface JdbcTableMetaData extends JdbcMetaData {

    /** DottedPath of this table (catalog.schema.table) */
    DottedPath getTablePath();

    /** Table name */
    String getTableName();

    /**
     * Type of this table, such as
     * "TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"
     * 、"LOCAL TEMPORARY"、"ALIAS" 和 "SYNONYM"
     */
    JdbcTableType getType();

    /** Remarks for table */
    String getRemarks();

    /** Column self_referencing_col_name */
    String getIdCol();

    /** Column ref_generation */
    String getIdGeneration();

    /** Primary key */
    JdbcIndexMetaData getPrimaryKey();

    JdbcColumnMetaData getColumn(String columnName);

    List<JdbcColumnMetaData> getColumns();

    Collection<JdbcForeignKeyMetaData> getForeignKeySet();

    Collection<JdbcIndexMetaData> getIndexSet();

    JdbcIndexMetaData getIndex(String indexName);
}