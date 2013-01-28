package net.madz.db.core.meta.immutable.mysql;

import net.madz.db.core.meta.immutable.SchemaMetaData;

public interface MySQLSchemaMetaData extends
        SchemaMetaData<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    String getCharSet();

    String getCollation();
}
