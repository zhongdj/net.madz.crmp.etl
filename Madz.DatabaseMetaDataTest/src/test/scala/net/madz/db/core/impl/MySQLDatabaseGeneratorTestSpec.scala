package net.madz.db.core.impl

import java.sql.Connection

import scala.collection.mutable.ListBuffer
import scala.slick.jdbc.{ StaticQuery => Q }
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession

import org.scalatest.Assertions
import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSpec

import net.madz.db.core.impl.mysql.MySQLDatabaseGenerator
import net.madz.db.metadata.DottedPath
import net.madz.db.metadata.jdbc.JdbcSchemaMetaData
import net.madz.db.metadata.jdbc.impl.builder.JdbcSchemaMetaDataBuilder

class MySQLDatabaseGeneratorTestSpec extends FunSpec with BeforeAndAfterEach with MySQLConnector {

  var conn: Connection = null
  var generator: MySQLDatabaseGenerator = null

  override def beforeEach {
    conn = Database.forURL(urlRoot, user, password, prop).createSession.conn
    generator = new MySQLDatabaseGenerator(conn)
  }

  override def afterEach {
    Database.forURL(urlRoot, user, password, prop) withSession {
      Q.queryNA[String](drop_database_query).execute
    }
    if (null != conn) conn.close()
  }

  describe("Generate an Empty Database") {
    it("should generate an empty database with a specified database name") {

      val schemaMetaData: JdbcSchemaMetaData = new JdbcSchemaMetaDataBuilder(conn, new DottedPath(databaseName)).build()
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
      val schemaMetaData: JdbcSchemaMetaData = new JdbcSchemaMetaDataBuilder(conn, new DottedPath(databaseName)).build()
      //add table meta data into schemaMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, databaseName)

      Database.forURL(urlRoot + databaseName, user, password, prop) withSession {
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
  val databaseName = "madz_database_generator_test"
  val drop_database_query = "DROP DATABASE IF EXISTS " + databaseName + ";"
  val show_tables_query = "SHOW tables;"

}