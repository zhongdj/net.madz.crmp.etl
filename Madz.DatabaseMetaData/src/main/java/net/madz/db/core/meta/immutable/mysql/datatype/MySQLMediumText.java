package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLMediumText extends MySQLTextTypeBase {

    public static final String name = "MEDIUMTEXT";

    public MySQLMediumText() {
        super();
    }

    public MySQLMediumText(Boolean isBinary, String charsetName) {
        super(isBinary, charsetName);
    }

    public MySQLMediumText(Boolean isBinary) {
        super(isBinary);
    }

    public MySQLMediumText(String charsetName, String collationName) {
        super(charsetName, collationName);
    }

    public MySQLMediumText(String collationName) {
        super(collationName);
    }

    @Override
    public String getName() {
        return name;
    }
}
