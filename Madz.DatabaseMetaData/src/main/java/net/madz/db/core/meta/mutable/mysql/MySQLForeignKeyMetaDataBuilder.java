package net.madz.db.core.meta.mutable.mysql;

import java.sql.SQLException;

import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.mutable.ForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.impl.MySQLForeignKeyDbMetaDataEnum;

public interface MySQLForeignKeyMetaDataBuilder extends
        MySQLForeignKeyMetaData,
        // ForeignKeyMetaData<MySQLSchemaMetaDataBuilder,
        // MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder,
        // MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder>,
        ForeignKeyMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    void addEntry(MetaDataResultSet<MySQLForeignKeyDbMetaDataEnum> fkRs) throws SQLException;
    /*
     * MySQLForeignKeyMetaData<MySQLSchemaMetaDataBuilder,
     * MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder,
     * MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder>,
     * ForeignKeyMetaDataBuilder <MySQLSchemaMetaDataBuilder,
     * MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder,
     * MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder,
     * MySQLSchemaMetaData<MySQLSchemaMetaData, MySQLTableMetaData,
     * MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>,
     * MySQLTableMetaData<MySQLSchemaMetaData, MySQLTableMetaData,
     * MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>,
     * MySQLColumnMetaData<MySQLSchemaMetaData, MySQLTableMetaData,
     * MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>,
     * MySQLForeignKeyMetaData<MySQLSchemaMetaData, MySQLTableMetaData,
     * MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>,
     * MySQLIndexMetaData<MySQLSchemaMetaData, MySQLTableMetaData,
     * MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> {
     */
}
