package net.madz.db.core.impl

import java.sql.Connection
import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.immutable.List
import scala.slick.session.Database
import org.scalatest.Assertions
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSpec
import net.madz.db.core.impl.mysql.MySQLSchemaMetaDataParserImpl
import net.madz.db.core.meta.DottedPath
import net.madz.db.core.meta.immutable.IndexMetaData
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum
import net.madz.db.core.meta.immutable.mysql.enums.MySQLIndexMethod
import net.madz.db.core.meta.immutable.types.IndexTypeEnum
import net.madz.db.core.meta.immutable.types.KeyTypeEnum
import net.madz.db.core.meta.immutable.types.SortDirectionEnum
import net.madz.db.core.meta.immutable.types.TableType
import net.madz.db.core.meta.immutable.ForeignKeyMetaData

class MySQLSchemaMetaDataParserTest extends FunSpec with BeforeAndAfterEach with MySQLCommandLine {

  var conn: Connection = null
  var parser: MySQLSchemaMetaDataParserImpl = null

  override def beforeEach {
    exec(drop_database_query :: create_database_query :: Nil)
    conn = Database.forURL(urlRoot + database_name, user, password, prop).createSession.conn
    parser = new MySQLSchemaMetaDataParserImpl(database_name, conn)
  }

  override def afterEach {
    exec(drop_database_query)
    if (null != conn) conn.close()
  }

  describe("Parse an Empty Database") {
    it("should parse an empty database") {

      val result = parser.parseSchemaMetaData()
      Assertions.expectResult(new DottedPath(database_name))(result getSchemaPath)
      Assertions.expectResult(0)(result.getTables size)
    }

    it("should parse an empty database with correction database level default character set and default collation configuration") {

      exec(
        drop_database_query
          :: """
           CREATE DATABASE `madz_database_parser_test`DEFAULT CHARACTER SET = `gbk` DEFAULT COLLATE = `gbk_chinese_ci`;
           """
          :: Nil)

      val result = parser.parseSchemaMetaData()

      Assertions.expectResult(new DottedPath(database_name))(result getSchemaPath)
      Assertions.expectResult(0)(result.getTables size)
      Assertions.expectResult("gbk")(result getCharSet)
      Assertions.expectResult("gbk_chinese_ci")(result getCollation)

    }

  }

  describe("Parse a single table Database") {

    it("should parse correct tableName, charsetEncoding, collation, and storage engine with MySQL Parser") {
      exec(
        drop_database_query
          :: """
           CREATE DATABASE `madz_database_parser_test`DEFAULT CHARACTER SET = `gbk` DEFAULT COLLATE = `gbk_chinese_ci`;
           """
          :: "USE `madz_database_parser_test`;"
          :: """
           CREATE TABLE `single_table_test` (
              name VARCHAR(64) DEFAULT NULL
           ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
           """
          :: Nil)

      val result = parser.parseSchemaMetaData()

      Assertions.expectResult(1)(result.getTables() size)
      val table = result.getTables().toArray[MySQLTableMetaData](Array[MySQLTableMetaData]())(0)
      Assertions.expectResult("single_table_test")(table getTableName)
      Assertions.expectResult(MySQLEngineEnum.InnoDB)(table.getEngine)
      Assertions.expectResult("utf8" toLowerCase)(table getCharacterSet)
      Assertions.expectResult("utf8_unicode_ci" toLowerCase)(table getCollation)
      Assertions.expectResult(TableType.table)(table getType)
    }

  }

  describe("Parse columns from a single table Database") {
    it("should parse all kinds of data types defined in MySQL documents") {
      exec(
        "USE `madz_database_parser_test`;"
          :: create_table_with_all_data_types_DDL_1
          :: create_table_with_all_data_types_DDL_2
          :: create_table_with_all_data_types_DDL_3
          :: create_table_with_all_data_types_DDL_4
          :: create_table_with_all_data_types_DDL_5
          :: Nil)

      val result = parser.parseSchemaMetaData();

      val table1 = result.getTable("table_with_all_data_types_p1")
      val table2 = result.getTable("table_with_all_data_types_p2")
      val table3 = result.getTable("table_with_all_data_types_p3")
      val table4 = result.getTable("table_with_all_data_types_p4")
      val table5 = result.getTable("table_with_all_data_types_p5")
      Assertions.expectResult(31)(table1.getColumns size)
      Assertions.expectResult(1)(table2.getColumns size)
      Assertions.expectResult(10)(table3.getColumns size)
      Assertions.expectResult(1)(table4.getColumns size)
      Assertions.expectResult(1)(table5.getColumns size)

      verifyColumns(columns_in_table1, table1.getColumns)
      verifyColumns(columns_in_table2, table2.getColumns)
      verifyColumns(columns_in_table3, table3.getColumns)
      verifyColumns(columns_in_table4, table4.getColumns)
      verifyColumns(columns_in_table5, table5.getColumns)

    }

    it("should parse column with column option NULLABLE") {
      exec(
        """
           USE `madz_database_parser_test`;
        """
          :: """
          CREATE TABLE `table_with_nullable_option` (
             `nullable_column_name` VARCHAR(32) NULL,
             `not_nullable_column_name` VARCHAR(32) NOT NULL
           ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
          """
          :: Nil)

      val result = parser parseSchemaMetaData

      Assertions.expectResult(true)(result.getTable("table_with_nullable_option").getColumn("nullable_column_name").isNullable())
      Assertions.expectResult(false)(result.getTable("table_with_nullable_option").getColumn("not_nullable_column_name").isNullable())

    }

    it("should parse column with column option DEFAULT") {
      exec(
        """
           USE `madz_database_parser_test`;
          """
          :: """
           CREATE TABLE `table_with_default_option` (
             `defaulted_column_name` VARCHAR(32) DEFAULT 'Hello',
             `not_defaulted_column_name` VARCHAR(32) NOT NULL
           ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
          """ :: Nil)

      val result = parser parseSchemaMetaData

      Assertions.expectResult("Hello")(result.getTable("table_with_default_option").getColumn("defaulted_column_name").getDefaultValue())
      Assertions.expectResult(null)(result.getTable("table_with_default_option").getColumn("not_defaulted_column_name").getDefaultValue())
    }

    it("should parse column with column comment") {
      exec(
        """
           USE `madz_database_parser_test`;
          """ :: """
           CREATE TABLE `table_with_column_comment` (
             `column_comment_column` VARCHAR(32) COMMENT 'should have a comment',
             `no_column_comment_column` VARCHAR(32) 
           ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
          """ :: Nil)

      val result = parser parseSchemaMetaData

      Assertions.expectResult("should have a comment")(result.getTable("table_with_column_comment").getColumn("column_comment_column").getRemarks)
      Assertions.expectResult("")(result.getTable("table_with_column_comment").getColumn("no_column_comment_column").getRemarks)

    }

  }

