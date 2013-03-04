package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public enum MySQL_BinaryTypeEnum implements DataType {
    BINARY,
    VARBINARY;

    /** same with CHARACTER_MAXIMUM_LENGTH, CHARACTER_OCTET_LENGTH */
    private long length = 1;

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public void setColumnBuilder(MySQLColumnMetaDataBuilder builder) {
        builder.setSqlTypeName(this.name());
        builder.setCharacterMaximumLength(length);
        final StringBuilder result = new StringBuilder();
        result.append(this.name());
        result.append("(");
        result.append(length);
        result.append(")");
        builder.setColumnType(result.toString());
    }
}
