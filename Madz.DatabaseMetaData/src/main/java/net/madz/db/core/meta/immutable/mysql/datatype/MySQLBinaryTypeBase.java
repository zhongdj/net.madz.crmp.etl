package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public abstract class MySQLBinaryTypeBase implements DataType {

    public abstract long getLength();

    @Override
    public void build(MySQLColumnMetaDataBuilder builder) {
        builder.setSqlTypeName(getName());
        builder.setCharacterMaximumLength(getLength());
        final StringBuilder result = new StringBuilder();
        result.append(getName());
        if ( getLength() > 0 ) {
            result.append("(");
            result.append(getLength());
            result.append(")");
        }
        builder.setColumnType(result.toString());
    }
}
