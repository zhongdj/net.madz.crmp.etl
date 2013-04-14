package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLBigInt extends MySQLIntTypeBase {

    public static final String name = "BIGINT";

    public MySQLBigInt() {
        super();
    }

    public MySQLBigInt(Short displayLength, Boolean isUnsigned, Boolean isZeroFill) {
        super(displayLength, isUnsigned, isZeroFill);
    }

    public MySQLBigInt(Short displayLength, Boolean isUnsigned) {
        super(displayLength, isUnsigned);
    }

    public MySQLBigInt(Short displayLength) {
        super(displayLength);
    }

    @Override
    public String getName() {
        return name;
    }
}
