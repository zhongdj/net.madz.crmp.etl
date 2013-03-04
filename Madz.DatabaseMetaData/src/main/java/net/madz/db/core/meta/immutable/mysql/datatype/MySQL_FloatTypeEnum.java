package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public enum MySQL_FloatTypeEnum implements DataType {
    REAL,
    DOUBLE,
    FLOAT,
    DECIMAL,
    NUMERIC;

    /** same to numeric_precision in columns table of information_schema */
    private int length;
    /** same to numeric_scale in columns table of information_schema */
    private int decimals;
    private boolean isUnsigned;
    private boolean isZeroFill;

    public long getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public boolean isUnsigned() {
        return isUnsigned;
    }

    public void setUnsigned(boolean isUnsigned) {
        this.isUnsigned = isUnsigned;
    }

    public boolean isZerofill() {
        return isZeroFill;
    }

    public void setZerofill(boolean isZeroFill) {
        this.isZeroFill = isZeroFill;
    }

    @Override
    public void setColumnBuilder(MySQLColumnMetaDataBuilder builder) {
        builder.setSqlTypeName(this.name());
        builder.setNumericPrecision(this.length);
        builder.setNumericScale(this.decimals);
        builder.setUnsigned(isUnsigned);
        builder.setZeroFill(isZeroFill);
        final StringBuilder result = new StringBuilder();
        result.append(this.name());
        result.append("(");
        result.append(length);
        result.append(",");
        result.append(decimals);
        result.append(")");
        if ( isUnsigned ) {
            result.append(" UNSIGNED ");
        }
        if ( isZeroFill ) {
            result.append(" ZEROFILL ");
        }
        builder.setColumnType(result.toString());
    }
}
