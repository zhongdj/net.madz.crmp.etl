package net.madz.db.core.impl

import scala.collection.mutable.ListBuffer
import scala.slick.jdbc.{ StaticQuery => Q }
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession
import org.scalatest.FunSpec
import org.scalatest.BeforeAndAfterEach
import net.madz.db.core.impl.mysql.MySQLSchemaMetaDataParserImpl
import scala.slick.session.Database
import java.sql.Connection
import org.scalatest.Assertions
import net.madz.db.metadata.DottedPath

class MySQLSchemaMetaDataParserTest extends FunSpec with BeforeAndAfterEach with MySQLConnector {

  var conn: Connection = null
  var parser: MySQLSchemaMetaDataParserImpl = null

  override def beforeEach {
    conn = Database.forURL(urlRoot, user, password, prop).createSession.conn
    parser = new MySQLSchemaMetaDataParserImpl(database_name, conn)
  }

  override def afterEach {
    Database.forURL(urlRoot, user, password, prop) withSession {
      val q = Q.queryNA[String](drop_database_query)
      q.execute
    }
    if (null != conn) conn.close()
  }

  describe("Parse an Empty Database") {
    it("should parse an empty database") {

      Database.forURL(urlRoot, user, password, prop) withSession {
        Q.queryNA[String](drop_database_query).execute
        Q.queryNA[String](create_database_query).execute
      }

      val result = parser.parseSchemaMetaData()
      Assertions.expectResult(new DottedPath(database_name))(result.getSchemaPath())
      Assertions.expectResult(0)(result.getTables().size)
    }

    it("should parse an empty database with correction database level charsetEncoding configuration") {
      Database.forURL(urlRoot, user, password, prop) withSession {
        Q.queryNA[String](drop_database_query).execute
        Q.queryNA[String]("""
            CREATE DATABASE `madz_database_parser_test`	DEFAULT CHARACTER SET = `gbk`;
            """).execute
      }
      
      val result = parser.parseSchemaMetaData()
      Assertions.expectResult(new DottedPath(database_name))(result.getSchemaPath())
      Assertions.expectResult(0)(result.getTables().size)
      //Assertions.expectResult("gbk")(result.get)

    }
    it("should parse an empty database with correction database level collation configuration") {

    }
  }

  describe("Parse a single table Database") {
    it("should parse correct charsetEncoding with MySQL Parser") {
      pending
    }

    it("should parse correct storage engine with MySQL Parser") {
      pending
    }
  }

  describe("Parse columns from a single table Database") {
    it("should parse all kinds of data types defined in JDBC specification") {
      pending
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
  val create_table_with_all_data_types_DDL_1 = """
    CREATE TABLE `table_with_all_data_types_p1` (
      `BIT_COLUMN`                      BIT(1)                          DEFAULT NULL,
      `BIT_PLUS_COLUMN`                 BIT(2)                          DEFAULT NULL,
      `TINYINT_COLUMN`                  TINYINT(1)                      DEFAULT NULL,
      `TINYINT_PLUS_COLUMN`             TINYINT(8)                      DEFAULT NULL,
      `TINYINT_UNSIGNED_COLUMN`         TINYINT(8) UNSIGNED             DEFAULT NULL,
      `BOOL_COLUMN`                     TINYINT(1)                      DEFAULT NULL,
      `BOOLEAN_COLUMN`                  TINYINT(1)                      DEFAULT NULL,
      `SMALLINT_COLUMN`                 SMALLINT(16)                    DEFAULT NULL,
      `SMALLINT_UNSIGNED_COLUMN`        SMALLINT(16) UNSIGNED           DEFAULT NULL,
      `MEDIUMINT_COLUMN`                MEDIUMINT(24)                   DEFAULT NULL,
      `MEDIUMINT_UNSIGNED_COLUMN`       MEDIUMINT(24) UNSIGNED          DEFAULT NULL,
      `INT_COLUMN`                      INT(32)                         DEFAULT NULL,
      `INT_UNSIGNED_COLUMN`             INT(32) UNSIGNED                DEFAULT NULL,
      `INTEGER_COLUMN`                  INTEGER(32)                     DEFAULT NULL,
      `INTEGER_UNSIGNED_COLUMN`         INTEGER(32) UNSIGNED            DEFAULT NULL,
      `BIGINT_COLUMN`                   BIGINT(64)                      DEFAULT NULL,                   
      `BIGINT_UNSIGNED_COLUMN`          BIGINT(64) UNSIGNED             DEFAULT NULL,
      `FLOAT_COLUMN`                    FLOAT(7,4)                      DEFAULT NULL,
      `DOUBLE_COLUMN`                   DOUBLE PRECISION (64,30)        DEFAULT NULL,
      `DOUBLE_PLUS_COLUMN`              DOUBLE PRECISION (128,30)       DEFAULT NULL,
      `DECIMAL_COLUMN`                  DECIMAL                         DEFAULT NULL,
      `DECIMAL_NO_SCALE_COLUMN`         DECIMAL(65, 0)                  DEFAULT NULL,
      `DECIMAL_SCALE_COLUMN`            DECIMAL(65, 30)                 DEFAULT NULL,
      `DATE_COLUMN`                     DATE                            DEFAULT NULL,
      `DATETIME_COLUMN`                 DATETIME                        DEFAULT NULL,
      `TIMESTAMP_COLUMN`                TIMESTAMP                       DEFAULT '2010-12-10 14:12:09',
      `TIME_COLUMN`                     TIME                            DEFAULT NULL,
      `YEAR_COLUMN`                     YEAR(2)                         DEFAULT NULL,
      `YEAR_PLUS_COLUMN`                YEAR(4)                         DEFAULT NULL,
      `CHAR_COLUMN`                     CHAR(255)                       DEFAULT NULL,
      `VARCHAR_COLUMN`                  VARCHAR(65535)                  DEFAULT NULL,
      `VARCHAR_BINARY_COLUMN`           VARCHAR(65535)  BINARY          DEFAULT NULL,
      `BINARY_COLUMN`                   BINARY(255)                     DEFAULT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=UTF8;
  """
  val create_table_with_all_data_types_DDL_2 = """
    CREATE TABLE `table_with_all_data_types_p2` (
      `VARBINARY_COLUMN`                VARBINARY(65532)                DEFAULT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=UTF8;
  """
  val create_table_with_all_data_types_DDL_3 = """
    CREATE TABLE `table_with_all_data_types_p3` (
      `TINYBLOB_COLUMN`                 TINYBLOB                        DEFAULT NULL,
      `TINYTEXT_COLUMN`                 TINYTEXT                        DEFAULT NULL,
      `BLOB_COLUMN`                     BLOB                            DEFAULT NULL,
      `TEXT_COLUMN`                     TEXT(65535)                     DEFAULT NULL,
      `MEDIUMBLOB_COLUMN`               MEDIUMBLOB                      DEFAULT NULL,
      `MEDIUMTEXT_COLUMN`               MEDIUMTEXT                      DEFAULT NULL,
      `LONGBLOB_COLUMN`                 LONGBLOB                        DEFAULT NULL,
      `LONGTEXT_COLUMN`                 LONGTEXT                        DEFAULT NULL,
      `ENUM_COLUMN`                     ENUM('A','B','C')               DEFAULT NULL,
      `SET_COLUMN`                      SET('HLJ','JX','BJ')            DEFAULT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=UTF8;
  """
}