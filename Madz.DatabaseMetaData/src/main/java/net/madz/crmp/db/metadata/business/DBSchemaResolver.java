package net.madz.crmp.db.metadata.business;

import com.mysql.jdbc.Connection;

import net.madz.crmp.db.metadata.business.impl.DataBaseMetaData;

/**
 * This class is responsible for creating DatabaseMetadata instance of specified
 * database.
 * 
 * @author tracy
 * 
 */
public abstract class DBSchemaResolver {
	private Connection conn;

	/**
	 * @param conn
	 */
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @param dbName
	 * @return
	 */
	public abstract DataBaseMetaData createDBMetadata();
}
