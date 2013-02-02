package net.madz.db.core.impl.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.madz.db.core.AbsDatabaseGenerator;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.utils.MessageConsts;

public class MySQLDatabaseGenerator extends
        AbsDatabaseGenerator<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    public MySQLDatabaseGenerator(Connection conn) {
        super(conn);
    }

    @Override
    public String generateDatabase(MySQLSchemaMetaData metaData, String targetDatabaseName) throws SQLException {
        this.schemaMetaData = metaData;
        if ( null == targetDatabaseName || 0 >= targetDatabaseName.length() ) {
            throw new IllegalArgumentException("Target " + MessageConsts.DATABASE_NAME_SHOULD_NOT_BE_NULL);
        }
        GenerateDatabase(conn, targetDatabaseName);
        return targetDatabaseName;
    }

    private void GenerateDatabase(Connection conn, String targetDatabaseName) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeQuery("CREATE DATABASE IF NOT EXISTS " + targetDatabaseName + " CHARACTER SET = '" + this.schemaMetaData.getCharSet() + "' COLLATE = '"
                + this.schemaMetaData.getCollation() + "';");
    }
}
