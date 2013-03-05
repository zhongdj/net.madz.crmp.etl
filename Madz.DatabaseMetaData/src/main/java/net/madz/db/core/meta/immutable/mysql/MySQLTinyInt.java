package net.madz.db.core.meta.immutable.mysql;

import net.madz.db.core.meta.immutable.mysql.datatype.MySQLIntTypeBase;

public class MySQLTinyInt extends MySQLIntTypeBase {

    public static final String name = "TINYINT";

    @Override
    public String getName() {
        return name;
    }
}
