package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLText extends MySQLTextTypeBase {

    public MySQLText() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MySQLText(boolean isBinary, String charsetName) {
        super(isBinary, charsetName);
        // TODO Auto-generated constructor stub
    }

    public MySQLText(boolean isBinary) {
        super(isBinary);
        // TODO Auto-generated constructor stub
    }

    public MySQLText(String charsetName, String collationName) {
        super(charsetName, collationName);
        // TODO Auto-generated constructor stub
    }

    public MySQLText(String collationName) {
        super(collationName);
        // TODO Auto-generated constructor stub
    }

    public static final String name = "TEXT";

    @Override
    public String getName() {
        return name;
    }
}
