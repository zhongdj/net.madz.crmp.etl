package net.madz.db.core.impl.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import net.madz.db.core.AbsSchemaMetaDataParser;
import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.mysql.impl.MySQLSchemaMetaDataImpl;
import net.madz.db.metadata.mysql.impl.builder.MySQLSchemaMetaDataBuilder;

public class MySQLSchemaMetaDataParserImpl extends AbsSchemaMetaDataParser {

    private MySQLSchemaMetaDataImpl mysqlMetaData;

    public MySQLSchemaMetaDataParserImpl(String databaseName, Connection conn) {
        super(databaseName, conn);
    }

    @Override
    public MySQLSchemaMetaDataImpl parseSchemaMetaData() throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        MySQLSchemaMetaDataBuilder mySQLSchemaMetaDataBuilder = new MySQLSchemaMetaDataBuilder(new DottedPath(databaseName));
        mySQLSchemaMetaDataBuilder.build(conn);
        System.out.println(mySQLSchemaMetaDataBuilder.toString());
        return mySQLSchemaMetaDataBuilder.getCopy();
    }
}
