package net.madz.db.core.impl.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.madz.db.core.AbsSchemaMetaDataParser;
import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.mysql.impl.MySQLSchemaMetaDataImpl;
import net.madz.db.metadata.mysql.impl.builder.MySQLSchemaMetaDataBuilder;

public class MySQLSchemaMetaDataParserImpl extends AbsSchemaMetaDataParser {

    private MySQLSchemaMetaDataImpl mysqlMetaData;

    // TODO [Jan 22, 2013][barry] Reconsider resource lifecycle of conn with this class
    public MySQLSchemaMetaDataParserImpl(String databaseName, Connection conn) {
        super(databaseName, conn);
    }

    @Override
    public MySQLSchemaMetaDataImpl parseSchemaMetaData() throws SQLException {
    	// TODO [Jan 22, 2013][barry] Close Resources Connection somewhere
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet typeInfo = metaData.getTypeInfo();
        while ( typeInfo.next() ) {
            System.out.print("typeName=" + typeInfo.getString("TYPE_NAME"));
            System.out.print(",dataType=" + typeInfo.getInt("DATA_TYPE"));
            System.out.print(",precision=" + typeInfo.getInt("PRECISION"));
            System.out.print(",isCaseSensitive=" + typeInfo.getBoolean("CASE_SENSITIVE"));
            System.out.print(",isUnsignedAttribute=" + typeInfo.getBoolean("UNSIGNED_ATTRIBUTE"));
            System.out.print(",fixedPrecScale=" + typeInfo.getBoolean("FIXED_PREC_SCALE"));
            System.out.print(",isAutoIncrement=" + typeInfo.getBoolean("AUTO_INCREMENT"));
            System.out.println(",numPrecRadix=" + typeInfo.getInt("NUM_PREC_RADIX"));
        }
        MySQLSchemaMetaDataBuilder mySQLSchemaMetaDataBuilder = new MySQLSchemaMetaDataBuilder(new DottedPath(databaseName));
        mySQLSchemaMetaDataBuilder.build(conn);
        System.out.println(mySQLSchemaMetaDataBuilder.toString());
        return mySQLSchemaMetaDataBuilder.getCopy();
    }
}
