package net.madz.db.core.meta.immutable.mysql;

import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLColumnTypeEnum;

public interface MySQLColumnMetaData extends
        ColumnMetaData<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    String getCharacterSet();

    String getCollation();

    MySQLColumnTypeEnum getColumnType();
}
