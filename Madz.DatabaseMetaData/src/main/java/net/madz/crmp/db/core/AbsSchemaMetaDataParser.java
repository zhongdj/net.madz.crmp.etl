package net.madz.crmp.db.core;

import java.sql.Connection;
import java.sql.SQLException;

import net.madz.crmp.db.metadata.jdbc.JdbcSchemaMetaData;

/**
 * This class is responsible for creating DatabaseMetadata instance of specified
 * database.
 * 
 * @author tracy
 * 
 */
public abstract class AbsSchemaMetaDataParser {

    protected final String databaseName;
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
