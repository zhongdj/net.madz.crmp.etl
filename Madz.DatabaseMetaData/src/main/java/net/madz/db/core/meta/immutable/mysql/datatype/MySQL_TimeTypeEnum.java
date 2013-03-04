package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public enum MySQL_TimeTypeEnum implements DataType {
    DATE,
    TIME,
    TIMESTAMP,
    DATETIME,
    YEAR;

    @Override
    public void setColumnBuilder(MySQLColumnMetaDataBuilder builder) {
        builder.setSqlTypeName(this.name());
        builder.setColumnType(this.name());
    }
}
