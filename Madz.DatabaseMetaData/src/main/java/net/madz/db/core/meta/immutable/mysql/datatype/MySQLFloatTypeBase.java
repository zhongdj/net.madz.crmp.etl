package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public abstract class MySQLFloatTypeBase implements DataType {

    /** same to numeric_precision in columns table of information_schema */
    private Integer length;
    /** same to numeric_scale in columns table of information_schema */
    private Integer decimals;
    private Boolean isUnsigned;
    private Boolean isZeroFill;

    public MySQLFloatTypeBase(Integer length, Integer decimals) {
        this(length, decimals, false, false);
    }

    public MySQLFloatTypeBase(Integer length, Integer decimals, Boolean isUnsigned, Boolean isZeroFill) {
        super();
        this.length = length;
        this.decimals = decimals;
        this.isUnsigned = isUnsigned;
        this.isZeroFill = isZeroFill;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public Boolean isUnsigned() {
        return isUnsigned;
    }

    public void setUnsigned(Boolean isUnsigned) {
        this.isUnsigned = isUnsigned;
    }

    public Boolean isZerofill() {
        return isZeroFill;
    }

    public void setZerofill(Boolean isZeroFill) {
        this.isZeroFill = isZeroFill;
    }

    public abstract String getName();

    @Override
    public void build(MySQLColumnMetaDataBuilder builder) {
        builder.setSqlTypeName(getName());
        builder.setNumericPrecision(this.length);
        builder.setNumericScale(this.decimals);
        builder.setUnsigned(isUnsigned);
        builder.setZeroFill(isZeroFill);
        final StringBuilder result = new StringBuilder();
        result.append(getName());
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
