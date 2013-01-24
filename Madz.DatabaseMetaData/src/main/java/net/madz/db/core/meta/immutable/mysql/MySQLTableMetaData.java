package net.madz.db.core.meta.immutable.mysql;

import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLTableTypeEnum;

public interface MySQLTableMetaData extends
        TableMetaData<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    /** Type of table */
    MySQLTableTypeEnum getTableType();

    /** The table uses what kind of engine */
    MySQLEngineEnum getEngine();

    /** The table uses what kind of character set */
    String getCharacterSet();

    /** The table uses what kind of collation */
    String getCollation();
}
