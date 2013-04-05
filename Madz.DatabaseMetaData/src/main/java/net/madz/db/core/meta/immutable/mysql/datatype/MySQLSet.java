package net.madz.db.core.meta.immutable.mysql.datatype;

import java.util.List;

public class MySQLSet extends MySQLCollectionTypeBase {

    public static final String name = "SET";

    public MySQLSet(List<String> values) {
        super(values);
    }

    public MySQLSet(List<String> values, String charset, String collation) {
        super(values, charset, collation);
    }

    @Override
    public String getName() {
        return name;
    }
}
