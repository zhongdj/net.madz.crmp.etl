package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLFloat extends MySQLFloatTypeBase {

	public static final String name = "FLOAT";

	public MySQLFloat(int length, int decimals, boolean isUnsigned,
			boolean isZeroFill) {
		super(length, decimals, isUnsigned, isZeroFill);
		// TODO Auto-generated constructor stub
	}

	public MySQLFloat(int length, int decimals) {
		super(length, decimals);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return name;
	}
}
