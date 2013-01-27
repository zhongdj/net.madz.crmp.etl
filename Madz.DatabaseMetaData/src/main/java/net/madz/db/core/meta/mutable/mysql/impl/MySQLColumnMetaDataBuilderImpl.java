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
import net.madz.db.core.meta.immutable.mysql.enums.MySQLDataTypeEnum;
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
    // private MySQLDataTypeEnum
    private String columnType;
    private int characterMaximumLength;
    private int numericPrecision;
    private int numericScale;
    private String collationName;
    private String columnKey;
    private String extra;
    private String columnComment;

    public MySQLColumnMetaDataBuilderImpl(MySQLTableMetaDataBuilder tableBuilder, DottedPath columnPath) {
        super(tableBuilder, columnPath);
    }

    @Override
    public MySQLColumnMetaDataBuilder build(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM columns WHERE table_schema='" + this.tableBuilder.getTablePath().getParent().getName()
                + "' AND table_name='" + this.tableBuilder.getTableName() + "' AND column_name='" + this.columnPath.getName() + "';");
        while ( rs.next() && rs.getRow() == 1 ) {
            this.ordinalPosition = rs.getShort("ordinal_position");
            this.defaultValue = rs.getString("column_default");
            this.isNullable = rs.getBoolean("is_nullable");
            this.sqlTypeName = MySQLDataTypeEnum.valueOf(rs.getString("data_type")).name();
            this.characterMaximumLength = rs.getInt("character_maximum_length");
            this.characterOctetLength = rs.getInt("character_octet_length");
            this.numericPrecision = rs.getInt("numeric_precision");
            this.numericScale = rs.getInt("numeric_scale");
            this.characterSet = rs.getString("character_set_name");
            this.collationName = rs.getString("collation_name");
            this.columnType = rs.getString("column_type");
            this.columnKey = rs.getString("column_key");
            this.extra = rs.getString("extra");
            this.columnComment = rs.getString("column_comment");
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
    public String getColumnType() {
        return columnType;
    }

    @Override
    public int getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    @Override
    public int getNumericPrecision() {
        return numericPrecision;
    }

    @Override
    public int getNumericScale() {
        return numericScale;
    }

    @Override
    public String getCollationName() {
        return collationName;
    }

    @Override
    public String getColumnKey() {
        return columnKey;
    }

    @Override
    public String getExtra() {
        return extra;
    }

    @Override
    public String getColumnComment() {
        return columnComment;
    }

    @Override
    public MySQLColumnMetaData getMetaData() {
        return new MySQLColumnMetaDataImpl(this);
    }
}
