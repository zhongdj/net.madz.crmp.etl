package net.madz.db.metadata.mysql;

import net.madz.db.metadata.jdbc.JdbcIndexMetaData;

/** Refer http://dev.mysql.com/doc/refman/5.5/en/show-index.html */
public interface MySQLIndexMetaData extends JdbcIndexMetaData {

    int getSubPart();

    boolean isNull();

    MySQLIndexMethod getIndexMethod();
}
