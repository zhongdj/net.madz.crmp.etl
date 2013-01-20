package net.madz.crmp.db.metadata.mysql;

import net.madz.crmp.db.metadata.jdbc.JdbcTableMetaData;

/**
 * @author tracy
 * 
 */
public interface MySqlTableMetaData extends JdbcTableMetaData {

    /** Type of table */
    MySqlTableType getTableType();

    /** The table uses what kind of engine */
    MySqlEngine getEngine();

    /** The table uses what kind of character set */
    String getCharacterSet();

    /** The table uses what kind of collation */
    String getCollation();
}
