package net.madz.db.core.impl.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
        // TODO [Jan 22, 2013][barry][Done] Use modifier final with immutable
        // variable
        final DatabaseMetaData metaData = conn.getMetaData();
        // final JdbcSchemaMetaDataBuilder jdbcSchemaMetaDataBuilder = new
        // JdbcSchemaMetaDataBuilder(new DottedPath(databaseName));
        // jdbcSchemaMetaDataBuilder.build(conn);
        // jdbcSchemaMetaData = jdbcSchemaMetaDataBuilder.getCopy();
        return jdbcSchemaMetaData;
    }
}
