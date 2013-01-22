package net.madz.db.core.impl.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import net.madz.db.core.AbsSchemaMetaDataParser;
import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;
import net.madz.db.metadata.jdbc.impl.builder.JdbcSchemaMetaDataBuilder;

public class JdbcSchemaMetaDataParserImpl extends AbsSchemaMetaDataParser {

    protected JdbcSchemaMetaData jdbcSchemaMetaData;

    public JdbcSchemaMetaDataParserImpl(String databaseName, Connection conn) {
        super(databaseName, conn);
    }

    @Override
    public JdbcSchemaMetaData parseSchemaMetaData() throws SQLException {
    	// TODO [Jan 22, 2013][barry] Use modifier final with immutable variable
        DatabaseMetaData metaData = conn.getMetaData();
        JdbcSchemaMetaDataBuilder jdbcSchemaMetaDataBuilder = new JdbcSchemaMetaDataBuilder(new DottedPath(databaseName));
        jdbcSchemaMetaDataBuilder.build(conn);
        jdbcSchemaMetaData = jdbcSchemaMetaDataBuilder.getCopy();
        return jdbcSchemaMetaData;
    }
}
