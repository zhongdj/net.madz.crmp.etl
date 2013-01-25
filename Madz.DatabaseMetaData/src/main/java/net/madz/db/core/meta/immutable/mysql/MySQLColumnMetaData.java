package net.madz.db.core.meta.immutable.mysql;

import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLColumnTypeEnum;

public interface MySQLColumnMetaData

extends ColumnMetaData<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
/*
<
MSMD extends MySQLSchemaMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>, 
MTMD extends MySQLTableMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>, 
MCMD extends MySQLColumnMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>,
MFMD extends MySQLForeignKeyMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>, 
MIMD extends MySQLIndexMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>
>
extends
        ColumnMetaData<MSMD,MTMD,MCMD,MFMD,MIMD> 
*/

{

    String getCharacterSet();

    String getCollation();

    MySQLColumnTypeEnum getColumnType();
}
