package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public abstract class MySQLFloatTypeBase implements DataType {

    /** same to numeric_precision in columns table of information_schema */
    private int length;
    /** same to numeric_scale in columns table of information_schema */
    private int decimals;
    private boolean isUnsigned;
    private boolean isZeroFill;

    
    public MySQLFloatTypeBase(int length, int decimals) {
        this(length, decimals, false, false);
	}

	public MySQLFloatTypeBase(int length, int decimals, boolean isUnsigned,
			boolean isZeroFill) {
		super();
		this.length = length;
		this.decimals = decimals;
		this.isUnsigned = isUnsigned;
		this.isZeroFill = isZeroFill;
	}

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
