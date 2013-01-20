package net.madz.crmp.db.metadata.mysql.impl.builder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.madz.crmp.db.metadata.jdbc.JdbcIndexMetaData;
import net.madz.crmp.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.crmp.db.metadata.jdbc.impl.builder.JdbcIndexMetaDataBuilder;
import net.madz.crmp.db.metadata.jdbc.impl.builder.JdbcTableMetaDataBuilder;
import net.madz.crmp.db.metadata.mysql.MySQLIndexMetaData;
import net.madz.crmp.db.metadata.mysql.MySqlIndexMethod;
import net.madz.crmp.db.metadata.mysql.impl.MySQLIndexMetaDataImpl;

public class MySQLIndexMetaDataBuilder extends JdbcIndexMetaDataBuilder implements MySQLIndexMetaData {

    private int subPart;
    private boolean isNull;
    private MySqlIndexMethod indexMethod;
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
            indexMethod = MySqlIndexMethod.valueOf(rs.getString("INDEX_TYPE"));
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
    public MySqlIndexMethod getIndexMethod() {
        return this.indexMethod;
    }
}
