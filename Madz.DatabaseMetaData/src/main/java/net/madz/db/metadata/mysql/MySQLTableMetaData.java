package net.madz.db.metadata.mysql;

import net.madz.db.metadata.jdbc.JdbcTableMetaData;

/**
 * @author tracy
 * 
 */
public interface MySQLTableMetaData extends JdbcTableMetaData {

    /** Type of table */
    MySQLTableTypeEnum getTableType();

    /** The table uses what kind of engine */
    MySQLEngineEnum getEngine();

    /** The table uses what kind of character set */
    String getCharacterSet();

    /** The table uses what kind of collation */
    String getCollation();
}
