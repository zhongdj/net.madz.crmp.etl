package net.madz.db.core.impl

import java.sql.Connection
import scala.slick.session.Database
import org.scalatest.Assertions
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSpec
import net.madz.db.core.impl.mysql.MySQLSchemaMetaDataParserImpl
import net.madz.db.metadata.DottedPath
import net.madz.db.metadata.mysql.MySQLTableMetaData
import net.madz.db.metadata.mysql.MySQLTableTypeEnum
import net.madz.db.metadata.mysql.MySQLEngineEnum

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
           CREATE DATABASE `madz_database_parser_test`	DEFAULT CHARACTER SET = `gbk` DEFAULT COLLATE = `gbk_chinese_ci`;
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
           CREATE DATABASE `madz_database_parser_test`	DEFAULT CHARACTER SET = `gbk` DEFAULT COLLATE = `gbk_chinese_ci`;
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
      Assertions.expectResult(MySQLTableTypeEnum.base_table)(table getTableType)
    }

  }

  describe("Parse columns from a single table Database") {
    it("should parse all kinds of data types defined in MySQL documents") {
      exec(
        "USE `madz_database_parser_test`;"
          :: create_table_with_all_data_types_DDL_1
          :: create_table_with_all_data_types_DDL_2
          :: create_table_with_all_data_types_DDL_3
          :: Nil)

      val result = parser parseSchemaMetaData

      val table1 = result.getTable("table_with_all_data_types_p1")
      Assertions.expectResult(33)(table1.getColumns size)
//      val columnNames = 
//           ("BIT_COLUMN","BIT",1) 
//        :: ("BIT_PLUS_COLUMN", "BIT", 2) 
//        :: ("TINYINT_COLUMN", "TINYINT", 1) 
//        :: ("TINYINT_PLUS_COLUMN", "TINYINT", 8) 
//        :: Nil
//      columnNames.foreach(columnName =>
//        {
//          val column = table1.getColumn(columnName)
//          Assertions.expectResult(columnName)(column.getColumnName)
//          Assertions.expectResult(columnName)(column.getSqlTypeName())
//          
//        })

    }

    it("should parse column with modifier NULLABLE") {
      pending
    }

    it("should parse column with modifier DEFAULT") {
      pending
    }

    it("should parse column with correct collation") {
      pending
    }
  }

  describe("Parse indexes from Database") {
    it("should parse single column PK index") {
      pending
    }

    it("should parse auto incremental index") {
      pending
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
}