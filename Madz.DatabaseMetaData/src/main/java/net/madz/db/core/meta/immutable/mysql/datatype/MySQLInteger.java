package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLInteger extends MySQLIntTypeBase {

    public static final String name = "INTEGER";

    public MySQLInteger(Short displayLength, boolean isUnsigned,
			boolean isZeroFill) {
		super(displayLength, isUnsigned, isZeroFill);
	}

	public MySQLInteger(Short displayLength) {
		super(displayLength);
	}

	public MySQLInteger(Short displayLength, boolean isUnsigned) {
		super(displayLength, isUnsigned);
	}

	@Override
    public String getName() {
        return name;
    }
}
