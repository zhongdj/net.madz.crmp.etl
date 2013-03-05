package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLDateTime extends MySQLTimeTypeBase {

    public static final String name = "DATETIME";

    @Override
    public String getName() {
        return name;
    }
}
