package net.madz.db.core.meta.mutable.mysql;

import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.types.KeyTypeEnum;
import net.madz.db.core.meta.mutable.IndexMetaDataBuilder;

public interface MySQLIndexMetaDataBuilder
        extends
        MySQLIndexMetaData,
        IndexMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    MySQLIndexMetaData createMetaData(MySQLTableMetaData parent);
    /*
     * MySQLIndexMetaData<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder,
     * MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder,
     * MySQLIndexMetaDataBuilder>, IndexMetaDataBuilder
     * <MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder,
     * MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder,
     * MySQLIndexMetaDataBuilder, MySQLSchemaMetaData<MySQLSchemaMetaData,
     * MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData,
     * MySQLIndexMetaData>, MySQLTableMetaData<MySQLSchemaMetaData,
     * MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData,
     * MySQLIndexMetaData>, MySQLColumnMetaData<MySQLSchemaMetaData,
     * MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData,
     * MySQLIndexMetaData>, MySQLForeignKeyMetaData<MySQLSchemaMetaData,
     * MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData,
     * MySQLIndexMetaData>, MySQLIndexMetaData<MySQLSchemaMetaData,
     * MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData,
     * MySQLIndexMetaData>> {
     */

    void setKeyType(KeyTypeEnum keyType);
}
