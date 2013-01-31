package net.madz.db.core.impl.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import net.madz.db.core.AbsSchemaMetaDataParser;
import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.mutable.mysql.impl.MySQLSchemaMetaDataBuilderImpl;

public class MySQLSchemaMetaDataParserImpl extends
        AbsSchemaMetaDataParser<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    // TODO [Jan 22, 2013][barry] Reconsider resource lifecycle of conn with
    // this class
    public MySQLSchemaMetaDataParserImpl(String databaseName, Connection conn) {
        super(databaseName, conn);
    }

    @Override
    public MySQLSchemaMetaData parseSchemaMetaData() throws SQLException {
        try {
            // TODO [Jan 22, 2013][barry][Done] Close Resources Connection
            // somewhere
            MySQLSchemaMetaDataBuilderImpl mySQLSchemaMetaDataBuilder = new MySQLSchemaMetaDataBuilderImpl(new DottedPath(databaseName));
            return mySQLSchemaMetaDataBuilder.build(conn).getMetaData();
        } finally {
            conn.close();
        }
    }
}
