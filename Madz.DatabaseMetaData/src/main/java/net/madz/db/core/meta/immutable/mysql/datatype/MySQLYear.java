package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLYear extends MySQLTimeTypeBase {

    public static final String name = "YEAR";

    @Override
    public String getName() {
        return name;
    }
}
