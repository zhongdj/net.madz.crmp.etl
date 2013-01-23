package net.madz.db.metadata.mysql;

import java.util.Collection;

import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;

public interface MySQLSchemaMetaData extends JdbcSchemaMetaData {

    String getCharSet();

    String getCollation();
    
    @Override
    Collection<MySQLTableMetaData> getTables();
    
    @SuppressWarnings("unchecked")
    @Override
	MySQLTableMetaData getTable(String name);
}
