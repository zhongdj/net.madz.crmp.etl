package net.madz.db.core.meta.mutable.mysql;

import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.mutable.SchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.TableMetaDataBuilder;
import net.madz.db.core.meta.mutable.impl.BaseTableMetaDataBuilder;

public interface MySQLSchemaMetaDataBuilder
        extends
        MySQLSchemaMetaData,
        SchemaMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    MySQLTableMetaDataBuilder getTableBuilder(String string);

    /*
     * MySQLSchemaMetaData<MySQLSchemaMetaDataBuilder,
     * MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder,
     * MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder> ,
     * SchemaMetaDataBuilder <MySQLSchemaMetaDataBuilder,
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
/*
 * The interface SchemaMetaData cannot be implemented more than once with
 * different arguments:
 * SchemaMetaData<MySQLSchemaMetaDataBuilder,MySQLTableMetaDataBuilder
 * ,MySQLColumnMetaDataBuilder
 * ,MySQLForeignKeyMetaDataBuilder,MySQLIndexMetaDataBuilder>
 * SchemaMetaData<MySQLSchemaMetaData
 * ,MySQLTableMetaData,MySQLColumnMetaData,MySQLForeignKeyMetaData
 * ,MySQLIndexMetaData>
 */