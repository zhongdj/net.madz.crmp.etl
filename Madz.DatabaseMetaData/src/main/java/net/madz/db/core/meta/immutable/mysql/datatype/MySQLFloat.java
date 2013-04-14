package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLFloat extends MySQLFloatTypeBase {

    public static final String name = "FLOAT";

    public MySQLFloat(Integer length, Integer decimals, Boolean isUnsigned, Boolean isZeroFill) {
        super(length, decimals, isUnsigned, isZeroFill);
    }

    public MySQLFloat(Integer length, Integer decimals) {
        super(length, decimals);
    }

    @Override
    public String getName() {
        return name;
    }
}
