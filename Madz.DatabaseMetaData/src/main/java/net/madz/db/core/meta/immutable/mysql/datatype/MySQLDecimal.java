package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLDecimal extends MySQLFloatTypeBase {

    public static final String name = "DECIMAL";

    @Override
    public String getName() {
        return name;
    }
}
