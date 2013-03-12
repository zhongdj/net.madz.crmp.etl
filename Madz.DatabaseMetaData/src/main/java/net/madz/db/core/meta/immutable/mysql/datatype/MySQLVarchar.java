package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLVarchar extends MySQLCharBase {

	public static final String name = "VARCHAR";

	public MySQLVarchar(long length) {
		super(length);
	}

	public MySQLVarchar(long length, String charsetName, String collationName) {
		super(length, charsetName, collationName);
	}

	@Override
	public String getName() {
		return name;
	}

}