  describe("Parse indexes from Database") {
    it("should parse single column PK index") {
      exec(
        """
           USE `madz_database_parser_test`;
          """ :: """
           CREATE TABLE `table_with_single_column_pk` (
             `single_column_pk` VARCHAR(32) CHARACTER SET `latin7` COLLATE `latin7_general_ci` PRIMARY KEY,
             `common_column` VARCHAR(32) 
           ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
          """
          :: Nil)

      /* 
	  """
	  mysql> select * from statistics where table_schema = 'madz_database_parser_test';
	  +---------------+---------------------------+-----------------------------+------------+---------------------------+------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
	  | TABLE_CATALOG | TABLE_SCHEMA              | TABLE_NAME                  | NON_UNIQUE | INDEX_SCHEMA              | INDEX_NAME | SEQ_IN_INDEX | COLUMN_NAME      | COLLATION | CARDINALITY | SUB_PART | PACKED | NULLABLE | INDEX_TYPE | COMMENT | INDEX_COMMENT |
	  +---------------+---------------------------+-----------------------------+------------+---------------------------+------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
	  | def           | madz_database_parser_test | table_with_single_column_pk |          0 | madz_database_parser_test | PRIMARY    |            1 | single_column_pk | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
	  +---------------+---------------------------+-----------------------------+------------+---------------------------+------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
	  1 row in set (0.01 sec)
	  """
      */
      val result = parser parseSchemaMetaData
      val pk = result.getTable("table_with_single_column_pk").getPrimaryKey()
      val column = result.getTable("table_with_single_column_pk").getColumn("single_column_pk")
      Assertions.expectResult(MySQLIndexMethod.btree)(pk getIndexMethod)
      Assertions.expectResult(true)(pk isUnique)
      Assertions.expectResult(0)(pk getCardinality)
      Assertions.expectResult("PRIMARY")(pk getIndexName)
      Assertions.expectResult(IndexTypeEnum.clustered)(pk getIndexType)
      Assertions.expectResult(KeyTypeEnum.primaryKey)(pk getKeyType)
      Assertions.expectResult(0 /*"unknown"*/ )(pk getPageCount)
      Assertions.expectResult(SortDirectionEnum.ascending)(pk getSortDirection)
      Assertions.expectResult("table_with_single_column_pk")(pk.getTable getTableName)
      Assertions.expectResult(false)(pk.isNull)
      Assertions.expectResult(true)(pk.containsColumn(column))
      Assertions.expectResult(true)(column.isMemberOfPrimaryKey)
      val pkEntry = collectionAsScalaIterable(pk.getEntrySet).toList.get(0)
      Assertions.expectResult(pkEntry.getColumn)(column)
      Assertions.expectResult(pkEntry.getKey)(pk)
      Assertions.expectResult(pkEntry.getKey.getIndexName)(pk.getIndexName)
      Assertions.expectResult(pkEntry.getColumn.getColumnName)(column.getColumnName)
      Assertions.expectResult(1)(pkEntry.getPosition)
    }

    it("should parse auto incremental index") {
      exec(
        """
           USE `madz_database_parser_test`;
          """ :: """
           CREATE TABLE `table_with_auto_incremental_index` (
             `single_column_pk` INTEGER(32) AUTO_INCREMENT PRIMARY KEY,
             `common_column` VARCHAR(32) 
           ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
          """ :: Nil)

      /*
          mysql> select * from statistics where table_schema = 'madz_database_parser_test';
          +---------------+---------------------------+-----------------------------------+------------+---------------------------+------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
          | TABLE_CATALOG | TABLE_SCHEMA              | TABLE_NAME                        | NON_UNIQUE | INDEX_SCHEMA              | INDEX_NAME | SEQ_IN_INDEX | COLUMN_NAME      | COLLATION | CARDINALITY | SUB_PART | PACKED | NULLABLE | INDEX_TYPE | COMMENT | INDEX_COMMENT |
          +---------------+---------------------------+-----------------------------------+------------+---------------------------+------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
          | def           | madz_database_parser_test | table_with_auto_incremental_index |          0 | madz_database_parser_test | PRIMARY    |            1 | single_column_pk | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
          +---------------+---------------------------+-----------------------------------+------------+---------------------------+------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
          1 row in set (0.00 sec)
          
          mysql> select * from columns where table_schema = 'madz_database_parser_test' and table_name = 'table_with_auto_incremental_index'\G
		*************************** 1. row ***************************
		           TABLE_CATALOG: def
		            TABLE_SCHEMA: madz_database_parser_test
		              TABLE_NAME: table_with_single_column_pk
		             COLUMN_NAME: single_column_pk
		        ORDINAL_POSITION: 1
		          COLUMN_DEFAULT: NULL
		             IS_NULLABLE: NO
		               DATA_TYPE: int
		CHARACTER_MAXIMUM_LENGTH: NULL
		  CHARACTER_OCTET_LENGTH: NULL
		       NUMERIC_PRECISION: 10
		           NUMERIC_SCALE: 0
		      CHARACTER_SET_NAME: NULL
		          COLLATION_NAME: NULL
		             COLUMN_TYPE: int(32)
		              COLUMN_KEY: PRI
		                   EXTRA: auto_increment
		              PRIVILEGES: select,insert,update,references
		          COLUMN_COMMENT: 
        */
      val result = parser parseSchemaMetaData
      val pk = result.getTable("table_with_auto_incremental_index").getPrimaryKey()
      Assertions.expectResult("table_with_auto_incremental_index")(pk.getTable.getTableName)
      Assertions.expectResult("PRIMARY")(pk getIndexName)
      Assertions.expectResult(SortDirectionEnum.ascending)(pk getSortDirection)
      Assertions.expectResult(true)(pk isUnique)
      Assertions.expectResult(null)(pk isNull)
      Assertions.expectResult(0)(pk getCardinality)
      Assertions.expectResult(IndexTypeEnum.clustered)(pk getIndexType)
      Assertions.expectResult(KeyTypeEnum.primaryKey)(pk getKeyType)

      val column = result.getTable("table_with_auto_incremental_index").getColumn("single_column_pk")
      Assertions.expectResult(true)(column isAutoIncremented)
      Assertions.expectResult(true)(column isMemberOfPrimaryKey)
      Assertions.expectResult(true)(column isMemberOfIndex)
      Assertions.expectResult(true)(column isMemberOfUniqueIndex)
      Assertions.expectResult(false)(column isNullable)
      Assertions.expectResult(null)(column getCollationName)

    }

    it("should parse single column PK with descending sort order") {
      /*
       * An index_col_name specification can end with ASC or DESC. 
       * These keywords are permitted for future extensions for specifying ascending or descending index value storage. 
       * Currently, they are parsed but ignored; index values are always stored in ascending order.
       * 
       * Refer to: http://dev.mysql.com/doc/refman/5.5/en/create-table.html
       */

      exec(
        """
           USE `madz_database_parser_test`;
          """ :: """
           CREATE TABLE `table_with_not_supported_descending_order` (
             `single_column_pk` VARCHAR(32),
             `common_column` VARCHAR(32),
              PRIMARY KEY (single_column_pk DESC) 
           ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
          """ :: Nil)

      val result = parser parseSchemaMetaData
      val pk = result.getTable("table_with_not_supported_descending_order") getPrimaryKey

      Assertions.expectResult(SortDirectionEnum.ascending)(pk getSortDirection)

    }

    it("should parse index type with BTREE mode") {
      exec(
        """
           USE `madz_database_parser_test`;
          """ :: """
           CREATE TABLE `table_with_btree_index` (
             `single_column_pk` VARCHAR(32),
             `common_column` VARCHAR(32),
              PRIMARY KEY USING BTREE (single_column_pk) 
           ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
          """ :: Nil)

      val result = parser parseSchemaMetaData
      val pk = result.getTable("table_with_btree_index") getPrimaryKey

      Assertions.expectResult(MySQLIndexMethod.btree)(pk getIndexMethod)
    }

    it("should parse index type with HASH mode") {
      /*
       * index type is storage engine related, such as following:  
       * Engine			Permissible Index Types
       * --------------------------------
       * MyISAM			BTREE
       * InnoDB			BTREE
       * MEMORY/HEAP	HASH, BTREE
       * NDB			BTREE, HASH 
       * --------------------------------
       */

      exec(
        """
           USE `madz_database_parser_test`;
          """ :: """
           CREATE TABLE `table_with_hash_index` (
             `single_column_pk` VARCHAR(32),
             `common_column` VARCHAR(32),
              PRIMARY KEY (single_column_pk) USING HASH  
           ) ENGINE=`MEMORY` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
          """ :: Nil)

      val result = parser parseSchemaMetaData
      val pk = result.getTable("table_with_hash_index") getPrimaryKey

      Assertions.expectResult(MySQLIndexMethod.hash)(pk getIndexMethod)
    }

    it("should parse nullable index") {

      exec(
        """
           USE `madz_database_parser_test`;
          """ :: """
           CREATE TABLE `table_with_nullable_index` (
             `single_column_pk` VARCHAR(32),
             `common_column` VARCHAR(32) NULL,
              PRIMARY KEY (single_column_pk),
              INDEX common_column_index (common_column)
           ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
          """ :: Nil)
      /*
	mysql> select * from statistics where table_schema = 'test';
	+---------------+--------------+---------------------------+------------+--------------+---------------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
	| TABLE_CATALOG | TABLE_SCHEMA | TABLE_NAME                | NON_UNIQUE | INDEX_SCHEMA | INDEX_NAME          | SEQ_IN_INDEX | COLUMN_NAME      | COLLATION | CARDINALITY | SUB_PART | PACKED | NULLABLE | INDEX_TYPE | COMMENT | INDEX_COMMENT |
	+---------------+--------------+---------------------------+------------+--------------+---------------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
	| def           | test         | table_with_nullable_index |          0 | test         | PRIMARY             |            1 | single_column_pk | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
	| def           | test         | table_with_nullable_index |          1 | test         | common_column_index |            1 | common_column    | A         |           0 |     NULL | NULL   | YES      | BTREE      |         |               |
	+---------------+--------------+---------------------------+------------+--------------+---------------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
	2 rows in set (0.00 sec)
	*/

      val result = parser parseSchemaMetaData
      val index = result.getTable("table_with_nullable_index") getIndex ("common_column_index")
      val nullable_column = result.getTable("table_with_nullable_index").getColumn("common_column")
      Assertions.expectResult(1)(nullable_column.getNonUniqueIndexSet size)
      Assertions.expectResult(1)(nullable_column.getUniqueIndexSet size)

      val non_unique_index_entry = collectionAsScalaIterable[IndexMetaData.Entry[MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData]](nullable_column getNonUniqueIndexSet).toList(0)

      Assertions.expectResult(nullable_column)(non_unique_index_entry getColumn)
      Assertions.expectResult(index)(non_unique_index_entry getKey)
      Assertions.expectResult(1)(non_unique_index_entry getPosition)
      Assertions.expectResult(false)(index isUnique)
      Assertions.expectResult(true)(index isNull)
    }

    it("should parse composite PK with multiple columns") {
      exec(
        """
          USE `madz_database_parser_test`;
          """ :: """
          CREATE TABLE `table_with_composite_pk` (
            `pk_column_part_1` INTEGER(32) NOT NULL,
            `pk_column_part_2` CHAR(12) NOT NULL,
            `pk_column_part_3` INTEGER(32) NOT NULL,
            `data_column_1` VARCHAR(64) NULL,
            PRIMARY KEY (`pk_column_part_1`,`pk_column_part_2`, `pk_column_part_3`) 
          ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_bin`
          """
          :: Nil)

      /*
		mysql> select * from statistics where table_schema = 'madz_database_parser_test';
		+---------------+---------------------------+---------------------------+------------+--------------+---------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
		| TABLE_CATALOG | TABLE_SCHEMA              | TABLE_NAME                | NON_UNIQUE | INDEX_SCHEMA | INDEX_NAME    | SEQ_IN_INDEX | COLUMN_NAME      | COLLATION | CARDINALITY | SUB_PART | PACKED | NULLABLE | INDEX_TYPE | COMMENT | INDEX_COMMENT |
		+---------------+---------------------------+---------------------------+------------+--------------+---------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
		| def           | madz_database_parser_test | table_with_composite_pk   |          0 | test         | PRIMARY       |            1 | pk_column_part_1 | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
		| def           | madz_database_parser_test | table_with_composite_pk   |          0 | test         | PRIMARY       |            2 | pk_column_part_2 | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
		| def           | madz_database_parser_test | table_with_composite_pk   |          0 | test         | PRIMARY       |            3 | pk_column_part_3 | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
		+---------------+---------------------------+---------------------------+------------+--------------+---------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
		6 rows in set (0.00 sec)
       * 
       */
      val result = parser parseSchemaMetaData

      val table = result getTable "table_with_composite_pk"
      val pk: MySQLIndexMetaData = table getPrimaryKey
      val pk_column_part_1 = table getColumn "pk_column_part_1"
      val pk_column_part_2 = table getColumn "pk_column_part_2"
      val pk_column_part_3 = table getColumn "pk_column_part_3"
      val data_columnt_1 = table getColumn "data_column_1"

      Assertions.expectResult(MySQLIndexMethod.btree)(pk getIndexMethod)
      //??
      Assertions.expectResult(false)(pk.isNull)
      Assertions.expectResult(true)(pk.containsColumn(pk_column_part_1))
      Assertions.expectResult(true)(pk.containsColumn(pk_column_part_2))
      Assertions.expectResult(true)(pk.containsColumn(pk_column_part_3))
      Assertions.expectResult(false)(pk.containsColumn(data_columnt_1))
      Assertions.expectResult(0)(pk getCardinality)
      Assertions.expectResult("PRIMARY")(pk.getIndexName())
      Assertions.expectResult(KeyTypeEnum.primaryKey)(pk.getKeyType())
      //pk.getPageCount()
      Assertions.expectResult(SortDirectionEnum.ascending)(pk.getSortDirection)
      Assertions.expectResult(table)(pk.getTable)
      Assertions.expectResult(table.getTableName)(pk.getTable.getTableName)
      Assertions.expectResult(true)(pk.isUnique)
      val indexEntryList: List[IndexMetaData.Entry[MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData]] = collectionAsScalaIterable[IndexMetaData.Entry[MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData]](pk.getEntrySet) toList

      Assertions.expectResult(3)(indexEntryList.length)
      val column_vs_entry = List(pk_column_part_1, pk_column_part_2, pk_column_part_3) zip indexEntryList
      column_vs_entry.foreach(pair => {
        Assertions.expectResult(pair._1)(pair._2 getColumn)
        Assertions.expectResult(pk)(pair._2 getKey)
        Assertions.expectResult(null)(pair._2.getSubPart)
        //Tricky part
        Assertions.expectResult(pair._1.getOrdinalPosition)(pair._2 getPosition)
      })
    }

    it("should parse single column UNIQUE KEY") {
      exec(
        """
          USE `madz_database_parser_test`;
          """ :: """
          CREATE TABLE `table_with_single_column_unique_key` (
            `id` INTEGER(32) AUTO_INCREMENT PRIMARY KEY,
            `email` VARCHAR(64) NOT NULL,
            `nickname` VARCHAR(80) NULL,
            `phone` VARCHAR(20) NULL,
             UNIQUE KEY email_index (email),
             INDEX nickname_index (`nickname`)
          ) ENGINE = `InnoDB` DEFAULT CHARACTER SET = 'utf8' DEFAULT COLLATE = 'utf8_bin';
          """ :: Nil)

      val result = parser parseSchemaMetaData
      val table = result.getTable("table_with_single_column_unique_key")
      val pk = table.getColumn("id")
      val unique_key_column = table.getColumn("email")
      val non_unique_index_column = table.getColumn("nickname")

      val email_index = table.getIndex("email_index");
      val nickname_index = table.getIndex("nickname_index");
      Assertions.expectResult(true)(email_index.isUnique)
      Assertions.expectResult(false)(nickname_index.isUnique)
      Assertions.expectResult(true)(email_index.isNull)
      Assertions.expectResult(false)(nickname_index.isNull)

    }

    it("should parse composite UNIQUE KEY with multiple columns") {
      exec("""
          USE `madz_database_parser_test`;
          """ :: """
          CREATE TABLE `table_with_composite_unique_key` (
            `id` INTEGER(32) AUTO_INCREMENT PRIMARY KEY,
            `unique_key_part_1` INTEGER(32) NOT NULL,
            `unique_key_part_2` VARCHAR(32) NOT NULL,
            UNIQUE KEY `composite_unique_key` (`unique_key_part_1`, `unique_key_part_2`)
          ) ENGINE = `InnoDB` DEFAULT CHARACTER SET = 'utf8' DEFAULT COLLATE='utf8_bin'; 
          """ :: Nil)

      /*
mysql> select * from statistics where table_name = 'table_with_composite_unique_key';
+---------------+--------------+---------------------------------+------------+--------------+----------------------+--------------+-------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
| TABLE_CATALOG | TABLE_SCHEMA | TABLE_NAME                      | NON_UNIQUE | INDEX_SCHEMA | INDEX_NAME           | SEQ_IN_INDEX | COLUMN_NAME       | COLLATION | CARDINALITY | SUB_PART | PACKED | NULLABLE | INDEX_TYPE | COMMENT | INDEX_COMMENT |
+---------------+--------------+---------------------------------+------------+--------------+----------------------+--------------+-------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
| def           | test         | table_with_composite_unique_key |          0 | test         | PRIMARY              |            1 | id                | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
| def           | test         | table_with_composite_unique_key |          0 | test         | composite_unique_key |            1 | unique_key_part_1 | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
| def           | test         | table_with_composite_unique_key |          0 | test         | composite_unique_key |            2 | unique_key_part_2 | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
+---------------+--------------+---------------------------------+------------+--------------+----------------------+--------------+-------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
3 rows in set (0.00 sec)
 */

      val schema = parser parseSchemaMetaData
      val table = schema.getTable("table_with_composite_unique_key")
      val composite_unique_key = table.getIndex("composite_unique_key");
      Assertions.expectResult(true)(composite_unique_key isUnique)
      val entries = composite_unique_key.getEntrySet.toArray(Array[IndexMetaData.Entry[MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData]]())
      Assertions.expectResult(table.getColumn("unique_key_part_1"))(entries(0).getColumn())
      Assertions.expectResult(table.getColumn("unique_key_part_2"))(entries(1).getColumn())
      Assertions.expectResult(composite_unique_key)(entries(0).getKey)
      Assertions.expectResult(composite_unique_key)(entries(1).getKey)
      Assertions.expectResult(1)(entries(0).getPosition())
      Assertions.expectResult(2)(entries(1).getPosition())
      Assertions.expectResult(null)(entries(0).getSubPart)
      Assertions.expectResult(null)(entries(1).getSubPart)
    }

    it("should parse non-PK auto-incremental column") {
      exec("""
          USE `madz_database_parser_test`;
          """ :: """
          CREATE TABLE `table_with_increment_non_primary_key` (
            `id` INTEGER(32) AUTO_INCREMENT,
            `name` VARCHAR(32),
            KEY `non_pk_incremental_key` (`id`)
          ) ENGINE = `InnoDB` DEFAULT CHARACTER SET = 'utf8' DEFAULT COLLATE='utf8_bin'; 
          """ :: Nil)
      /*
mysql> select * from statistics where table_name='table_with_increment_non_primary_key';
+---------------+--------------+--------------------------------------+------------+--------------+------------------------+--------------+-------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
| TABLE_CATALOG | TABLE_SCHEMA | TABLE_NAME                           | NON_UNIQUE | INDEX_SCHEMA | INDEX_NAME             | SEQ_IN_INDEX | COLUMN_NAME | COLLATION | CARDINALITY | SUB_PART | PACKED | NULLABLE | INDEX_TYPE | COMMENT | INDEX_COMMENT |
+---------------+--------------+--------------------------------------+------------+--------------+------------------------+--------------+-------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
| def           | test         | table_with_increment_non_primary_key |          1 | test         | non_pk_incremental_key |            1 | id          | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
+---------------+--------------+--------------------------------------+------------+--------------+------------------------+--------------+-------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
1 row in set (0.00 sec)

mysql> select * from columns where table_name='table_with_increment_non_primary_key';
+---------------+--------------+--------------------------------------+-------------+------------------+----------------+-------------+-----------+--------------------------+------------------------+-------------------+---------------+--------------------+----------------+-------------+------------+----------------+---------------------------------+----------------+
| TABLE_CATALOG | TABLE_SCHEMA | TABLE_NAME                           | COLUMN_NAME | ORDINAL_POSITION | COLUMN_DEFAULT | IS_NULLABLE | DATA_TYPE | CHARACTER_MAXIMUM_LENGTH | CHARACTER_OCTET_LENGTH | NUMERIC_PRECISION | NUMERIC_SCALE | CHARACTER_SET_NAME | COLLATION_NAME | COLUMN_TYPE | COLUMN_KEY | EXTRA          | PRIVILEGES                      | COLUMN_COMMENT |
+---------------+--------------+--------------------------------------+-------------+------------------+----------------+-------------+-----------+--------------------------+------------------------+-------------------+---------------+--------------------+----------------+-------------+------------+----------------+---------------------------------+----------------+
| def           | test         | table_with_increment_non_primary_key | id          |                1 | NULL           | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(32)     | MUL        | auto_increment | select,insert,update,references |                |
| def           | test         | table_with_increment_non_primary_key | name        |                2 | NULL           | YES         | varchar   |                       32 |                     96 |              NULL |          NULL | utf8               | utf8_bin       | varchar(32) |            |                | select,insert,update,references |                |
+---------------+--------------+--------------------------------------+-------------+------------------+----------------+-------------+-----------+--------------------------+------------------------+-------------------+---------------+--------------------+----------------+-------------+------------+----------------+---------------------------------+----------------+
2 rows in set (0.00 sec)
           */
      val schema = parser parseSchemaMetaData
      val table = schema.getTable("table_with_increment_non_primary_key")
      val composite_unique_key = table.getIndex("non_pk_incremental_key")
      val column = table.getColumn("id")
      //suppose msyql defect
      Assertions.expectResult(false)(composite_unique_key isUnique)
      Assertions.expectResult(null)(table.getPrimaryKey)
      Assertions.expectResult(true)(column.isAutoIncremented())
    }

    it("should parse single column FOREIGN KEY") {
      exec(
        """
          USE `madz_database_parser_test`;
          """ :: """
          CREATE TABLE `table_1` (
            `pk_column` INTEGER(32) AUTO_INCREMENT PRIMARY KEY,
            `secondary_key_column` VARCHAR(60) UNIQUE KEY
          ) ENGINE = `InnoDB` DEFAULT CHARACTER SET = 'utf8' DEFAULT COLLATE='utf8_bin';
          """ :: """
          CREATE TABLE `table_2` (
            `id` INTEGER(32) AUTO_INCREMENT PRIMARY KEY,
            `fk_column_1` INTEGER(32),
            `fk_column_2` VARCHAR(60),
            CONSTRAINT `FK_table_2_fk_column_1` FOREIGN KEY (`fk_column_1`) REFERENCES `table_1` (`pk_column`),
        	CONSTRAINT `FK_table_2_fk_column_2` FOREIGN KEY (`fk_column_2`) REFERENCES `table_1` (`secondary_key_column`)
          ) ENGINE = `InnoDB` DEFAULT CHARACTER SET = 'utf8' DEFAULT COLLATE='utf8_bin';
          """ :: Nil)
      /*
mysql> select * from statistics where table_name= 'table_1' or table_name='table_2';
+---------------+--------------+------------+------------+--------------+------------------------+--------------+----------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
| TABLE_CATALOG | TABLE_SCHEMA | TABLE_NAME | NON_UNIQUE | INDEX_SCHEMA | INDEX_NAME             | SEQ_IN_INDEX | COLUMN_NAME          | COLLATION | CARDINALITY | SUB_PART | PACKED | NULLABLE | INDEX_TYPE | COMMENT | INDEX_COMMENT |
+---------------+--------------+------------+------------+--------------+------------------------+--------------+----------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
| def           | test         | table_1    |          0 | test         | PRIMARY                |            1 | pk_column            | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
| def           | test         | table_1    |          0 | test         | secondary_key_column   |            1 | secondary_key_column | A         |           0 |     NULL | NULL   | YES      | BTREE      |         |               |
| def           | test         | table_2    |          0 | test         | PRIMARY                |            1 | id                   | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
| def           | test         | table_2    |          1 | test         | FK_table_2_fk_column_1 |            1 | fk_column_1          | A         |           0 |     NULL | NULL   | YES      | BTREE      |         |               |
| def           | test         | table_2    |          1 | test         | FK_table_2_fk_column_2 |            1 | fk_column_2          | A         |           0 |     NULL | NULL   | YES      | BTREE      |         |               |
+---------------+--------------+------------+------------+--------------+------------------------+--------------+----------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
5 rows in set (0.20 sec)

mysql> select * from columns where table_name= 'table_1' or table_name='table_2';
+---------------+--------------+------------+----------------------+------------------+----------------+-------------+-----------+--------------------------+------------------------+-------------------+---------------+--------------------+----------------+-------------+------------+----------------+---------------------------------+----------------+
| TABLE_CATALOG | TABLE_SCHEMA | TABLE_NAME | COLUMN_NAME          | ORDINAL_POSITION | COLUMN_DEFAULT | IS_NULLABLE | DATA_TYPE | CHARACTER_MAXIMUM_LENGTH | CHARACTER_OCTET_LENGTH | NUMERIC_PRECISION | NUMERIC_SCALE | CHARACTER_SET_NAME | COLLATION_NAME | COLUMN_TYPE | COLUMN_KEY | EXTRA          | PRIVILEGES                      | COLUMN_COMMENT |
+---------------+--------------+------------+----------------------+------------------+----------------+-------------+-----------+--------------------------+------------------------+-------------------+---------------+--------------------+----------------+-------------+------------+----------------+---------------------------------+----------------+
| def           | test         | table_1    | pk_column            |                1 | NULL           | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(32)     | PRI        | auto_increment | select,insert,update,references |                |
| def           | test         | table_1    | secondary_key_column |                2 | NULL           | YES         | varchar   |                       60 |                    180 |              NULL |          NULL | utf8               | utf8_bin       | varchar(60) | UNI        |                | select,insert,update,references |                |
| def           | test         | table_2    | id                   |                1 | NULL           | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(32)     | PRI        | auto_increment | select,insert,update,references |                |
| def           | test         | table_2    | fk_column_1          |                2 | NULL           | YES         | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(32)     | MUL        |                | select,insert,update,references |                |
| def           | test         | table_2    | fk_column_2          |                3 | NULL           | YES         | varchar   |                       60 |                    180 |              NULL |          NULL | utf8               | utf8_bin       | varchar(60) | MUL        |                | select,insert,update,references |                |
+---------------+--------------+------------+----------------------+------------------+----------------+-------------+-----------+--------------------------+------------------------+-------------------+---------------+--------------------+----------------+-------------+------------+----------------+---------------------------------+----------------+
5 rows in set (0.00 sec)
* 
mysql> select * from REFERENTIAL_CONSTRAINTS where constraint_schema='test';
+--------------------+-------------------+------------------------+---------------------------+--------------------------+------------------------+--------------+-------------+-------------+------------+-----------------------+
| CONSTRAINT_CATALOG | CONSTRAINT_SCHEMA | CONSTRAINT_NAME        | UNIQUE_CONSTRAINT_CATALOG | UNIQUE_CONSTRAINT_SCHEMA | UNIQUE_CONSTRAINT_NAME | MATCH_OPTION | UPDATE_RULE | DELETE_RULE | TABLE_NAME | REFERENCED_TABLE_NAME |
+--------------------+-------------------+------------------------+---------------------------+--------------------------+------------------------+--------------+-------------+-------------+------------+-----------------------+
| def                | test              | FK_table_2_fk_column_1 | def                       | test                     | PRIMARY                | NONE         | RESTRICT    | RESTRICT    | table_2    | table_1               |
| def                | test              | FK_table_2_fk_column_2 | def                       | test                     | secondary_key_column   | NONE         | RESTRICT    | RESTRICT    | table_2    | table_1               |
+--------------------+-------------------+------------------------+---------------------------+--------------------------+------------------------+--------------+-------------+-------------+------------+-----------------------+
2 rows in set (0.00 sec)

mysql> select * from key_column_usage where constraint_schema='test' and (table_name='table_1' or table_name='table_2');
+--------------------+-------------------+------------------------+---------------+--------------+------------+----------------------+------------------+-------------------------------+-------------------------+-----------------------+------------------------+
| CONSTRAINT_CATALOG | CONSTRAINT_SCHEMA | CONSTRAINT_NAME        | TABLE_CATALOG | TABLE_SCHEMA | TABLE_NAME | COLUMN_NAME          | ORDINAL_POSITION | POSITION_IN_UNIQUE_CONSTRAINT | REFERENCED_TABLE_SCHEMA | REFERENCED_TABLE_NAME | REFERENCED_COLUMN_NAME |
+--------------------+-------------------+------------------------+---------------+--------------+------------+----------------------+------------------+-------------------------------+-------------------------+-----------------------+------------------------+
| def                | test              | PRIMARY                | def           | test         | table_1    | pk_column            |                1 |                          NULL | NULL                    | NULL                  | NULL                   |
| def                | test              | secondary_key_column   | def           | test         | table_1    | secondary_key_column |                1 |                          NULL | NULL                    | NULL                  | NULL                   |
| def                | test              | PRIMARY                | def           | test         | table_2    | id                   |                1 |                          NULL | NULL                    | NULL                  | NULL                   |
| def                | test              | FK_table_2_fk_column_1 | def           | test         | table_2    | fk_column_1          |                1 |                             1 | test                    | table_1               | pk_column              |
| def                | test              | FK_table_2_fk_column_2 | def           | test         | table_2    | fk_column_2          |                1 |                             1 | test                    | table_1               | secondary_key_column   |
+--------------------+-------------------+------------------------+---------------+--------------+------------+----------------------+------------------+-------------------------------+-------------------------+-----------------------+------------------------+
5 rows in set (0.01 sec)
*/
      val schema = parser parseSchemaMetaData

      val table_1 = schema getTable "table_1"
      val table_2 = schema getTable "table_2"
      val fk_1 = table_2.getForeignKeySet().toArray(Array[ForeignKeyMetaData.Entry[MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData]]())(0)
      val fk_2 = table_2.getForeignKeySet().toArray(Array[ForeignKeyMetaData.Entry[MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData]]())(1)
      val fk_index_1 = table_2 getIndex "FK_table_2_fk_column_1"
      val fk_index_2 = table_2 getIndex "FK_table_2_fk_column_2"
      val fk_column_1 = table_2 getColumn "fk_column_1"
      val fk_column_2 = table_2 getColumn "fk_column_2"

      Assertions.expectResult(null)(fk_index_1.getIndexComment)
      Assertions.expectResult(MySQLIndexMethod.btree)(fk_index_1.getIndexMethod)
      Assertions.expectResult(true)(fk_index_1.isNull)
      Assertions.expectResult(true)(fk_index_1.containsColumn(fk_column_1))
      Assertions.expectResult(false)(fk_index_1.containsColumn(fk_column_2))
      Assertions.expectResult(false)(fk_index_1.containsColumn(table_2 getColumn "id"))
      Assertions.expectResult(0)(fk_index_1.getCardinality)
      Assertions.expectResult("FK_table_2_fk_column_1")(fk_index_1.getIndexName)
      Assertions.expectResult(IndexTypeEnum.statistic)(fk_index_1.getIndexType)
      Assertions.expectResult(KeyTypeEnum.index)(fk_index_1.getKeyType)
      Assertions.expectResult(null)(fk_index_1.getPageCount)
      Assertions.expectResult(SortDirectionEnum.ascending)(fk_index_1.getSortDirection)
      Assertions.expectResult(table_2)(fk_index_1.getTable)
      Assertions.expectResult(false)(fk_index_1.isUnique)

      Assertions.expectResult(null)(fk_index_2.getIndexComment)
      Assertions.expectResult(MySQLIndexMethod.btree)(fk_index_2.getIndexMethod)
      Assertions.expectResult(true)(fk_index_2.isNull)
      Assertions.expectResult(false)(fk_index_2.containsColumn(fk_column_1))
      Assertions.expectResult(true)(fk_index_2.containsColumn(fk_column_2))
      Assertions.expectResult(false)(fk_index_2.containsColumn(table_2 getColumn "id"))
      Assertions.expectResult(0)(fk_index_2.getCardinality)
      Assertions.expectResult("FK_table_2_fk_column_2")(fk_index_2.getIndexName)
      Assertions.expectResult(IndexTypeEnum.statistic)(fk_index_2.getIndexType)
      Assertions.expectResult(KeyTypeEnum.index)(fk_index_2.getKeyType)
      Assertions.expectResult(null)(fk_index_2.getPageCount)
      Assertions.expectResult(SortDirectionEnum.ascending)(fk_index_2.getSortDirection)
      Assertions.expectResult(table_2)(fk_index_2.getTable)
      Assertions.expectResult(false)(fk_index_2.isUnique)
    }

    it("should parse multiple columns FOREIGN KEY with correct order") {
      exec(
        """
          USE `madz_database_parser_test`;
          """ :: """
          CREATE TABLE `t1` (
          `key_column_1` INTEGER(32) NOT NULL,
          `key_column_2` INTEGER(32) NOT NULL,
          `data` VARCHAR(80),
          PRIMARY KEY (`key_column_1`, `key_column_2`)
          ) ENGINE = `InnoDB` ;
          """ :: """ 
          CREATE TABLE `t2` (
          `id` INTEGER(32) NOT NULL,
          `fk_column_1` INTEGER(32) NOT NULL,
          `fk_column_2` INTEGER(32) NOT NULL,
          PRIMARY KEY (`id`),
          CONSTRAINT `FK_t1_composite_column_1_column_2` FOREIGN KEY (`fk_column_1`,`fk_column_2`) REFERENCES `t1` (`key_column_1`,`key_column_2`)
          ) ENGINE = `InnoDB`;
          """ :: Nil)
      /*
mysql> use information_schema;
Database changed
mysql> select * from statistics where table_name= 't1' or table_name='t2';
+---------------+---------------------------+------------+------------+---------------------------+-----------------------------------+--------------+--------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
| TABLE_CATALOG | TABLE_SCHEMA              | TABLE_NAME | NON_UNIQUE | INDEX_SCHEMA              | INDEX_NAME                        | SEQ_IN_INDEX | COLUMN_NAME  | COLLATION | CARDINALITY | SUB_PART | PACKED | NULLABLE | INDEX_TYPE | COMMENT | INDEX_COMMENT |
+---------------+---------------------------+------------+------------+---------------------------+-----------------------------------+--------------+--------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
| def           | crmp                      | t2         |          0 | crmp                      | PRIMARY                           |            1 | a            | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
| def           | crmp                      | t2         |          0 | crmp                      | PRIMARY                           |            2 | b            | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
| def           | madz_database_parser_test | t1         |          0 | madz_database_parser_test | PRIMARY                           |            1 | key_column_1 | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
| def           | madz_database_parser_test | t1         |          0 | madz_database_parser_test | PRIMARY                           |            2 | key_column_2 | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
| def           | madz_database_parser_test | t2         |          0 | madz_database_parser_test | PRIMARY                           |            1 | id           | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
| def           | madz_database_parser_test | t2         |          1 | madz_database_parser_test | FK_t1_composite_column_1_column_2 |            1 | fk_column_1  | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
| def           | madz_database_parser_test | t2         |          1 | madz_database_parser_test | FK_t1_composite_column_1_column_2 |            2 | fk_column_2  | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
+---------------+---------------------------+------------+------------+---------------------------+-----------------------------------+--------------+--------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
7 rows in set (0.20 sec)

mysql> select * from columns where table_name= 't1' or table_name='t2';
+---------------+---------------------------+------------+--------------+------------------+----------------+-------------+-----------+--------------------------+------------------------+-------------------+---------------+--------------------+----------------+-------------+------------+-------+---------------------------------+----------------+
| TABLE_CATALOG | TABLE_SCHEMA              | TABLE_NAME | COLUMN_NAME  | ORDINAL_POSITION | COLUMN_DEFAULT | IS_NULLABLE | DATA_TYPE | CHARACTER_MAXIMUM_LENGTH | CHARACTER_OCTET_LENGTH | NUMERIC_PRECISION | NUMERIC_SCALE | CHARACTER_SET_NAME | COLLATION_NAME | COLUMN_TYPE | COLUMN_KEY | EXTRA | PRIVILEGES                      | COLUMN_COMMENT |
+---------------+---------------------------+------------+--------------+------------------+----------------+-------------+-----------+--------------------------+------------------------+-------------------+---------------+--------------------+----------------+-------------+------------+-------+---------------------------------+----------------+
| def           | crmp                      | t2         | a            |                1 | 0              | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(11)     | PRI        |       | select,insert,update,references |                |
| def           | crmp                      | t2         | b            |                2 | 0              | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(11)     | PRI        |       | select,insert,update,references |                |
| def           | crmp                      | t2         | c            |                3 | NULL           | YES         | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(11)     |            |       | select,insert,update,references |                |
| def           | madz_database_parser_test | t1         | key_column_1 |                1 | NULL           | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(32)     | PRI        |       | select,insert,update,references |                |
| def           | madz_database_parser_test | t1         | key_column_2 |                2 | NULL           | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(32)     | PRI        |       | select,insert,update,references |                |
| def           | madz_database_parser_test | t1         | data         |                3 | NULL           | YES         | varchar   |                       80 |                    160 |              NULL |          NULL | gbk                | gbk_chinese_ci | varchar(80) |            |       | select,insert,update,references |                |
| def           | madz_database_parser_test | t2         | id           |                1 | NULL           | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(32)     | PRI        |       | select,insert,update,references |                |
| def           | madz_database_parser_test | t2         | fk_column_1  |                2 | NULL           | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(32)     | MUL        |       | select,insert,update,references |                |
| def           | madz_database_parser_test | t2         | fk_column_2  |                3 | NULL           | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(32)     |            |       | select,insert,update,references |                |
+---------------+---------------------------+------------+--------------+------------------+----------------+-------------+-----------+--------------------------+------------------------+-------------------+---------------+--------------------+----------------+-------------+------------+-------+---------------------------------+----------------+
9 rows in set (0.01 sec)

mysql> select * from REFERENTIAL_CONSTRAINTS where constraint_schema='madz_database_parser_test';
+--------------------+---------------------------+-----------------------------------+---------------------------+---------------------------+------------------------+--------------+-------------+-------------+------------+-----------------------+
| CONSTRAINT_CATALOG | CONSTRAINT_SCHEMA         | CONSTRAINT_NAME                   | UNIQUE_CONSTRAINT_CATALOG | UNIQUE_CONSTRAINT_SCHEMA  | UNIQUE_CONSTRAINT_NAME | MATCH_OPTION | UPDATE_RULE | DELETE_RULE | TABLE_NAME | REFERENCED_TABLE_NAME |
+--------------------+---------------------------+-----------------------------------+---------------------------+---------------------------+------------------------+--------------+-------------+-------------+------------+-----------------------+
| def                | madz_database_parser_test | FK_t1_composite_column_1_column_2 | def                       | madz_database_parser_test | PRIMARY                | NONE         | RESTRICT    | RESTRICT    | t2         | t1                    |
+--------------------+---------------------------+-----------------------------------+---------------------------+---------------------------+------------------------+--------------+-------------+-------------+------------+-----------------------+
1 row in set (0.00 sec)

mysql> select * from key_column_usage where constraint_schema='madz_database_parser_test' and (table_name='t1' or table_name='t2');
+--------------------+---------------------------+-----------------------------------+---------------+---------------------------+------------+--------------+------------------+-------------------------------+---------------------------+-----------------------+------------------------+
| CONSTRAINT_CATALOG | CONSTRAINT_SCHEMA         | CONSTRAINT_NAME                   | TABLE_CATALOG | TABLE_SCHEMA              | TABLE_NAME | COLUMN_NAME  | ORDINAL_POSITION | POSITION_IN_UNIQUE_CONSTRAINT | REFERENCED_TABLE_SCHEMA   | REFERENCED_TABLE_NAME | REFERENCED_COLUMN_NAME |
+--------------------+---------------------------+-----------------------------------+---------------+---------------------------+------------+--------------+------------------+-------------------------------+---------------------------+-----------------------+------------------------+
| def                | madz_database_parser_test | PRIMARY                           | def           | madz_database_parser_test | t1         | key_column_1 |                1 |                          NULL | NULL                      | NULL                  | NULL                   |
| def                | madz_database_parser_test | PRIMARY                           | def           | madz_database_parser_test | t1         | key_column_2 |                2 |                          NULL | NULL                      | NULL                  | NULL                   |
| def                | madz_database_parser_test | PRIMARY                           | def           | madz_database_parser_test | t2         | id           |                1 |                          NULL | NULL                      | NULL                  | NULL                   |
| def                | madz_database_parser_test | FK_t1_composite_column_1_column_2 | def           | madz_database_parser_test | t2         | fk_column_1  |                1 |                             1 | madz_database_parser_test | t1                    | key_column_1           |
| def                | madz_database_parser_test | FK_t1_composite_column_1_column_2 | def           | madz_database_parser_test | t2         | fk_column_2  |                2 |                             2 | madz_database_parser_test | t1                    | key_column_2           |
+--------------------+---------------------------+-----------------------------------+---------------+---------------------------+------------+--------------+------------------+-------------------------------+---------------------------+-----------------------+------------------------+
5 rows in set (0.00 sec)
         */
      val schema = parser parseSchemaMetaData
      val table_1 = schema getTable "t1"
      val table_2 = schema getTable "t2"
      val fk_index = table_2.getIndex("FK_t1_composite_column_1_column_2")
      val fkEntry_1 = table_2.getForeignKeySet.toArray(Array[IndexMetaData.Entry[MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData]]())(0)
      val fkEntry_2 = table_2.getForeignKeySet.toArray(Array[IndexMetaData.Entry[MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData]]())(1)
      val fk_column_1 = table_2.getColumn("fk_column_1")
      val fk_column_2 = table_2.getColumn("fk_column_2")
      Assertions.expectResult(fk_column_1)(fkEntry_1 getColumn)
      Assertions.expectResult(fk_column_2)(fkEntry_2 getColumn)
      Assertions.expectResult(1)(fkEntry_1 getPosition)
      Assertions.expectResult(2)(fkEntry_2 getPosition)
      Assertions.expectResult(fk_index)(fkEntry_1.getKey)
      Assertions.expectResult(fk_index)(fkEntry_2.getKey)
      Assertions.expectResult(null)(fkEntry_1 getSubPart)
      Assertions.expectResult(null)(fkEntry_2.getSubPart)

      Assertions.expectResult(null)(fk_index.getIndexComment)
      Assertions.expectResult(MySQLIndexMethod.btree)(fk_index.getIndexMethod)
      Assertions.expectResult(null)(fk_index.isNull)
      Assertions.expectResult(true)(fk_index.containsColumn(fk_column_1))
      Assertions.expectResult(true)(fk_index.containsColumn(fk_column_2))
      Assertions.expectResult(false)(fk_index.containsColumn(table_2 getColumn "id"))
      Assertions.expectResult(null)(fk_index.getCardinality)
      Assertions.expectResult("FK_t1_composite_column_1_column_2")(fk_index.getIndexName)
      Assertions.expectResult(IndexTypeEnum.statistic)(fk_index.getIndexType)
      Assertions.expectResult(KeyTypeEnum.index)(fk_index.getKeyType)
      Assertions.expectResult(null)(fk_index.getPageCount)
      Assertions.expectResult(SortDirectionEnum.ascending)(fk_index.getSortDirection)
      Assertions.expectResult(table_2)(fk_index.getTable)
      Assertions.expectResult(false)(fk_index.isUnique)
    }

    it("should parse single column index") {
      exec(
        """
          USE `madz_database_parser_test`;
          """ :: """
          CREATE TABLE `t1` (
          `id` INTEGER(32) AUTO_INCREMENT PRIMARY KEY,
          `index_column` INTEGER(32) NOT NULL,
          `data` VARCHAR(80),
          INDEX `single_column_index` (`index_column`)
          ) ENGINE = `InnoDB` ;
          """ :: Nil)

      val schema = parser parseSchemaMetaData
      val t1 = schema getTable "t1"
      val index_column = t1 getColumn "index_column"
      val index = t1 getIndex "single_column_index"
      val index_entry = index.getEntrySet.toArray(Array[IndexMetaData.Entry[MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData]]())(1)
      Assertions.expectResult(index)(index_entry getKey)
      Assertions.expectResult(index_column)(index_entry getColumn)
      Assertions.expectResult(1)(index_entry getPosition)
      Assertions.expectResult(null)(index_entry getSubPart)
      Assertions.expectResult(null)(index.getIndexComment)
      Assertions.expectResult(MySQLIndexMethod.btree)(index.getIndexMethod)
      Assertions.expectResult(null)(index.isNull)
      Assertions.expectResult(true)(index.containsColumn(index_column))
      Assertions.expectResult(false)(index.containsColumn(t1 getColumn "data"))
      Assertions.expectResult(null)(index.getCardinality)
      Assertions.expectResult("single_column_index")(index.getIndexName)
      Assertions.expectResult(IndexTypeEnum.statistic)(index.getIndexType)
      Assertions.expectResult(KeyTypeEnum.index)(index.getKeyType)
      Assertions.expectResult(null)(index.getPageCount)
      Assertions.expectResult(SortDirectionEnum.ascending)(index.getSortDirection)
      Assertions.expectResult(t1)(index.getTable)
      Assertions.expectResult(false)(index.isUnique)
    }

    it("should parse multiple column index with correct order") {
      exec(
        """
          USE `madz_database_parser_test`;
          """ :: """
          CREATE TABLE `table_composite_index_test` (
          `id` INTEGER(32) AUTO_INCREMENT PRIMARY KEY,
          `index_column_1` INTEGER(32) NOT NULL,
          `index_column_2` INTEGER(32) NOT NULL,
          `data` VARCHAR(80),
          INDEX `composite_column_index` (`index_column_1`, `index_column_2`)
          ) ENGINE = `InnoDB` ;
          """ :: Nil)
      /*
mysql> select * from statistics where table_name= 'table_composite_index_test';
+---------------+---------------------------+----------------------------+------------+---------------------------+------------------------+--------------+----------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
| TABLE_CATALOG | TABLE_SCHEMA              | TABLE_NAME                 | NON_UNIQUE | INDEX_SCHEMA              | INDEX_NAME             | SEQ_IN_INDEX | COLUMN_NAME    | COLLATION | CARDINALITY | SUB_PART | PACKED | NULLABLE | INDEX_TYPE | COMMENT | INDEX_COMMENT |
+---------------+---------------------------+----------------------------+------------+---------------------------+------------------------+--------------+----------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
| def           | madz_database_parser_test | table_composite_index_test |          0 | madz_database_parser_test | PRIMARY                |            1 | id             | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
| def           | madz_database_parser_test | table_composite_index_test |          1 | madz_database_parser_test | composite_column_index |            1 | index_column_1 | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
| def           | madz_database_parser_test | table_composite_index_test |          1 | madz_database_parser_test | composite_column_index |            2 | index_column_2 | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
+---------------+---------------------------+----------------------------+------------+---------------------------+------------------------+--------------+----------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
3 rows in set (0.01 sec)

mysql> select * from columns where table_name= 'table_composite_index_test';
+---------------+---------------------------+----------------------------+----------------+------------------+----------------+-------------+-----------+--------------------------+------------------------+-------------------+---------------+--------------------+----------------+-------------+------------+----------------+---------------------------------+----------------+
| TABLE_CATALOG | TABLE_SCHEMA              | TABLE_NAME                 | COLUMN_NAME    | ORDINAL_POSITION | COLUMN_DEFAULT | IS_NULLABLE | DATA_TYPE | CHARACTER_MAXIMUM_LENGTH | CHARACTER_OCTET_LENGTH | NUMERIC_PRECISION | NUMERIC_SCALE | CHARACTER_SET_NAME | COLLATION_NAME | COLUMN_TYPE | COLUMN_KEY | EXTRA          | PRIVILEGES                      | COLUMN_COMMENT |
+---------------+---------------------------+----------------------------+----------------+------------------+----------------+-------------+-----------+--------------------------+------------------------+-------------------+---------------+--------------------+----------------+-------------+------------+----------------+---------------------------------+----------------+
| def           | madz_database_parser_test | table_composite_index_test | id             |                1 | NULL           | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(32)     | PRI        | auto_increment | select,insert,update,references |                |
| def           | madz_database_parser_test | table_composite_index_test | index_column_1 |                2 | NULL           | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(32)     | MUL        |                | select,insert,update,references |                |
| def           | madz_database_parser_test | table_composite_index_test | index_column_2 |                3 | NULL           | NO          | int       |                     NULL |                   NULL |                10 |             0 | NULL               | NULL           | int(32)     |            |                | select,insert,update,references |                |
| def           | madz_database_parser_test | table_composite_index_test | data           |                4 | NULL           | YES         | varchar   |                       80 |                    160 |              NULL |          NULL | gbk                | gbk_chinese_ci | varchar(80) |            |                | select,insert,update,references |                |
+---------------+---------------------------+----------------------------+----------------+------------------+----------------+-------------+-----------+--------------------------+------------------------+-------------------+---------------+--------------------+----------------+-------------+------------+----------------+---------------------------------+----------------+
4 rows in set (0.13 sec)
 */
      val schema = parser parseSchemaMetaData
      val t1 = schema getTable "table_composite_index_test"
      val index_column_1 = t1 getColumn "index_column_1"
      val index_column_2 = t1 getColumn "index_column_2"
      val index = t1 getIndex "composite_column_index"
      val index_entry_1 = index.getEntrySet.toArray(Array[IndexMetaData.Entry[MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData]]())(1)
      val index_entry_2 = index.getEntrySet.toArray(Array[IndexMetaData.Entry[MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData]]())(2)
      Assertions.expectResult(index)(index_entry_1 getKey)
      Assertions.expectResult(index)(index_entry_2 getKey)
      Assertions.expectResult(index_column_1)(index_entry_1 getColumn)
      Assertions.expectResult(index_column_2)(index_entry_2 getColumn)
      Assertions.expectResult(1)(index_entry_1 getPosition)
      Assertions.expectResult(2)(index_entry_2 getPosition)
      Assertions.expectResult(null)(index_entry_1 getSubPart)
      Assertions.expectResult(null)(index_entry_2 getSubPart)
      Assertions.expectResult(null)(index.getIndexComment)
      Assertions.expectResult(MySQLIndexMethod.btree)(index.getIndexMethod)
      Assertions.expectResult(null)(index.isNull)
      Assertions.expectResult(true)(index.containsColumn(index_column_1))
      Assertions.expectResult(true)(index.containsColumn(index_column_2))
      Assertions.expectResult(false)(index.containsColumn(t1 getColumn "data"))
      Assertions.expectResult(null)(index.getCardinality)
      Assertions.expectResult("composite_column_index")(index.getIndexName)
      Assertions.expectResult(IndexTypeEnum.statistic)(index.getIndexType)
      Assertions.expectResult(KeyTypeEnum.index)(index.getKeyType)
      Assertions.expectResult(null)(index.getPageCount)
      Assertions.expectResult(SortDirectionEnum.ascending)(index.getSortDirection)
      Assertions.expectResult(t1)(index.getTable)
      Assertions.expectResult(false)(index.isUnique)
    }

  }
  val database_name = "madz_database_parser_test"
  val drop_database_query = "DROP DATABASE IF EXISTS " + database_name + ";"
  val create_database_query = "CREATE DATABASE " + database_name + ";"
  val create_table_with_all_data_types_DDL_1 = """
    CREATE TABLE `table_with_all_data_types_p1` (
      `BIT_COLUMN`                      BIT(1)                          DEFAULT null,
      `BIT_PLUS_COLUMN`                 BIT(2)                          DEFAULT null,
      `TINYINT_COLUMN`                  TINYINT(1)                      DEFAULT null,
      `TINYINT_PLUS_COLUMN`             TINYINT(8)                      DEFAULT null,
      `TINYINT_UNSIGNED_COLUMN`         TINYINT(8) UNSIGNED             DEFAULT null,
      `BOOL_COLUMN`                     TINYINT(1)                      DEFAULT null,
      `BOOLEAN_COLUMN`                  TINYINT(1)                      DEFAULT null,
      `SMALLINT_COLUMN`                 SMALLINT(16)                    DEFAULT null,
      `SMALLINT_UNSIGNED_COLUMN`        SMALLINT(16) UNSIGNED           DEFAULT null,
      `MEDIUMINT_COLUMN`                MEDIUMINT(24)                   DEFAULT null,
      `MEDIUMINT_UNSIGNED_COLUMN`       MEDIUMINT(24) UNSIGNED          DEFAULT null,
      `INT_COLUMN`                      INT(32)                         DEFAULT null,
      `INT_UNSIGNED_COLUMN`             INT(32) UNSIGNED                DEFAULT null,
      `INTEGER_COLUMN`                  INTEGER(32)                     DEFAULT null,
      `INTEGER_UNSIGNED_COLUMN`         INTEGER(32) UNSIGNED            DEFAULT null,
      `BIGINT_COLUMN`                   BIGINT(64)                      DEFAULT null,                   
      `BIGINT_UNSIGNED_COLUMN`          BIGINT(64) UNSIGNED             DEFAULT null,
      `FLOAT_COLUMN`                    FLOAT(7,4)                      DEFAULT null,
      `DOUBLE_COLUMN`                   DOUBLE PRECISION (64,30)        DEFAULT null,
      `DOUBLE_PLUS_COLUMN`              DOUBLE PRECISION (128,30)       DEFAULT null,
      `DECIMAL_COLUMN`                  DECIMAL                         DEFAULT null,
      `DECIMAL_NO_SCALE_COLUMN`         DECIMAL(65, 0)                  DEFAULT null,
      `DECIMAL_SCALE_COLUMN`            DECIMAL(65, 30)                 DEFAULT null,
      `DATE_COLUMN`                     DATE                            DEFAULT null,
      `DATETIME_COLUMN`                 DATETIME                        DEFAULT null,
      `TIMESTAMP_COLUMN`                TIMESTAMP                       DEFAULT '2010-12-10 14:12:09',
      `TIME_COLUMN`                     TIME                            DEFAULT null,
      `YEAR_COLUMN`                     YEAR(2)                         DEFAULT null,
      `YEAR_PLUS_COLUMN`                YEAR(4)                         DEFAULT null,
      `CHAR_COLUMN`                     CHAR(255)                       DEFAULT null,
      `BINARY_COLUMN`                   BINARY(255)                     DEFAULT null
    ) ENGINE=InnoDB DEFAULT CHARSET=latin7;
  """
  val create_table_with_all_data_types_DDL_2 = """
    CREATE TABLE `table_with_all_data_types_p2` (
      `VARBINARY_COLUMN`                VARBINARY(65532)                DEFAULT null
    ) ENGINE=InnoDB DEFAULT CHARSET=latin7;
  """
  val create_table_with_all_data_types_DDL_3 = """
    CREATE TABLE `table_with_all_data_types_p3` (
      `TINYBLOB_COLUMN`                 TINYBLOB                        DEFAULT null,
      `TINYTEXT_COLUMN`                 TINYTEXT                        DEFAULT null,
      `BLOB_COLUMN`                     BLOB                            DEFAULT null,
      `TEXT_COLUMN`                     TEXT(65535)                     DEFAULT null,
      `MEDIUMBLOB_COLUMN`               MEDIUMBLOB                      DEFAULT null,
      `MEDIUMTEXT_COLUMN`               MEDIUMTEXT                      DEFAULT null,
      `LONGBLOB_COLUMN`                 LONGBLOB                        DEFAULT null,
      `LONGTEXT_COLUMN`                 LONGTEXT                        DEFAULT null,
      `ENUM_COLUMN`                     ENUM('A','B','C')               DEFAULT null,
      `SET_COLUMN`                      SET('HLJ','JX','BJ')            DEFAULT null
    ) ENGINE=InnoDB DEFAULT CHARSET=latin7;
  """

