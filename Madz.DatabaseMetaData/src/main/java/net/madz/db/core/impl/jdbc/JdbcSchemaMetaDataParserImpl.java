package net.madz.db.core.impl.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import net.madz.db.core.AbsSchemaMetaDataParser;
import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.db.metadata.jdbc.impl.JdbcSchemaMetaDataImpl;
import net.madz.db.metadata.jdbc.impl.builder.JdbcSchemaMetaDataBuilder;
import net.madz.db.metadata.jdbc.impl.builder.JdbcTableMetaDataBuilder;

public class JdbcSchemaMetaDataParserImpl extends AbsSchemaMetaDataParser {

    protected JdbcSchemaMetaData jdbcSchemaMetaData;

    public JdbcSchemaMetaDataParserImpl(String databaseName, Connection conn) {
        super(databaseName, conn);
    }

    @Override
    public JdbcSchemaMetaData parseSchemaMetaData() throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        jdbcSchemaMetaData = new JdbcSchemaMetaDataBuilder(conn, new DottedPath(databaseName)).build();
        return jdbcSchemaMetaData;
    }
}
