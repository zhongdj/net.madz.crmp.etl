package net.madz.db.core.impl

import java.sql.Connection
import scala.slick.jdbc.{ StaticQuery => Q }
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession
import org.scalatest.Assertions
import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSpec
import net.madz.crmp.db.core.impl.MySQLDatabaseGenerator
import net.madz.crmp.db.metadata.SchemaMetaData
import scala.collection.mutable.ListBuffer

class MySQLDatabaseGeneratorTestSpec extends FunSpec with BeforeAndAfterEach {

  var conn: Connection = null
  var generator: MySQLDatabaseGenerator = null

  def urlRoot = { "jdbc:mysql://localhost:3306/" }
  def user = { "root" }
  def password = { "1q2w3e4r5t" }

  override def beforeEach {
    conn = Database.forURL(urlRoot, user, password, driver = "com.mysql.jdbc.Driver").createSession.conn
    generator = new MySQLDatabaseGenerator(conn)
  }

  override def afterEach {
    if (null != conn) conn.close()
  }

  describe("Generate an Empty Database") {
    it("should generate an empty database with a specified database name") {

      val databaseName = "madz_empty_database_test"
      val schemaMetaData: SchemaMetaData = new SchemaMetaData(databaseName)
      val generatedDbName = generator.generateDatabase(schemaMetaData, databaseName)

      Database.forURL(urlRoot + databaseName, user, password, driver = "com.mysql.jdbc.Driver") withSession {
        val q = Q.queryNA[String](show_tables_query)
        Assertions.expectResult(0)(q.list().size)
      }
    }
  }

  describe("Generate correct tables") {

    it("should generate table with specific storage engine, such as InnoDB") {
      pending
    }

    it("should generate table with specific charsetEncoding") {
      pending
    }
  }

  describe("Generate correct columns") {

    it("should generate with all kinds of data type defined in JDBC specification") {
      val databaseName = "madz_database_test"
      val schemaMetaData: SchemaMetaData = new SchemaMetaData(databaseName)
      //add table meta data into schemaMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, databaseName)

      Database.forURL(urlRoot + databaseName, user, password, driver = "com.mysql.jdbc.Driver") withSession {
        val q = Q.queryNA[String](show_tables_query)
        //Assertions.expectResult(0)(q.list().size)
      }

      val rs = conn.getMetaData().getTables(databaseName, null, null, Array("Table"))
      val tables: ListBuffer[String] = ListBuffer()
      while (rs.next())
        tables += rs.getString("TABLE_NAME")
      tables.foreach(tableName => {
        val column = conn.getMetaData().getColumns(databaseName, null, tableName, null);
        while (column.next()) {
          println(column.getString("COLUMN_NAME") + "\t" + column.getInt("DATA_TYPE"))
        }
      })

    }

    it("should generate columns with modifier as NULLABLE(true or false)") {
      pending
    }

    it("should generate columns with modifier as DEFAULT value") {
      pending
    }

    it("should generate columns with specific collation") {
      pending
    }

  }

  describe("Generate correct indexes") {

    it("should generate with single column PK") {
      pending
    }

    it("should generate with auto incremental PK") {
      pending
    }

    it("should generate with composite PK with multiple columns in a specific order") {
      pending
    }

    it("should generate with single column UNIQUE KEY") {
      pending
    }

    it("should generate with composite UNIQUE KEY with multiple columns in a specific order") {
      pending
    }

    it("should generate with auto incremental KEY (non-PK)") {
      pending
    }

    it("should generate with single column index") {
      pending
    }

    it("should generate with multiple columns index in a specific order") {
      pending
    }

    it("should generate with single column FOREIGN KEY") {
      pending
    }

    it("should generate with multiple columns FOREIGN KEY in a specific order") {
      pending
    }

  }
  val show_tables_query = "SHOW tables;"
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