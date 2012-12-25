package net.madz.crmp.db.core;

import com.mysql.jdbc.Connection;

import net.madz.crmp.db.metadata.SchemaMetaData;

/**
 * This class is responsible for creating DatabaseMetadata instance of specified
 * database.
 * 
 * @author tracy
 * 
 */
public abstract class AbsSchemaMetaDataParser {

    protected final Connection conn;

    /**
     * @param conn
     */
    public AbsSchemaMetaDataParser(Connection conn) {
        this.conn = conn;
    }

    /**
     * @param dbName
     * @return
     */
    public abstract SchemaMetaData parseSchemaMetaData();
}
