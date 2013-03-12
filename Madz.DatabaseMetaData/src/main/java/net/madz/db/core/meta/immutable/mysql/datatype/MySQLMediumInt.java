package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLMediumInt extends MySQLIntTypeBase {

    public static final String name = "MEDIUMINT";

    public MySQLMediumInt(Short displayLength, boolean isUnsigned,
			boolean isZeroFill) {
		super(displayLength, isUnsigned, isZeroFill);
	}

	public MySQLMediumInt(Short displayLength) {
		super(displayLength);
	}
	
	public MySQLMediumInt(Short displayLength, boolean isUnsigned) {
		super(displayLength, isUnsigned);
	}

	@Override
    public String getName() {
        return name;
    }
}
