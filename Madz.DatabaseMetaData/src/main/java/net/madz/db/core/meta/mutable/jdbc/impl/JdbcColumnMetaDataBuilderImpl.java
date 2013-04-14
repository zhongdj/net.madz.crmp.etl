package net.madz.db.core.meta.mutable.jdbc.impl;

import java.sql.Connection;
import java.sql.SQLException;

import net.madz.db.core.meta.immutable.ForeignKeyEntry;
import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.impl.enums.ColumnDbMetaDataEnum;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;
import net.madz.db.core.meta.immutable.jdbc.impl.JdbcColumnMetaDataImpl;
import net.madz.db.core.meta.mutable.impl.BaseColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcTableMetaDataBuilder;

public class JdbcColumnMetaDataBuilderImpl
        extends
        BaseColumnMetaDataBuilder<JdbcSchemaMetaDataBuilder, JdbcTableMetaDataBuilder, JdbcColumnMetaDataBuilder, JdbcForeignKeyMetaDataBuilder, JdbcIndexMetaDataBuilder, JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData>
        implements JdbcColumnMetaDataBuilder {

    private Integer sqlType;
    private Integer decimalDigits;

    public JdbcColumnMetaDataBuilderImpl(JdbcTableMetaDataBuilder tableBuilder, MetaDataResultSet<ColumnDbMetaDataEnum> columnRs) throws SQLException {
        super(tableBuilder, columnRs.get(ColumnDbMetaDataEnum.COLUMN_NAME));
        this.characterOctetLength = columnRs.getLong(ColumnDbMetaDataEnum.CHAR_OCTET_LENGTH);
        this.defaultValue = columnRs.get(ColumnDbMetaDataEnum.COLUMN_DEF);
        // this.isAutoIncremented =
        // columnRs.getBoolean(ColumnDbMetaDataEnum.IS_AUTOINCREMENT);// Access
        // doesn't support
        String isNullableValue = columnRs.get(ColumnDbMetaDataEnum.IS_NULLABLE);
        if ( isNullableValue.equalsIgnoreCase("Yes") ) {
            this.isNullable = true;
        } else {
            this.isNullable = false;
        }
        this.ordinalPosition = columnRs.getShort(ColumnDbMetaDataEnum.ORDINAL_POSITION);
        this.radix = columnRs.getInt(ColumnDbMetaDataEnum.NUM_PREC_RADIX);
        this.remarks = columnRs.get(ColumnDbMetaDataEnum.REMARKS);
        this.size = columnRs.getInt(ColumnDbMetaDataEnum.COLUMN_SIZE);
        this.sqlTypeName = columnRs.get(ColumnDbMetaDataEnum.TYPE_NAME);
        this.sqlType = columnRs.getInt(ColumnDbMetaDataEnum.SQL_DATA_TYPE);
        this.decimalDigits = columnRs.getInt(ColumnDbMetaDataEnum.DECIMAL_DIGITS);
    }

    @Override
    public Integer getSqlType() {
        return this.sqlType;
    }

    @Override
    public JdbcColumnMetaDataBuilder appendForeignKeyEntry(
            ForeignKeyEntry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> entry) {
        if ( null == entry ) {
            return this;
        }
        return this.appendForeignKeyEntry(entry);
    }

    @Override
    public JdbcColumnMetaDataBuilder build(Connection conn) throws SQLException {
        return this;
    }

    @Override
    protected JdbcColumnMetaData createMetaData() {
        this.constructedMetaData = new JdbcColumnMetaDataImpl(this.tableBuilder.getMetaData(), this);
        return constructedMetaData;
    }

    @Override
    public Integer getDecimalDigits() {
        return this.decimalDigits;
    }
}
