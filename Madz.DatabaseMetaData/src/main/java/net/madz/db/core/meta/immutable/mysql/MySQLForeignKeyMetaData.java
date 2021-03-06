package net.madz.db.core.meta.immutable.mysql;

import net.madz.db.core.meta.immutable.ForeignKeyMetaData;

public interface MySQLForeignKeyMetaData extends
        ForeignKeyMetaData<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
/*
 * < MSMD extends MySQLSchemaMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>, MTMD
 * extends MySQLTableMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>, MCMD extends
 * MySQLColumnMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>, MFMD extends
 * MySQLForeignKeyMetaData<MSMD, MTMD, MCMD, MFMD, MIMD>, MIMD extends
 * MySQLIndexMetaData<MSMD, MTMD, MCMD, MFMD, MIMD> > extends
 * ForeignKeyMetaData<MSMD,MTMD,MCMD,MFMD,MIMD>
 */{
}
