package net.madz.db.core;

import java.sql.Connection;

import net.madz.db.metadata.jdbc.JdbcSchemaMetaData;

/**
 * This class is responsible for cloning the database schema into MySQL
 * database.
 * 
 * @author tracy
 * 
 */
public abstract class AbsDatabaseGenerator {

    protected final Connection conn;
    protected JdbcSchemaMetaData schemaMetaData;

    /**
     * @param conn
     */
    public AbsDatabaseGenerator(Connection conn) {
        this.conn = conn;
    }

    /**
     * @param metadata
     * @return generated database name
     */
    public abstract String generateDatabase(JdbcSchemaMetaData metadata, String targetDatabaseName);
}
