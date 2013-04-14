package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLChar extends MySQLCharBase {

    public static final String name = "CHAR";

    public MySQLChar() {
        super(1L); // default value
    }

    public MySQLChar(Long length) {
        super(length);
    }

    public MySQLChar(Long length, String charsetName, String collationName) {
        super(length, charsetName, collationName);
    }

    @Override
    public String getName() {
        return name;
    }
}
