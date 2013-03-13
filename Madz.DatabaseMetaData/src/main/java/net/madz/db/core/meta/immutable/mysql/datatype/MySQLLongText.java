package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLLongText extends MySQLTextTypeBase {

    public static final String name = "LONGTEXT";

    public MySQLLongText() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MySQLLongText(boolean isBinary, String charsetName) {
        super(isBinary, charsetName);
        // TODO Auto-generated constructor stub
    }

    public MySQLLongText(boolean isBinary) {
        super(isBinary);
        // TODO Auto-generated constructor stub
    }

    public MySQLLongText(String charsetName, String collationName) {
        super(charsetName, collationName);
        // TODO Auto-generated constructor stub
    }

    public MySQLLongText(String collationName) {
        super(collationName);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getName() {
        return name;
    }
}
