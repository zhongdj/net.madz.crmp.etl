package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLNumeric extends MySQLFloatTypeBase {

    public static final String name = "NUMERIC";

    public MySQLNumeric(int length, int decimals, boolean isUnsigned, boolean isZeroFill) {
        super(length, decimals, isUnsigned, isZeroFill);
        // TODO Auto-generated constructor stub
    }

    public MySQLNumeric(int length, int decimals) {
        super(length, decimals);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getName() {
        return name;
    }
}
