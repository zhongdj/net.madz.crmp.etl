package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLText extends MySQLTextTypeBase {

    public static final String name = "TEXT";

    public MySQLText() {
        super();
    }

    public MySQLText(Boolean isBinary, String charsetName) {
        super(isBinary, charsetName);
    }

    public MySQLText(Boolean isBinary) {
        super(isBinary);
    }

    public MySQLText(String charsetName, String collationName) {
        super(charsetName, collationName);
    }

    public MySQLText(String collationName) {
        super(collationName);
    }


    @Override
    public String getName() {
        return name;
    }
}
