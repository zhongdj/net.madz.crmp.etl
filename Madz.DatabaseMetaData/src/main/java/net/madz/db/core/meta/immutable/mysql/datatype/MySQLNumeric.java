package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLNumeric extends MySQLFloatTypeBase {

    public static final String name = "NUMERIC";

    @Override
    public String getName() {
        return name;
    }
}
