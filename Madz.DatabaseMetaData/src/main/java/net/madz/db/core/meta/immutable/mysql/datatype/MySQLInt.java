package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLInt extends MySQLIntTypeBase {

    public static final String name = "INT";

    public MySQLInt() {
        super();
    }

    public MySQLInt(Short displayLength, Boolean isUnsigned, Boolean isZeroFill) {
        super(displayLength, isUnsigned, isZeroFill);
    }

    public MySQLInt(Short displayLength) {
        super(displayLength);
    }

    public MySQLInt(Short displayLength, Boolean isUnsigned) {
        super(displayLength, isUnsigned);
    }

    @Override
    public String getName() {
        return name;
    }
}
