package net.madz.db.core.impl

import java.sql.Connection
import scala.slick.jdbc.{ StaticQuery => Q }
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession
import org.scalatest.Assertions
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSpec
import net.madz.crmp.db.core.AbsDatabaseGenerator
import net.madz.crmp.db.core.impl.MySQLDatabaseGenerator
import net.madz.crmp.db.metadata.SchemaMetaData
import org.scalatest.BeforeAndAfterEach

class MySQLDatabaseGeneratorTestSpec extends FunSpec with BeforeAndAfterEach {
  val show_tables_query = "show tables;"
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
      val generatedDbName = generator.generateDatabase(schemaMetaData, "madz_empty_database_test")

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
      pending
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

}