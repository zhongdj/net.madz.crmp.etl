package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

/***
 * 
 * @author tracy The BIT data type is used to store bit-field values. A type of
 *         BIT(M) enables storage of M-bit values. M can range from 1 to 64.
 * 
 */
public class MySQL_Bit implements DataType {

    /** length is equal to number_precision */
    private short length = 1;

    public MySQL_Bit() {
    }

    public MySQL_Bit(short length) {
        super();
        this.length = length;
    }

    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    @Override
    public void setColumnBuilder(MySQLColumnMetaDataBuilder builder) {
        builder.setSqlTypeName("BIT");
        builder.setNumericPrecision((int) this.length);
        final StringBuilder result = new StringBuilder();
        result.append("BIT");
        result.append("(");
        result.append(length);
        result.append(")");
        builder.setColumnType(result.toString());
    }
}
