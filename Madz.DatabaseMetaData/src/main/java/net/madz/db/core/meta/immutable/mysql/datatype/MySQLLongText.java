package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLLongText extends MySQLTextTypeBase {

    public static final String name = "LONGTEXT";

    @Override
    public String getName() {
        return name;
    }
}
