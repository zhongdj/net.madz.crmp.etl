package net.madz.crmp.db.metadata.business;

import com.mysql.jdbc.Connection;

import net.madz.crmp.db.metadata.business.impl.DataBaseMetaData;

/**
 * This class is responsible for cloning the database metadata into mysql
 * database.
 * 
 * @author tracy
 * 
 */
public abstract class DBSchemaConstructor {
	private Connection conn;

	/**
	 * @param conn
	 */
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @param metadata
	 * @return generated db name
	 */
	public abstract String cloneDbSchema(DataBaseMetaData metadata);
}
