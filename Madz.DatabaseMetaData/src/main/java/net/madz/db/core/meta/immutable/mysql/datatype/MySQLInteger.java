package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLInteger extends MySQLIntTypeBase {

    public static final String name = "INTEGER";

    public MySQLInteger() {
        super();
    }

    public MySQLInteger(Short displayLength, Boolean isUnsigned, Boolean isZeroFill) {
        super(displayLength, isUnsigned, isZeroFill);
    }

    public MySQLInteger(Short displayLength) {
        super(displayLength);
    }

    public MySQLInteger(Short displayLength, Boolean isUnsigned) {
        super(displayLength, isUnsigned);
    }

    @Override
    public String getName() {
        return name;
    }
}
