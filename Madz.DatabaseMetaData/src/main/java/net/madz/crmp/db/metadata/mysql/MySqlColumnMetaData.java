package net.madz.crmp.db.metadata.mysql;

import net.madz.crmp.db.metadata.jdbc.JdbcColumnMetaData;

public interface MySqlColumnMetaData extends JdbcColumnMetaData {

    String getCharacterSet();

    String getCollation();
}
