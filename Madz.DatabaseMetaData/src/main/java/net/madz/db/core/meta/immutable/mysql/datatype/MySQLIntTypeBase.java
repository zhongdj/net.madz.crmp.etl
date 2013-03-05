package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public abstract class MySQLIntTypeBase implements DataType {

    private Short displayLength;
    private boolean isUnsigned;
    private boolean isZeroFill;

    public Short getLength() {
        return displayLength;
    }

    public void setLength(Short length) {
        this.displayLength = length;
    }

    public boolean isUnsigned() {
        return isUnsigned;
    }

    public void setUnsigned(boolean unsigned) {
        this.isUnsigned = unsigned;
    }

    public boolean isZerofill() {
        return isZeroFill;
    }

    public void setZerofill(boolean zerofill) {
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
