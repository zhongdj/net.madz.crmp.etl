package net.madz.db.core.impl.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import net.madz.db.core.AbsSchemaMetaDataParser;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;

public class JdbcSchemaMetaDataParserImpl extends
        AbsSchemaMetaDataParser<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {

    protected JdbcSchemaMetaData jdbcSchemaMetaData;

    public JdbcSchemaMetaDataParserImpl(String databaseName, Connection conn) {
        super(databaseName, conn);
    }

    @Override
    public JdbcSchemaMetaData parseSchemaMetaData() throws SQLException {
        try {
            JdbcSchemaMetaDataBuilderImpl builderImpl = new JdbcSchemaMetaDataBuilderImpl(databaseName);
            return builderImpl.build(conn).getMetaData();
        } finally {
            conn.close();
        }
    }
}
