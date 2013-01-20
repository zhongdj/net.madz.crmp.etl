package net.madz.crmp.db.core.impl.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import net.madz.crmp.db.core.AbsSchemaMetaDataParser;
import net.madz.crmp.db.metadata.DottedPath;
import net.madz.crmp.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.crmp.db.metadata.jdbc.impl.JdbcSchemaMetaDataImpl;
import net.madz.crmp.db.metadata.jdbc.impl.builder.JdbcSchemaMetaDataBuilder;
import net.madz.crmp.db.metadata.jdbc.impl.builder.JdbcTableMetaDataBuilder;

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
