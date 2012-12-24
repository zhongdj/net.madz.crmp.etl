package net.madz.crmp.db.metadata.connection;

public interface Connection {
	Connection createConnection(String dbName);
}
