package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLText extends MySQLTextTypeBase {

    public static final String name = "TEXT";

    @Override
    public String getName() {
        return name;
    }
}
