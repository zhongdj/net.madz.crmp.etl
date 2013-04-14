package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLVarchar extends MySQLCharBase {

    public static final String name = "VARCHAR";

    public MySQLVarchar(Long length) {
        super(length);
    }

    public MySQLVarchar(Long length, String charsetName, String collationName) {
        super(length, charsetName, collationName);
    }

    @Override
    public String getName() {
        return name;
    }
}
