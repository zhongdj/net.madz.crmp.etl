package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLDouble extends MySQLFloatTypeBase {

    public static final String name = "DOUBLE";

    public MySQLDouble(Integer length, Integer decimals, Boolean isUnsigned, Boolean isZeroFill) {
        super(length, decimals, isUnsigned, isZeroFill);
    }

    public MySQLDouble(Integer length, Integer decimals) {
        super(length, decimals);
    }

    @Override
    public String getName() {
        return name;
    }
}
