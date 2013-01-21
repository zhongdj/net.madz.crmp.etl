package net.madz.crmp.db.metadata.mysql;

import net.madz.crmp.db.metadata.jdbc.JdbcColumnMetaData;

public interface MySQLColumnMetaData extends JdbcColumnMetaData {

    String getCharacterSet();

    String getCollation();
}
