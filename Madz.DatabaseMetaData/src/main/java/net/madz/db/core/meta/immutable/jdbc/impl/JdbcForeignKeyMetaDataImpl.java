package net.madz.db.core.meta.immutable.jdbc.impl;

import net.madz.db.core.meta.immutable.impl.ForeignKeyMetaDataImpl;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;

public final class JdbcForeignKeyMetaDataImpl extends
        ForeignKeyMetaDataImpl<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> implements
        JdbcForeignKeyMetaData {

    public JdbcForeignKeyMetaDataImpl(JdbcTableMetaData parent, JdbcForeignKeyMetaData fkMetaData) {
        super(parent, fkMetaData);
    }

}
