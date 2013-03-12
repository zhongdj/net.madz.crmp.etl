package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLTinyText extends MySQLTextTypeBase {

    public static final String name = "TINYTEXT";

    public MySQLTinyText() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MySQLTinyText(boolean isBinary, String charsetName) {
		super(isBinary, charsetName);
		// TODO Auto-generated constructor stub
	}

	public MySQLTinyText(boolean isBinary) {
		super(isBinary);
		// TODO Auto-generated constructor stub
	}

	public MySQLTinyText(String charsetName, String collationName) {
		super(charsetName, collationName);
		// TODO Auto-generated constructor stub
	}

	public MySQLTinyText(String collationName) {
		super(collationName);
		// TODO Auto-generated constructor stub
	}

	@Override
    public String getName() {
        return name;
    }
}
