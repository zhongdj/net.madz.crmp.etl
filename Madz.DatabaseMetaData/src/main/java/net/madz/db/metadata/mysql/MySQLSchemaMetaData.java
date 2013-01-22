package net.madz.db.metadata.mysql;

import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;

public interface MySQLSchemaMetaData extends JdbcSchemaMetaData {

    String getCharSet();

    String getCollation();
}
