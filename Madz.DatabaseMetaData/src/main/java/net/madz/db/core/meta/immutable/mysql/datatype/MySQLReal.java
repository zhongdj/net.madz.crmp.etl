package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLReal extends MySQLFloatTypeBase {

    public static final String name = "REAL";

    public MySQLReal(Integer length, Integer decimals, Boolean isUnsigned, Boolean isZeroFill) {
        super(length, decimals, isUnsigned, isZeroFill);
    }

    public MySQLReal(Integer length, Integer decimals) {
        super(length, decimals);
    }

    @Override
    public String getName() {
        return name;
    }
}
