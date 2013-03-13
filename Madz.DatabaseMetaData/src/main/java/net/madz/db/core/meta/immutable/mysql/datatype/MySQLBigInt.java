package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLBigInt extends MySQLIntTypeBase {

    public static final String name = "BIGINT";

    public MySQLBigInt(Short displayLength, boolean isUnsigned, boolean isZeroFill) {
        super(displayLength, isUnsigned, isZeroFill);
    }

    public MySQLBigInt(Short displayLength, boolean isUnsigned) {
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
