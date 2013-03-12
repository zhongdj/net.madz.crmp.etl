package net.madz.db.core.meta.immutable.mysql.datatype;


public class MySQLChar extends MySQLCharBase {

    public static final String name = "CHAR";

    public MySQLChar() {
        super(1); // default value
    }

    public MySQLChar(long length) {
        super(length);
    }

    public MySQLChar(long length, String charsetName, String collationName) {
		super(length, charsetName, collationName);
	}

	@Override
    public String getName() {
        return name;
    }

}
