package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public abstract class MySQLIntTypeBase implements DataType {

    private Short displayLength = 0;
    private Boolean isUnsigned = false;
    private Boolean isZeroFill = false;

    public MySQLIntTypeBase() {
    }

    public MySQLIntTypeBase(Short displayLength) {
        this(displayLength, false, false);
    }

    public MySQLIntTypeBase(Short displayLength, Boolean isUnsigned) {
        this(displayLength, isUnsigned, false);
    }

    public MySQLIntTypeBase(Short displayLength, Boolean isUnsigned, Boolean isZeroFill) {
        super();
        this.displayLength = displayLength;
        this.isUnsigned = isUnsigned;
        this.isZeroFill = isZeroFill;
    }

    public Short getLength() {
        return displayLength;
    }

    public void setLength(Short length) {
        this.displayLength = length;
    }

    public boolean isUnsigned() {
        return isUnsigned;
    }

    public void setUnsigned(Boolean unsigned) {
        this.isUnsigned = unsigned;
    }

    public boolean isZerofill() {
        return isZeroFill;
    }

    public void setZerofill(Boolean zerofill) {
        this.isZeroFill = zerofill;
    }

    public abstract String getName();

    @Override
    public void build(MySQLColumnMetaDataBuilder builder) {
        builder.setSqlTypeName(getName());
        builder.setUnsigned(isUnsigned);
        builder.setZeroFill(isZeroFill);
        final StringBuilder result = new StringBuilder();
        result.append(getName());
        if ( 0 < displayLength ) {
            result.append("(");
            result.append(displayLength);
            result.append(")");
        }
        if ( isUnsigned ) {
            result.append(" UNSIGNED ");
        }
        if ( isZeroFill ) {
            result.append(" ZEROFILL ");
        }
        builder.setColumnType(result.toString());
    }
}
