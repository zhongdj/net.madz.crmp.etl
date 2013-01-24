package net.madz.db.core.impl.mysql;

import java.sql.Connection;

import net.madz.db.core.AbsDatabaseGenerator;
import net.madz.db.core.meta.immutable.SchemaMetaData;

public class MySQLDatabaseGenerator extends AbsDatabaseGenerator {

    public MySQLDatabaseGenerator(Connection conn) {
        super(conn);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String generateDatabase(SchemaMetaData metadata, String targetDatabaseName) {
        return null;
    }
}
