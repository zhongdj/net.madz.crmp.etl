package net.madz.db.core.meta.mutable.jdbc.impl;

import java.sql.Connection;
import java.sql.SQLException;

import net.madz.db.core.meta.immutable.ForeignKeyMetaData.Entry;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;
import net.madz.db.core.meta.mutable.impl.BaseColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcTableMetaDataBuilder;


public class JdbcColumnMetaDataBuilderImpl extends BaseColumnMetaDataBuilder<JdbcSchemaMetaDataBuilder, JdbcTableMetaDataBuilder, JdbcColumnMetaDataBuilder, JdbcForeignKeyMetaDataBuilder, JdbcIndexMetaDataBuilder, JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> {

    public JdbcColumnMetaDataBuilderImpl(JdbcTableMetaDataBuilder tableBuilder, String name) {
        super(tableBuilder, name);
        // TODO Auto-generated constructor stub
    }

    @Override
    public JdbcColumnMetaDataBuilder appendForeignKeyEntry(
            Entry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> entry) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JdbcColumnMetaDataBuilder build(Connection conn) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected JdbcColumnMetaData createMetaData() {
        // TODO Auto-generated method stub
        return null;
    }
}
