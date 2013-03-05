package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLReal extends MySQLFloatTypeBase {

    public static final String name = "REAL";

    @Override
    public String getName() {
        return name;
    }
}
