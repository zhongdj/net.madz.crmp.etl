package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLDate extends MySQLTimeTypeBase {

    public static final String name = "DATE";

    @Override
    public String getName() {
        return name;
    }
}
