package net.madz.db.core.meta.immutable.mysql;

import net.madz.db.core.meta.immutable.mysql.datatype.MySQLIntTypeBase;

public class MySQLTinyInt extends MySQLIntTypeBase {

	public static final String name = "TINYINT";

	public MySQLTinyInt(Short displayLength, boolean isUnsigned,
			boolean isZeroFill) {
		super(displayLength, isUnsigned, isZeroFill);
	}

	public MySQLTinyInt(Short displayLength) {
		super(displayLength);
	}

	public MySQLTinyInt(Short displayLength, boolean isUnsigned) {
		super(displayLength, isUnsigned);
	}

	@Override
	public String getName() {
		return name;
	}
}
