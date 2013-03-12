package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLMediumText extends MySQLTextTypeBase {

    public static final String name = "MEDIUMTEXT";

    public MySQLMediumText() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MySQLMediumText(boolean isBinary, String charsetName) {
		super(isBinary, charsetName);
		// TODO Auto-generated constructor stub
	}

	public MySQLMediumText(boolean isBinary) {
		super(isBinary);
		// TODO Auto-generated constructor stub
	}

	public MySQLMediumText(String charsetName, String collationName) {
		super(charsetName, collationName);
		// TODO Auto-generated constructor stub
	}

	public MySQLMediumText(String collationName) {
		super(collationName);
		// TODO Auto-generated constructor stub
	}

	@Override
    public String getName() {
        return name;
    }
}
