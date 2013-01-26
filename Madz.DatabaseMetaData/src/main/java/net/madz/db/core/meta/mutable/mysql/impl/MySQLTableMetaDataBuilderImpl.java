package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLTableTypeEnum;
import net.madz.db.core.meta.immutable.types.TableType;
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
    private final String tableName;
    private DottedPath tablePath;

    public MySQLTableMetaDataBuilderImpl(MySQLSchemaMetaDataBuilder schema, String tableName) {
        super(schema);
        this.tableName = tableName;
        tablePath = schema.getSchemaPath().append(tableName);
    }

    public MySQLTableMetaDataBuilder build(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        rs = stmt.executeQuery("SELECT * FROM tables INNER JOIN character_sets ON default_collate_name = table_collation WHERE schema_name = '"
                + super.schema.getSchemaPath().getName() + "' AND table_name='" + this.tableName + "';" );
        while ( rs.next() ) {
            this.remarks = rs.getString(MySQLTableDbMetaDataEnum.TABLE_COMMENT.name());
            this.type = TableType.convertTableType(MySQLTableTypeEnum.getType(rs.getString(MySQLTableDbMetaDataEnum.TABLE_TYPE.name())));
            this.idCol = null;
            this.idGeneration = null;
            this.collation = rs.getString(MySQLTableDbMetaDataEnum.TABLE_COLLATION.name());
            this.engine = MySQLEngineEnum.valueOf(rs.getString(MySQLTableDbMetaDataEnum.ENGINE.name()));
            this.characterSet = rs.getString(MySQLTableDbMetaDataEnum.CHARACTER_SET_NAME.name());
            // Parse Columns
            stmt = conn.createStatement();
            ResultSet columnsRs = stmt.executeQuery("SELECT * FROM columns WHERE table_schema='" + super.schema.getSchemaPath().getName()
                    + "' AND table_name='" + this.tablePath.getName() + "';");
            MetaDataResultSet<MySQLColumnDbMetaDataEnum> colRs = new MetaDataResultSet<MySQLColumnDbMetaDataEnum>(columnsRs, MySQLColumnDbMetaDataEnum.values());
            while ( colRs.next() ) {
                // final MySQLColumnMetaDataBuilder columnBuilder = new
                // MySQLColumnMetaDataBuilderImpl(colRs);
                // appendColumnMetaDataBuilder(columnBuilder);
            }
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
}
