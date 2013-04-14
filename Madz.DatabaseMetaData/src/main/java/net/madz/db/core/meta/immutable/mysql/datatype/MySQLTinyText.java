package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLTinyText extends MySQLTextTypeBase {

    public static final String name = "TINYTEXT";

    public MySQLTinyText() {
        super();
    }

    public MySQLTinyText(Boolean isBinary, String charsetName) {
        super(isBinary, charsetName);
    }

    public MySQLTinyText(Boolean isBinary) {
        super(isBinary);
    }

    public MySQLTinyText(String charsetName, String collationName) {
        super(charsetName, collationName);
    }

    public MySQLTinyText(String collationName) {
        super(collationName);
    }

    @Override
    public String getName() {
        return name;
    }
}
