package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLColumnTypeEnum;
import net.madz.db.core.meta.immutable.mysql.impl.MySQLColumnMetaDataImpl;
import net.madz.db.core.meta.mutable.impl.BaseColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;

public final class MySQLColumnMetaDataBuilderImpl
        extends
        BaseColumnMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLColumnMetaDataBuilder {

    private String characterSet;
    private String collation;
    private MySQLColumnTypeEnum columnType;

    public MySQLColumnMetaDataBuilderImpl(MySQLTableMetaDataBuilder tableBuilder, DottedPath columnPath) {
        super(tableBuilder, columnPath);
    }

    @Override
    public MySQLColumnMetaDataBuilder build(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM columns WHERE table_schema='" + this.tableBuilder.getTablePath().getParent().getName() + "' AND table_name='" + this.tableBuilder.getTableName() + "' AND column_name='" + this.columnPath.getName() + "';");
        while(rs.next() && rs.getRow() == 1) {
            this.ordinalPosition = rs.getShort("ordinal_position");
            this.defaultValue = rs.getString("column_default");
            this.isNullable = rs.getBoolean("is_nullable");
            this.sqlTypeName = rs.getString("data_type");
        }
        return this;
    }

    @Override
    public String getCharacterSet() {
        return characterSet;
    }

    @Override
    public String getCollation() {
        return collation;
    }

    @Override
    public MySQLColumnTypeEnum getColumnType() {
        return columnType;
    }

    @Override
    public MySQLColumnMetaData getMetaData() {
        return new MySQLColumnMetaDataImpl(this);
    }

}

