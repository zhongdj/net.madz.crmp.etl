package net.madz.db.core.meta.immutable.mysql;

import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;

public interface MySQLTableMetaData extends
        TableMetaData<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
/*
 * < MSMD extends MySQLSchemaMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>, MTMD
 * extends MySQLTableMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>, MCMD extends
 * MySQLColumnMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>, MFMD extends
 * MySQLForeignKeyMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>, MIMD extends
 * MySQLIndexMetaData<MSMD, MTMD, MCMD, MFMD, MIMD> > extends
 * TableMetaData<MSMD,MTMD,MCMD,MFMD,MIMD>
 */{

    /** The table uses what kind of engine */
    MySQLEngineEnum getEngine();

    /** The table uses what kind of character set */
    String getCharacterSet();

    /** The table uses what kind of collation */
    String getCollation();
}
