package net.madz.db.core.impl.jdbc;

import java.sql.Connection;

import net.madz.db.core.AbsDatabaseGenerator;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;

public class JdbcDatabaseGenerator extends
        AbsDatabaseGenerator<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {

    // TODO [Jan 22, 2013][barry] Reconsider resource lifecycle
    public JdbcDatabaseGenerator(Connection conn) {
        super(conn);
    }

    @Override
    public String generateDatabase(JdbcSchemaMetaData metadata, String targetDatabaseName) {
        System.out.println("to be generated" + metadata);
        return null;
    }
}
