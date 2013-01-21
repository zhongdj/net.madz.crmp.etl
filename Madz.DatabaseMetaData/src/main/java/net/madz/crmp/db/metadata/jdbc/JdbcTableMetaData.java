package net.madz.crmp.db.metadata.jdbc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.madz.crmp.db.metadata.DottedPath;
import net.madz.crmp.db.metadata.jdbc.type.JdbcTableType;

public interface JdbcTableMetaData extends JdbcMetaData {

    /** DottedPath of this table (catalog.schema.table) */
    DottedPath getTablePath();

    /**
     * @return
     */
    String getTableName();

    /**
     * @return
     */
    JdbcIndexMetaData getPrimaryKey();

    /**
     * @return
     */
    List<JdbcColumnMetaData> getColumns();

    /**
     * @param columnName
     * @return
     */
    JdbcColumnMetaData getColumn(String columnName);

    /**
     * @return
     */
    Collection<JdbcForeignKeyMetaData> getForeignKeySet();

    /**
     * @return
     */
    Collection<JdbcIndexMetaData> getIndexSet();

    /**
     * @param indexName
     * @return
     */
    JdbcIndexMetaData getIndex(String indexName);

    JdbcTableType getType();

    String getRemarks();

    String getIdCol();

    String getIdGeneration();

    Map<String, JdbcColumnMetaData> getColumnMap();

    List<JdbcColumnMetaData> getOrderedColumns();

    Map<String, JdbcIndexMetaData> getIndexMap();
}