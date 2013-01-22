package net.madz.db.metadata.mysql.impl.builder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.madz.db.metadata.jdbc.JdbcIndexMetaData;
import net.madz.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.db.metadata.jdbc.impl.builder.JdbcIndexMetaDataBuilder;
import net.madz.db.metadata.jdbc.impl.builder.JdbcTableMetaDataBuilder;
import net.madz.db.metadata.mysql.MySQLIndexMetaData;
import net.madz.db.metadata.mysql.MySQLIndexMethod;
import net.madz.db.metadata.mysql.impl.MySQLIndexMetaDataImpl;

public class MySQLIndexMetaDataBuilder extends JdbcIndexMetaDataBuilder implements MySQLIndexMetaData {

    private int subPart;
    private boolean isNull;
    private MySQLIndexMethod indexMethod;
    private Connection conn;

    public MySQLIndexMetaDataBuilder(JdbcTableMetaDataBuilder metaData, JdbcMetaDataResultSet ixRs) throws SQLException {
        super(metaData, ixRs);
        conn = metaData.getConn();
        Statement stmt = conn.createStatement();
        stmt.executeQuery("use information_schema;");
        ResultSet rs = stmt.executeQuery("select * from STATISTICS where TABLE_SCHEMA='" + metaData.getCatalogName() + "' and TABLE_NAME = '"
                + metaData.getTableName() + "' and INDEX_NAME='" + super.getIndexName() + "'");
        while ( rs.next() && rs.getRow() == 1 ) {
            subPart = rs.getInt("SUB_PART");
            isNull = rs.getBoolean("NULLABLE");
            indexMethod = MySQLIndexMethod.valueOf(rs.getString("INDEX_TYPE"));
        }
    }

    @Override
    public JdbcIndexMetaData build() {
        System.out.println("Mysql index builder");
        super.build();
        return new MySQLIndexMetaDataImpl(this);
    }

    @Override
    public int getSubPart() {
        return this.subPart;
    }

    @Override
    public boolean isNull() {
        return this.isNull;
    }

    @Override
    public MySQLIndexMethod getIndexMethod() {
        return this.indexMethod;
    }
}