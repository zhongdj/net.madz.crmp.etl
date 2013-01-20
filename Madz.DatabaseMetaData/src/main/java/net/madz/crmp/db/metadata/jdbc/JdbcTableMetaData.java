package net.madz.crmp.db.metadata.jdbc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.madz.crmp.db.metadata.DottedPath;
import net.madz.crmp.db.metadata.jdbc.type.JdbcTableType;

public interface JdbcTableMetaData<I extends JdbcIndexMetaData, C extends JdbcColumnMetaData, F extends JdbcForeignKeyMetaData> extends JdbcMetaData {

    /** DottedPath of this table (catalog.schema.table) */
    DottedPath getTablePath();

    /**
     * @return
     */
    String getTableName();

    /**
     * @return
     */
    I getPrimaryKey();

    /**
     * @return
     */
    List<C> getColumns();

    /**
     * @param columnName
     * @return
     */
    C getColumn(String columnName);

    /**
     * @return
     */
    Collection<F> getForeignKeySet();

    /**
     * @return
     */
    Collection<I> getIndexSet();

    /**
     * @param indexName
     * @return
     */
    I getIndex(String indexName);

    JdbcTableType getType();

    String getRemarks();

    String getIdCol();

    String getIdGeneration();

    Map<String, JdbcColumnMetaData> getColumnMap();

    List<JdbcColumnMetaData> getOrderedColumns();

    Map<String, JdbcIndexMetaData> getIndexMap();
}