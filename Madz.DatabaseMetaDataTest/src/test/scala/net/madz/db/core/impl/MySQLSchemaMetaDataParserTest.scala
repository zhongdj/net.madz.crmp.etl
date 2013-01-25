package net.madz.db.core.impl

import java.sql.Connection

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.collection.immutable.List
import scala.slick.session.Database

import org.scalatest.Assertions
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSpec

import net.madz.db.core.impl.mysql.MySQLSchemaMetaDataParserImpl
import net.madz.db.core.meta.DottedPath
import net.madz.db.core.meta.immutable.IndexMetaData
import net.madz.db.core.meta.immutable.`type`.IndexType
import net.madz.db.core.meta.immutable.`type`.KeyType
import net.madz.db.core.meta.immutable.`type`.SortDirection
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum
import net.madz.db.core.meta.immutable.mysql.enums.MySQLIndexMethod
import net.madz.db.core.meta.immutable.mysql.enums.MySQLTableTypeEnum

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
      Assertions.expectResult(MySQLTableTypeEnum.base_table)(table getType)
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

      val result = parser parseSchemaMetaData

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
      verifyColumns(columns_in_table2, table1.getColumns)
      verifyColumns(columns_in_table3, table1.getColumns)
      verifyColumns(columns_in_table4, table1.getColumns)
      verifyColumns(columns_in_table5, table1.getColumns)

    }

    it("should parse column with column option NULLABLE") {
      exec(
        """
           USE `madz_database_parser_test`;
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
           CREATE TABLE `table_with_default_option` (
             `defaulted_column_name` VARCHAR(32) DEFAULT `Hello`,
             `not_defaulted_column_name` VARCHAR(32) NOT NULL
           ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
          """
          :: Nil)

      val result = parser parseSchemaMetaData

      Assertions.expectResult("Hello")(result.getTable("table_with_default_option").getColumn("defaulted_column_name").getDefaultValue())
      Assertions.expectResult(null)(result.getTable("table_with_default_option").getColumn("not_defaulted_column_name").getDefaultValue())
    }

    it("should parse column with column comment") {
      exec(
        """
           USE `madz_database_parser_test`;
           CREATE TABLE `table_with_column_comment` (
             `column_comment_column` VARCHAR(32) COMMENT `should have a comment`,
             `no_column_comment_column` VARCHAR(32) 
           ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
          """
          :: Nil)

      val result = parser parseSchemaMetaData

      Assertions.expectResult("should have a comment")(result.getTable("table_with_column_comment").getColumn("column_comment_column").getRemarks)
      Assertions.expectResult(null)(result.getTable("table_with_column_comment").getColumn("no_column_comment_column").getRemarks)

    }

  }

  describe("Parse indexes from Database") {
    it("should parse single column PK index") {
      exec(
        """
           USE `madz_database_parser_test`;
           CREATE TABLE `table_with_single_column_pk` (
             `single_column_pk` VARCHAR(32) CHARACTER SET `latin7` COLLATE `latin7_general_cs` PRIMARY KEY,
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
      Assertions.expectResult(null)(pk getSubPart)
      Assertions.expectResult(0)(pk getCardinality)
      Assertions.expectResult("PRIMARY")(pk getIndexName)
      Assertions.expectResult(IndexType.clustered)(pk getIndexType)
      Assertions.expectResult(KeyType.primaryKey)(pk getKeyType)
      Assertions.expectResult(0 /*"unknown"*/ )(pk getPageCount)
      Assertions.expectResult(SortDirection.ascending)(pk getSortDirection)
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
           CREATE TABLE `table_with_single_column_pk` (
             `single_column_pk` INTEGER(32) AUTO_INCREMENT PRIMARY KEY,
             `common_column` VARCHAR(32) 
           ) ENGINE=`InnoDB` DEFAULT CHARACTER SET=`utf8` DEFAULT COLLATE=`utf8_unicode_ci`;
          """
          :: Nil)

        /*
          mysql> select * from statistics where table_schema = 'madz_database_parser_test';
          +---------------+---------------------------+-----------------------------+------------+---------------------------+------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
          | TABLE_CATALOG | TABLE_SCHEMA              | TABLE_NAME                  | NON_UNIQUE | INDEX_SCHEMA              | INDEX_NAME | SEQ_IN_INDEX | COLUMN_NAME      | COLLATION | CARDINALITY | SUB_PART | PACKED | NULLABLE | INDEX_TYPE | COMMENT | INDEX_COMMENT |
          +---------------+---------------------------+-----------------------------+------------+---------------------------+------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
          | def           | madz_database_parser_test | table_with_single_column_pk |          0 | madz_database_parser_test | PRIMARY    |            1 | single_column_pk | A         |           0 |     NULL | NULL   |          | BTREE      |         |               |
          +---------------+---------------------------+-----------------------------+------------+---------------------------+------------+--------------+------------------+-----------+-------------+----------+--------+----------+------------+---------+---------------+
          1 row in set (0.00 sec)
          
          mysql> select * from columns where table_schema = 'madz_database_parser_test' and table_name = 'table_with_single_column_pk'\G
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
      val pk = result.getTable("table_with_single_column_pk").getPrimaryKey()
      val column = result.getTable("table_with_single_column_pk").getColumn("single_column_pk")
    }

    it("should parse composite PK with multiple columns") {
      pending
    }

    it("should parse single column UNIQUE KEY") {
      pending
    }

    it("should parse composite UNIQUE KEY with multiple columns") {
      pending
    }

    it("should parse non-PK auto-incremental column") {
      pending
    }

    it("should parse single column index") {
      pending
    }

    it("should parse multiple column index with correct order") {
      pending
    }

    it("should parse single column FOREIGN KEY") {
      pending
    }

    it("should parse multiple columns FOREIGN KEY with correct order") {
      pending
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
    ColumnMetaData("table_with_all_data_types_p1", "BIT_COLUMN", 1, null, true, "bit", null, 1, null, null, null, "bit(1)") ::
      ColumnMetaData("table_with_all_data_types_p1", "BIT_PLUS_COLUMN", 2, null, true, "bit", null, 2, null, null, null, "bit(2)") ::
      ColumnMetaData("table_with_all_data_types_p1", "TINYINT_COLUMN", 3, null, true, "tinyint", null, 3, 0, null, null, "tinyint(1)") ::
      ColumnMetaData("table_with_all_data_types_p1", "TINYINT_PLUS_COLUMN", 4, null, true, "tinyint", null, 3, 0, null, null, "tinyint(8)") ::
      ColumnMetaData("table_with_all_data_types_p1", "TINYINT_UNSIGNED_COLUMN", 5, null, true, "tinyint", null, 3, 0, null, null, "tinyint(8)unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "BOOL_COLUMN", 6, null, true, "tinyint", null, 3, 0, null, null, "tinyint(1)") ::
      ColumnMetaData("table_with_all_data_types_p1", "BOOLEAN_COLUMN", 7, null, true, "tinyint", null, 3, 0, null, null, "tinyint(1)") ::
      ColumnMetaData("table_with_all_data_types_p1", "SMALLINT_COLUMN", 8, null, true, "smallint", null, 5, 0, null, null, "smallint(16)") ::
      ColumnMetaData("table_with_all_data_types_p1", "SMALLINT_UNSIGNED_COLUMN", 9, null, true, "smallint", null, 5, 0, null, null, "smallint(16)unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "MEDIUMINT_COLUMN", 10, null, true, "mediumint", null, 7, 0, null, null, "mediumint(24)") ::
      ColumnMetaData("table_with_all_data_types_p1", "MEDIUMINT_UNSIGNED_COLUMN", 11, null, true, "mediumint", null, 7, 0, null, null, "mediumint(24)unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "INT_COLUMN", 12, null, true, "int", null, 10, 0, null, null, "int(32)") ::
      ColumnMetaData("table_with_all_data_types_p1", "INT_UNSIGNED_COLUMN", 13, null, true, "int", null, 10, 0, null, null, "int(32)unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "INTEGER_COLUMN", 14, null, true, "int", null, 10, 0, null, null, "int(32)") ::
      ColumnMetaData("table_with_all_data_types_p1", "INTEGER_UNSIGNED_COLUMN", 15, null, true, "int", null, 10, 0, null, null, "int(32)unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "BIGINT_COLUMN", 16, null, true, "bigint", null, 19, 0, null, null, "bigint(64)") ::
      ColumnMetaData("table_with_all_data_types_p1", "BIGINT_UNSIGNED_COLUMN", 17, null, true, "bigint", null, 20, 0, null, null, "bigint(64)unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "FLOAT_COLUMN", 18, null, true, "float", null, 7, 4, null, null, "float(7,4)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DOUBLE_COLUMN", 19, null, true, "double", null, 64, 30, null, null, "double(64,30)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DOUBLE_PLUS_COLUMN", 20, null, true, "double", null, 128, 30, null, null, "double(128,30)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DECIMAL_COLUMN", 21, null, true, "decimal", null, 10, 0, null, null, "decimal(10,0)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DECIMAL_NO_SCALE_COLUMN", 22, null, true, "decimal", null, 65, 0, null, null, "decimal(65,0)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DECIMAL_SCALE_COLUMN", 23, null, true, "decimal", null, 65, 30, null, null, "decimal(65,30)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DATE_COLUMN", 24, null, true, "date", null, null, null, null, null, "date") ::
      ColumnMetaData("table_with_all_data_types_p1", "DATETIME_COLUMN", 25, null, true, "datetime", null, null, null, null, null, "datetime") ::
      ColumnMetaData("table_with_all_data_types_p1", "TIMESTAMP_COLUMN", 26, "2010-12-1014:12:09", false, "timestamp", null, null, null, null, null, "timestamp") ::
      ColumnMetaData("table_with_all_data_types_p1", "TIME_COLUMN", 27, null, true, "time", null, null, null, null, null, "time") ::
      ColumnMetaData("table_with_all_data_types_p1", "YEAR_COLUMN", 28, null, true, "year", null, null, null, null, null, "year(2)") ::
      ColumnMetaData("table_with_all_data_types_p1", "YEAR_PLUS_COLUMN", 29, null, true, "year", null, null, null, null, null, "year(4)") ::
      ColumnMetaData("table_with_all_data_types_p1", "CHAR_COLUMN", 30, null, true, "char", 255, null, null, "latin7", "latin7_general_ci", "char(255)") ::
      ColumnMetaData("table_with_all_data_types_p1", "BINARY_COLUMN", 31, null, true, "binary", 255, null, null, null, null, "binary(255)") :: Nil

  val columns_in_table2 =
    ColumnMetaData("table_with_all_data_types_p2", "VARBINARY_COLUMN", 1, null, true, "varbinary", 65532, null, null, null, null, "varbinary(65532)") :: Nil

  val columns_in_table3 =
    ColumnMetaData("table_with_all_data_types_p3", "TINYBLOB_COLUMN", 1, null, true, "tinyblob", 255, null, null, null, null, "tinyblob") ::
      ColumnMetaData("table_with_all_data_types_p3", "TINYTEXT_COLUMN", 2, null, true, "tinytext", 255, null, null, "utf8", "utf8_general_ci", "tinytext") ::
      ColumnMetaData("table_with_all_data_types_p3", "BLOB_COLUMN", 3, null, true, "blob", 65535, null, null, null, null, "blob") ::
      ColumnMetaData("table_with_all_data_types_p3", "TEXT_COLUMN", 4, null, true, "mediumtext", 16777215L, null, null, "utf8", "utf8_general_ci", "mediumtext") ::
      ColumnMetaData("table_with_all_data_types_p3", "MEDIUMBLOB_COLUMN", 5, null, true, "mediumblob", 16777215L, null, null, null, null, "mediumblob") ::
      ColumnMetaData("table_with_all_data_types_p3", "MEDIUMTEXT_COLUMN", 6, null, true, "mediumtext", 16777215L, null, null, "utf8", "utf8_general_ci", "mediumtext") ::
      ColumnMetaData("table_with_all_data_types_p3", "LONGBLOB_COLUMN", 7, null, true, "longblob", 4294967295L, null, null, null, null, "longblob") ::
      ColumnMetaData("table_with_all_data_types_p3", "LONGTEXT_COLUMN", 8, null, true, "longtext", 4294967295L, null, null, "utf8", "utf8_general_ci", "longtext") ::
      ColumnMetaData("table_with_all_data_types_p3", "ENUM_COLUMN", 9, null, true, "enum", 1, null, null, "utf8", "utf8_general_ci", "enum('A','B','C')") ::
      ColumnMetaData("table_with_all_data_types_p3", "SET_COLUMN", 10, null, true, "set", 9, null, null, "utf8", "utf8_general_ci", "set('HLJ','JX','BJ')") :: Nil
  val columns_in_table4 =
    ColumnMetaData("table_with_all_data_types_p4", "VARCHAR_COLUMN", 1, null, true, "varchar", 65532, null, null, "latin7", "latin7_general_ci", "varchar(65532)") :: Nil
  val columns_in_table5 =
    ColumnMetaData("table_with_all_data_types_p5", "VARCHAR_BINARY_COLUMN", 1, null, true, "varchar", 65532, null, null, "latin7", "latin7_general_ci", "varchar(65532)") :: Nil

  def verifyColumns(expectedColumnList: List[ColumnMetaData], actualColumnList: java.util.List[MySQLColumnMetaData]) {
    val actualScalaList = collectionAsScalaIterable[MySQLColumnMetaData](actualColumnList).toList
    Assertions.assert(expectedColumnList.length == actualScalaList.length)
    val tuples = expectedColumnList zip actualScalaList
    tuples.foreach(pair =>
      compare(pair._1, pair._2))
  }

  def compare(expect: ColumnMetaData, actual: MySQLColumnMetaData) {
    Assertions.expectResult(expect.TABLE_NAME)(actual.getTableMetaData getTableName)
    Assertions.expectResult(expect.COLUMN_NAME)(actual getColumnName)
    Assertions.expectResult(expect.ORDINAL_POSITION)(actual getOrdinalPosition)
    Assertions.expectResult(expect.COLUMN_DEFAULT)(actual getDefaultValue)
    Assertions.expectResult(expect.IS_NULLABLE)(actual isNullable)
    Assertions.expectResult(expect.DATA_TYPE)(actual getSqlTypeName)
    Assertions.expectResult(expect.CHARACTER_MAXIMUM_LENGTH)(actual getSize)
    Assertions.expectResult(expect.NUMERIC_PRECISION)(actual)
    Assertions.expectResult(expect.NUMERIC_SCALE)(actual)
    Assertions.expectResult(expect.CHARACTER_SET_NAME)(actual getCharacterSet)
    Assertions.expectResult(expect.COLLATION_NAME)(actual getCollation)
    Assertions.expectResult(expect.COLUMN_TYPE)(actual.getColumnType.name.replaceAll("_", " "))
  }
}