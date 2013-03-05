package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLTime extends MySQLTimeTypeBase {

    public static final String name = "TIME";

    @Override
    public String getName() {
        return name;
    }
}
