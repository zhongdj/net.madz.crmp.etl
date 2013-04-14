package net.madz.db.core;

import static org.junit.Assert.fail;

import java.sql.SQLException;

import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLInt;
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLVarchar;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLIndexMethod;
import net.madz.db.core.meta.immutable.types.KeyTypeEnum;
import net.madz.db.core.meta.immutable.types.TableType;
import net.madz.db.core.meta.mutable.impl.BaseForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.impl.BaseIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.impl.MySQLColumnMetaDataBuilderImpl;
import net.madz.db.core.meta.mutable.mysql.impl.MySQLForeignKeyMetaDataBuilderImpl;
import net.madz.db.core.meta.mutable.mysql.impl.MySQLIndexMetaDataBuilderImpl;
import net.madz.db.core.meta.mutable.mysql.impl.MySQLSchemaMetaDataBuilderImpl;
import net.madz.db.core.meta.mutable.mysql.impl.MySQLTableMetaDataBuilderImpl;

import org.junit.Test;

import com.gargoylesoftware.base.testing.EqualsTester;

public class MySQLSchemaMetaDataImplTest {

    @Test
    public void testEqualsObject() {
        try {
            MySQLSchemaMetaDataBuilderImpl firstSchemaBuilder = new MySQLSchemaMetaDataBuilderImpl("crmp");
            firstSchemaBuilder.setCharSet("utf8");
            firstSchemaBuilder.setCollation("utf8_bin");
            MySQLSchemaMetaDataBuilderImpl secondSchemaBuilder = new MySQLSchemaMetaDataBuilderImpl("crmp");
            secondSchemaBuilder.setCharSet("utf8");
            secondSchemaBuilder.setCollation("utf8_bin");
            MySQLSchemaMetaDataBuilderImpl thirdSchemaBuilder = new MySQLSchemaMetaDataBuilderImpl("crmp");
            thirdSchemaBuilder.setCharSet("gbk");
            thirdSchemaBuilder.setCollation("gbk_bin");
            MySQLTableMetaDataBuilderImpl first_t1 = constructTable(firstSchemaBuilder, "t1");
            MySQLTableMetaDataBuilderImpl first_t2 = constructTable(firstSchemaBuilder, "t2");
            MySQLTableMetaDataBuilderImpl second_t1 = constructTable(secondSchemaBuilder, "t1");
            MySQLTableMetaDataBuilderImpl second_t2 = constructTable(secondSchemaBuilder, "t2");
            firstSchemaBuilder.appendTableMetaDataBuilder(first_t1);
            firstSchemaBuilder.appendTableMetaDataBuilder(first_t2);
            secondSchemaBuilder.appendTableMetaDataBuilder(second_t1);
            secondSchemaBuilder.appendTableMetaDataBuilder(second_t2);
            new EqualsTester(firstSchemaBuilder.getMetaData(), secondSchemaBuilder.getMetaData(), thirdSchemaBuilder.getMetaData(), null);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public MySQLTableMetaDataBuilderImpl constructTable(MySQLSchemaMetaDataBuilderImpl firstSchemaBuilder, String tableName) {
        MySQLTableMetaDataBuilderImpl table = new MySQLTableMetaDataBuilderImpl(firstSchemaBuilder, tableName);
        table.setCharacterSet("utf8");
        table.setCollation("utf8_bin");
        table.setEngine(MySQLEngineEnum.InnoDB);
        table.setRemarks("For testing equals methods");
        table.setType(TableType.table);
        // Construct column c1
        MySQLColumnMetaDataBuilderImpl c1 = new MySQLColumnMetaDataBuilderImpl(table, "c1");
        c1.setDataType(new MySQLVarchar(20L, "utf8", "utf8_bin"));
        c1.setNullable(false);
        c1.setColumnKey("PRI");
        table.appendColumnMetaDataBuilder(c1);
        // Construct column c2
        MySQLColumnMetaDataBuilderImpl c2 = new MySQLColumnMetaDataBuilderImpl(table, "c2");
        c2.setDataType(new MySQLInt((short) 4, true, true));
        c2.setNullable(false);
        table.appendColumnMetaDataBuilder(c2);
        // Construct primary key
        MySQLIndexMetaDataBuilderImpl primaryKey = new MySQLIndexMetaDataBuilderImpl(table, "PRIMARY");
        BaseIndexMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>.Entry entry = ( (BaseIndexMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>) primaryKey ).new Entry(
                primaryKey.getMetaData(), 0, c1.getMetaData(), (short) 0);
        primaryKey.addEntry(entry);
        primaryKey.setKeyType(KeyTypeEnum.primaryKey);
        primaryKey.setIndexMethod(MySQLIndexMethod.btree);
        primaryKey.setIndexComment("primary key");
        table.appendIndexMetaDataBuilder(primaryKey);
        // Construct index
        MySQLIndexMetaDataBuilderImpl index = new MySQLIndexMetaDataBuilderImpl(table, "index_c2");
        BaseIndexMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>.Entry indexEntry = ( (BaseIndexMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>) index ).new Entry(
                index, 0, c2, (short) 0);
        index.addEntry(indexEntry);
        index.setIndexMethod(MySQLIndexMethod.btree);
        index.setIndexComment(tableName + "index_c2");
        table.appendIndexMetaDataBuilder(index);
        return table;
    }
}
