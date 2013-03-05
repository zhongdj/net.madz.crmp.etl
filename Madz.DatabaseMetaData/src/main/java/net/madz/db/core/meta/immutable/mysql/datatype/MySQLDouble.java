package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLDouble extends MySQLFloatTypeBase {

    public static final String name = "DOUBLE";

    @Override
    public String getName() {
        return name;
    }
}