  val create_table_with_all_data_types_DDL_4 = """
    CREATE TABLE `table_with_all_data_types_p4` (
      `VARCHAR_COLUMN`                  VARCHAR(65532)                  DEFAULT null
    ) ENGINE=InnoDB DEFAULT CHARSET=latin7;
  """
  val create_table_with_all_data_types_DDL_5 = """
    CREATE TABLE `table_with_all_data_types_p5` (
      `VARCHAR_BINARY_COLUMN`           VARCHAR(65532)  BINARY          DEFAULT null
  ) ENGINE=InnoDB DEFAULT CHARSET=latin7;
  """

  case class ColumnMetaData(
    val TABLE_NAME: String, val COLUMN_NAME: String, val ORDINAL_POSITION: Integer,
    val COLUMN_DEFAULT: String, val IS_NULLABLE: Boolean, val DATA_TYPE: String,
    val CHARACTER_MAXIMUM_LENGTH: java.lang.Long, val NUMERIC_PRECISION: Integer,
    val NUMERIC_SCALE: Integer, val CHARACTER_SET_NAME: String, val COLLATION_NAME: String,
    val COLUMN_TYPE: String)

    val columns_in_table1 =
    ColumnMetaData("table_with_all_data_types_p1", "BIT_COLUMN", 1, null, true, "bit", 0, 1, 0, null, null, "bit(1)") ::
      ColumnMetaData("table_with_all_data_types_p1", "BIT_PLUS_COLUMN", 2, null, true, "bit", 0, 2, 0, null, null, "bit(2)") ::
      ColumnMetaData("table_with_all_data_types_p1", "TINYINT_COLUMN", 3, null, true, "tinyint", 0, 3, 0, null, null, "tinyint(1)") ::
      ColumnMetaData("table_with_all_data_types_p1", "TINYINT_PLUS_COLUMN", 4, null, true, "tinyint", 0, 3, 0, null, null, "tinyint(8)") ::
      ColumnMetaData("table_with_all_data_types_p1", "TINYINT_UNSIGNED_COLUMN", 5, null, true, "tinyint", 0, 3, 0, null, null, "tinyint(8) unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "BOOL_COLUMN", 6, null, true, "tinyint", 0, 3, 0, null, null, "tinyint(1)") ::
      ColumnMetaData("table_with_all_data_types_p1", "BOOLEAN_COLUMN", 7, null, true, "tinyint", 0, 3, 0, null, null, "tinyint(1)") ::
      ColumnMetaData("table_with_all_data_types_p1", "SMALLINT_COLUMN", 8, null, true, "smallint", 0, 5, 0, null, null, "smallint(16)") ::
      ColumnMetaData("table_with_all_data_types_p1", "SMALLINT_UNSIGNED_COLUMN", 9, null, true, "smallint", 0, 5, 0, null, null, "smallint(16) unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "MEDIUMINT_COLUMN", 10, null, true, "mediumint", 0, 7, 0, null, null, "mediumint(24)") ::
      ColumnMetaData("table_with_all_data_types_p1", "MEDIUMINT_UNSIGNED_COLUMN", 11, null, true, "mediumint", 0, 7, 0, null, null, "mediumint(24) unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "INT_COLUMN", 12, null, true, "int", 0, 10, 0, null, null, "int(32)") ::
      ColumnMetaData("table_with_all_data_types_p1", "INT_UNSIGNED_COLUMN", 13, null, true, "int", 0, 10, 0, null, null, "int(32) unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "INTEGER_COLUMN", 14, null, true, "int", 0, 10, 0, null, null, "int(32)") ::
      ColumnMetaData("table_with_all_data_types_p1", "INTEGER_UNSIGNED_COLUMN", 15, null, true, "int", 0, 10, 0, null, null, "int(32) unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "BIGINT_COLUMN", 16, null, true, "bigint", 0, 19, 0, null, null, "bigint(64)") ::
      ColumnMetaData("table_with_all_data_types_p1", "BIGINT_UNSIGNED_COLUMN", 17, null, true, "bigint", 0, 20, 0, null, null, "bigint(64) unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "FLOAT_COLUMN", 18, null, true, "float", 0, 7, 4, null, null, "float(7,4)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DOUBLE_COLUMN", 19, null, true, "double", 0, 64, 30, null, null, "double(64,30)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DOUBLE_PLUS_COLUMN", 20, null, true, "double", 0, 128, 30, null, null, "double(128,30)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DECIMAL_COLUMN", 21, null, true, "decimal", 0, 10, 0, null, null, "decimal(10,0)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DECIMAL_NO_SCALE_COLUMN", 22, null, true, "decimal", 0, 65, 0, null, null, "decimal(65,0)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DECIMAL_SCALE_COLUMN", 23, null, true, "decimal", 0, 65, 30, null, null, "decimal(65,30)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DATE_COLUMN", 24, null, true, "date", 0, 0, 0, null, null, "date") ::
      ColumnMetaData("table_with_all_data_types_p1", "DATETIME_COLUMN", 25, null, true, "datetime", 0, 0, 0, null, null, "datetime") ::
      ColumnMetaData("table_with_all_data_types_p1", "TIMESTAMP_COLUMN", 26, "2010-12-10 14:12:09", false, "timestamp", 0, 0, 0, null, null, "timestamp") ::
      ColumnMetaData("table_with_all_data_types_p1", "TIME_COLUMN", 27, null, true, "time", 0, 0, 0, null, null, "time") ::
      ColumnMetaData("table_with_all_data_types_p1", "YEAR_COLUMN", 28, null, true, "year", 0, 0, 0, null, null, "year(2)") ::
      ColumnMetaData("table_with_all_data_types_p1", "YEAR_PLUS_COLUMN", 29, null, true, "year", 0, 0, 0, null, null, "year(4)") ::
      ColumnMetaData("table_with_all_data_types_p1", "CHAR_COLUMN", 30, null, true, "char", 255, 0, 0, "latin7", "latin7_general_ci", "char(255)") ::
      ColumnMetaData("table_with_all_data_types_p1", "BINARY_COLUMN", 31, null, true, "binary", 255, 0, 0, null, null, "binary(255)") :: Nil

  val columns_in_table2 =
    ColumnMetaData("table_with_all_data_types_p2", "VARBINARY_COLUMN", 1, null, true, "varbinary", 65532, 0, 0, null, null, "varbinary(65532)") :: Nil

  val columns_in_table3 =
    ColumnMetaData("table_with_all_data_types_p3", "TINYBLOB_COLUMN", 1, null, true, "tinyblob", 255, 0, 0, null, null, "tinyblob") ::
      ColumnMetaData("table_with_all_data_types_p3", "TINYTEXT_COLUMN", 2, null, true, "tinytext", 255, 0, 0, "latin7", "latin7_general_ci", "tinytext") ::
      ColumnMetaData("table_with_all_data_types_p3", "BLOB_COLUMN", 3, null, true, "blob", 65535, 0, 0, null, null, "blob") ::
      ColumnMetaData("table_with_all_data_types_p3", "TEXT_COLUMN", 4, null, true, "text", 65535, 0, 0, "latin7", "latin7_general_ci", "text") ::
      ColumnMetaData("table_with_all_data_types_p3", "MEDIUMBLOB_COLUMN", 5, null, true, "mediumblob", 16777215L, 0, 0, null, null, "mediumblob") ::
      ColumnMetaData("table_with_all_data_types_p3", "MEDIUMTEXT_COLUMN", 6, null, true, "mediumtext", 16777215L, 0, 0, "latin7", "latin7_general_ci", "mediumtext") ::
      ColumnMetaData("table_with_all_data_types_p3", "LONGBLOB_COLUMN", 7, null, true, "longblob", 4294967295L, 0, 0, null, null, "longblob") ::
      ColumnMetaData("table_with_all_data_types_p3", "LONGTEXT_COLUMN", 8, null, true, "longtext", 4294967295L, 0, 0, "latin7", "latin7_general_ci", "longtext") ::
      ColumnMetaData("table_with_all_data_types_p3", "ENUM_COLUMN", 9, null, true, "enum", 1, 0, 0, "latin7", "latin7_general_ci", "enum('A','B','C')") ::
      ColumnMetaData("table_with_all_data_types_p3", "SET_COLUMN", 10, null, true, "set", 9, 0, 0, "latin7", "latin7_general_ci", "set('HLJ','JX','BJ')") :: Nil
  val columns_in_table4 =
    ColumnMetaData("table_with_all_data_types_p4", "VARCHAR_COLUMN", 1, null, true, "varchar", 65532, 0, 0, "latin7", "latin7_general_ci", "varchar(65532)") :: Nil
  val columns_in_table5 =
    ColumnMetaData("table_with_all_data_types_p5", "VARCHAR_BINARY_COLUMN", 1, null, true, "varchar", 65532, 0, 0, "latin7", "latin7_bin", "varchar(65532)") :: Nil
  def verifyColumns(expectedColumnList: List[ColumnMetaData], actualColumnList: java.util.List[MySQLColumnMetaData]) {
    val actualScalaList = collectionAsScalaIterable[MySQLColumnMetaData](actualColumnList).toList
    Assertions.expectResult(expectedColumnList.length)(actualScalaList.length)
    val tuples = expectedColumnList zip actualScalaList
    tuples.foreach(pair =>
      compare(pair._1, pair._2))
  }

  def compare(expect: ColumnMetaData, actual: MySQLColumnMetaData) {
    println(expect.TABLE_NAME + "_" + expect.COLUMN_NAME);
    Assertions.expectResult(expect.TABLE_NAME)(actual.getTableMetaData getTableName)
    Assertions.expectResult(expect.COLUMN_NAME)(actual getColumnName)
    Assertions.expectResult(expect.ORDINAL_POSITION)(actual getOrdinalPosition)
    Assertions.expectResult(expect.COLUMN_DEFAULT)(actual getDefaultValue)
    Assertions.expectResult(expect.IS_NULLABLE)(actual isNullable)
    Assertions.expectResult(expect.DATA_TYPE.toUpperCase())(actual getSqlTypeName)
    Assertions.expectResult(expect.CHARACTER_MAXIMUM_LENGTH)(actual getCharacterMaximumLength())
    Assertions.expectResult(expect.NUMERIC_PRECISION)(actual getNumericPrecision ())
    Assertions.expectResult(expect.NUMERIC_SCALE)(actual getNumericScale ())
    Assertions.expectResult(expect.CHARACTER_SET_NAME)(actual getCharacterSet())
    Assertions.expectResult(expect.COLLATION_NAME)(actual getCollationName())
    Assertions.expectResult(expect.COLUMN_TYPE)(actual getColumnType())
  }
}