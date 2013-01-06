package net.madz.crmp.db.metadata;

import java.util.Collection;
import java.util.List;

public interface JdbcTableMetaData extends JdbcMetaData {

    /**
     * @return
     */
    String getTableName();

    /**
     * @return
     */
    <T extends JdbcIndexMetaData> T getPrimaryKey();

    /**
     * @return
     */
    List<? extends JdbcColumnMetaData> getColumns();

    /**
     * @param columnName
     * @return
     */
    <T extends JdbcColumnMetaData> T getColumn(String columnName);

    /**
     * @return
     */
    Collection<? extends JdbcForeignKeyMetaData> getForeignKeySet();

    /**
     * @return
     */
    Collection<? extends JdbcIndexMetaData> getIndexSet();

    /**
     * @param indexName
     * @return
     */
    <T extends JdbcIndexMetaData> T getIndex(String indexName);
}