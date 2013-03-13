package net.madz.db.core.meta.mutable.mysql;

import java.util.Collection;

import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;
import net.madz.db.core.meta.mutable.TableMetaDataBuilder;

public interface MySQLTableMetaDataBuilder
        extends
        MySQLTableMetaData,
        TableMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    MySQLIndexMetaDataBuilder getIndexBuilder(String string);

    MySQLColumnMetaDataBuilder getColumnBuilder(String columnName);

    Collection<MySQLForeignKeyMetaDataBuilder> getForeignKeyBuilderSet();

    Collection<MySQLIndexMetaDataBuilder> getIndexBuilderSet();

    void setEngine(MySQLEngineEnum engine);

    void setCharacterSet(String characterSet);

    void setCollation(String collation);
    /*
     * MySQLTableMetaData<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder,
     * MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder,
     * MySQLIndexMetaDataBuilder>, TableMetaDataBuilder
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
}
