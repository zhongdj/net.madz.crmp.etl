package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public enum MySQL_IntTypeEnum implements DataType {
    TINYINT,
    SMALLINT,
    MEDIUMINT,
    INT,
    INTEGER,
    BIGINT;

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

    @Override
    public void setColumnBuilder(MySQLColumnMetaDataBuilder builder) {
        builder.setSqlTypeName(this.name());
        builder.setUnsigned(isUnsigned);
        builder.setZeroFill(isZeroFill);
        final StringBuilder result = new StringBuilder();
        result.append(this.name());
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
