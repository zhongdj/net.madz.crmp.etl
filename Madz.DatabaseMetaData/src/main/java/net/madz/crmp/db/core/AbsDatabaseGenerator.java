package net.madz.crmp.db.core;

import com.mysql.jdbc.Connection;

import net.madz.crmp.db.metadata.SchemaMetaData;

/**
 * This class is responsible for cloning the database schema into MySQL
 * database.
 * 
 * @author tracy
 * 
 */
public abstract class AbsDatabaseGenerator {

    protected final Connection conn;

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
    abstract String generateDatabase(SchemaMetaData metadata);
}
