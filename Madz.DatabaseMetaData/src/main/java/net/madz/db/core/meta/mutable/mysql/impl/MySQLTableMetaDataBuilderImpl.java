package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLTableTypeEnum;
import net.madz.db.core.meta.immutable.type.TableType;
import net.madz.db.core.meta.mutable.impl.BaseTableMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;

public class MySQLTableMetaDataBuilderImpl
        extends
        BaseTableMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLTableMetaDataBuilder {

    private MySQLEngineEnum engine;
    private String characterSet;
    private String collation;

    public MySQLTableMetaDataBuilderImpl(MySQLSchemaMetaDataBuilder schema) {
        super(schema);
    }

    public MySQLTableMetaDataBuilder build(MetaDataResultSet<MySQLTableDbMetaDataEnum> rs, Connection conn) throws SQLException {
        this.tablePath = this.schema.getSchemaPath().append(rs.get(MySQLTableDbMetaDataEnum.TABLE_NAME));
        this.remarks = rs.get(MySQLTableDbMetaDataEnum.TABLE_COMMENT);
        this.type = TableType.convertTableType(MySQLTableTypeEnum.getType(rs.get(MySQLTableDbMetaDataEnum.TABLE_TYPE)));
        this.idCol = null;
        this.idGeneration = null;
        this.collation = rs.get(MySQLTableDbMetaDataEnum.TABLE_COLLATION);
        this.engine = MySQLEngineEnum.valueOf(rs.get(MySQLTableDbMetaDataEnum.ENGINE));
        this.characterSet = rs.get(MySQLTableDbMetaDataEnum.CHARACTER_SET_NAME);
        // Parse Columns
        Statement stmt = conn.createStatement();
        ResultSet columnsRs = stmt.executeQuery("SELECT * FROM columns WHERE table_schema='" + super.schema.getSchemaPath().getName() + "' AND table_name='"
                + this.tablePath.getName() + "';");
        MetaDataResultSet<MySQLColumnDbMetaDataEnum> colRs = new MetaDataResultSet<MySQLColumnDbMetaDataEnum>(columnsRs, MySQLColumnDbMetaDataEnum.values());
        while ( colRs.next() ) {
            //final MySQLColumnMetaDataBuilder columnBuilder = new MySQLColumnMetaDataBuilderImpl(colRs);
            //appendColumnMetaDataBuilder(columnBuilder);
        }
        // Parse Index
        // Define PK
        return this;
    }

    @Override
    public MySQLEngineEnum getEngine() {
        return this.engine;
    }

    @Override
    public String getCharacterSet() {
        return this.characterSet;
    }

    @Override
    public String getCollation() {
        return this.collation;
    }

    @Override
    public MySQLTableMetaData getMetaData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLTableMetaDataBuilder build(Connection conn) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}
