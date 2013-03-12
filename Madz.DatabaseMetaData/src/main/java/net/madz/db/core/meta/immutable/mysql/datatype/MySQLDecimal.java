package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLDecimal extends MySQLFloatTypeBase {

    public static final String name = "DECIMAL";

    public MySQLDecimal(int length, int decimals, boolean isUnsigned,
			boolean isZeroFill) {
		super(length, decimals, isUnsigned, isZeroFill);
	}

	public MySQLDecimal(int length, int decimals) {
		super(length, decimals);
	}

	@Override
    public String getName() {
        return name;
    }
}
