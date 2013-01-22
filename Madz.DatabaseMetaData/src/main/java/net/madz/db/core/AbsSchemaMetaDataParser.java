package net.madz.db.core;

import java.sql.Connection;
import java.sql.SQLException;

import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;

/**
 * This class is responsible for creating DatabaseMetadata instance of specified
 * database.
 * 
 * @author tracy
 * 
 */
public abstract class AbsSchemaMetaDataParser {

    protected final String databaseName;
    // TODO [Jan 22, 2013][barry] Reconsider resource lifecycle of Connection
    protected final Connection conn;

    /**
     * @param conn
     */
    public AbsSchemaMetaDataParser(String databaseName, Connection conn) {
        this.databaseName = databaseName;
        this.conn = conn;
    }

    /**
     * @param dbName
     * @return
     */
    public abstract JdbcSchemaMetaData parseSchemaMetaData() throws SQLException;
}
