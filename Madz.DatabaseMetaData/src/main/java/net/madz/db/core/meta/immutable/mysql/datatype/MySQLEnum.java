package net.madz.db.core.meta.immutable.mysql.datatype;

import java.util.List;

public class MySQLEnum extends MySQLCollectionTypeBase {

    public static final String name = "ENUM";

    public MySQLEnum(List<String> values) {
        super(values);
    }

    public MySQLEnum(List<String> values, String charset, String collation) {
        super(values, charset, collation);
    }

    @Override
    public String getName() {
        return name;
    }
}
