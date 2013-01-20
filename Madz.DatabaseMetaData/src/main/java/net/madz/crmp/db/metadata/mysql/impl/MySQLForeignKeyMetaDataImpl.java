package net.madz.crmp.db.metadata.mysql.impl;

import java.sql.SQLException;

import net.madz.crmp.db.metadata.jdbc.impl.JdbcForeignKeyMetaDataImpl;
import net.madz.crmp.db.metadata.mysql.MySqlForeignKeyMetaData;

public class MySQLForeignKeyMetaDataImpl extends JdbcForeignKeyMetaDataImpl implements MySqlForeignKeyMetaData {

    public MySQLForeignKeyMetaDataImpl(MySqlForeignKeyMetaData metaData) throws SQLException {
        super(metaData);
    }
}
