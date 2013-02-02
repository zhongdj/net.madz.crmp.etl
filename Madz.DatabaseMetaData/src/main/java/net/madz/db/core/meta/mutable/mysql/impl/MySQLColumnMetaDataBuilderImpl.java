package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData.Entry;
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
import net.madz.db.utils.ResourceManagementUtils;

public final class MySQLColumnMetaDataBuilderImpl
        extends
        BaseColumnMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLColumnMetaDataBuilder {

    private String characterSet;
    // private MySQLDataTypeEnum
    private String columnType;
    private long characterMaximumLength;
    private Integer numericPrecision;
    private Integer numericScale;
    private String collationName;
    private String columnKey;
    private String extra;

    public MySQLColumnMetaDataBuilderImpl(MySQLTableMetaDataBuilder tableBuilder, DottedPath columnPath) {
        super(tableBuilder, columnPath);
    }

    @Override
    public MySQLColumnMetaDataBuilder build(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        stmt.executeQuery("use information_schema;");
        try {
            rs = stmt.executeQuery("SELECT * FROM columns WHERE table_schema='" + this.tableBuilder.getTablePath().getParent().getName() + "' AND table_name='"
                    + this.tableBuilder.getTableName() + "' AND column_name='" + this.columnPath.getName() + "';");
            while ( rs.next() ) {
                this.ordinalPosition = rs.getShort("ordinal_position");
                this.defaultValue = rs.getString("column_default");
                this.isNullable = rs.getBoolean("is_nullable");
                this.sqlTypeName = MySQLDataTypeEnum.valueOf(rs.getString("data_type").toUpperCase()).name();
                this.characterMaximumLength = rs.getLong("character_maximum_length");
                this.characterOctetLength = rs.getLong("character_octet_length");
                this.numericPrecision = rs.getInt("numeric_precision");
                this.numericScale = rs.getInt("numeric_scale");
                this.characterSet = rs.getString("character_set_name");
                this.collationName = rs.getString("collation_name");
                this.columnType = rs.getString("column_type");
                this.columnKey = rs.getString("column_key");
                this.extra = rs.getString("extra");
                if ( this.extra.equalsIgnoreCase("auto_increment") ) {
                    this.isAutoIncremented = true;
                }
                this.remarks = rs.getString("column_comment");
            }
        } finally {
            ResourceManagementUtils.closeResultSet(rs);
        }
        return this;
    }

    @Override
    public String getCharacterSet() {
        return characterSet;
    }

    @Override
    public String getColumnType() {
        return columnType;
    }

    @Override
    public long getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    @Override
    public Integer getNumericPrecision() {
        return numericPrecision;
    }

    @Override
    public Integer getNumericScale() {
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
    public MySQLColumnMetaDataImpl createMetaData() {
        this.constructedMetaData = new MySQLColumnMetaDataImpl(this.tableBuilder.getMetaData(), this);
        return (MySQLColumnMetaDataImpl) this.constructedMetaData;
    }

    @Override
    public MySQLColumnMetaDataBuilder appendForeignKeyEntry(
            Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry) {
        this.fkList.add(entry);
        return this;
    }
}
