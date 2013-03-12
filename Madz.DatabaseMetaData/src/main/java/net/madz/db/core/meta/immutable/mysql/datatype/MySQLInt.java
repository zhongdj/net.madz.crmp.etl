package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLInt extends MySQLIntTypeBase {

    public static final String name = "INT";

    public MySQLInt(Short displayLength, boolean isUnsigned, boolean isZeroFill) {
		super(displayLength, isUnsigned, isZeroFill);
	}

	public MySQLInt(Short displayLength) {
		super(displayLength);
	}

	public MySQLInt(Short displayLength, boolean isUnsigned) {
		super(displayLength, isUnsigned);
	}

	@Override
    public String getName() {
        return name;
    }
}
