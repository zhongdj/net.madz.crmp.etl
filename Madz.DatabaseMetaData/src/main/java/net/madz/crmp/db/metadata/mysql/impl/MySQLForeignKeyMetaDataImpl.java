package net.madz.crmp.db.metadata.mysql.impl;

import java.sql.SQLException;

import net.madz.crmp.db.metadata.jdbc.impl.JdbcForeignKeyMetaDataImpl;
import net.madz.crmp.db.metadata.mysql.MySQLForeignKeyMetaData;

public class MySQLForeignKeyMetaDataImpl extends JdbcForeignKeyMetaDataImpl implements MySQLForeignKeyMetaData {

    public MySQLForeignKeyMetaDataImpl(MySQLForeignKeyMetaData metaData) throws SQLException {
        super(metaData);
    }
}
