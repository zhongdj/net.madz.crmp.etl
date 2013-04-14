package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLLongText extends MySQLTextTypeBase {

    public static final String name = "LONGTEXT";

    public MySQLLongText() {
        super();
    }

    public MySQLLongText(Boolean isBinary, String charsetName) {
        super(isBinary, charsetName);
    }

    public MySQLLongText(Boolean isBinary) {
        super(isBinary);
    }

    public MySQLLongText(String charsetName, String collationName) {
        super(charsetName, collationName);
    }

    public MySQLLongText(String collationName) {
        super(collationName);
    }

    @Override
    public String getName() {
        return name;
    }
}
