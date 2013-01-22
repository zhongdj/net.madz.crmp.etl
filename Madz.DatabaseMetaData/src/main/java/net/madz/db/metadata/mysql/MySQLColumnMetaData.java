package net.madz.db.metadata.mysql;

import net.madz.db.metadata.jdbc.JdbcColumnMetaData;

public interface MySQLColumnMetaData extends JdbcColumnMetaData {

    String getCharacterSet();

    String getCollation();
    
    MySQLColumnTypeEnum getColumnType();
}
