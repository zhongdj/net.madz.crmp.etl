package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

/***
 * 
 * @author tracy The BIT data type is used to store bit-field values. A type of
 *         BIT(M) enables storage of M-bit values. M can range from 1 to 64.
 * 
 */
public class MySQLBit implements DataType {

    public static final String name = "BIT";
    /** length is equal to number_precision */
    private final short length;

    public MySQLBit() {
        length = 1;
    }

    public MySQLBit(short length) {
        super();
        this.length = length;
    }

    public short getLength() {
        return length;
    }

    @Override
    public void build(MySQLColumnMetaDataBuilder builder) {
        builder.setSqlTypeName(name);
        builder.setNumericPrecision((int) this.length);
        final StringBuilder result = new StringBuilder();
        result.append(name);
        result.append("(");
        result.append(length);
        result.append(")");
        builder.setColumnType(result.toString());
    }

    public String getName() {
        return name;
    }
}
