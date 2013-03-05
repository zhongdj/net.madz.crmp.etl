package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public interface DataType {

    void build(MySQLColumnMetaDataBuilder builder);

    String getName();
}
