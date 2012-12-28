package net.madz.db.core.impl

import scala.slick.session.Database
import org.scalatest.FunSpec
import Database.threadLocalSession
import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation
import net.madz.crmp.db.core.AbsDatabaseGenerator
import net.madz.crmp.db.metadata.SchemaMetaData
import org.scalatest.Assertions

class MySQLDatabaseGeneratorTestSpec extends FunSpec {
  
  def createGenerator(): AbsDatabaseGenerator = {
    //TODO give an implementation
    Database.forURL("jdbc:mysql://localhost:3306/madz_empty_database_test", "root", "1q2w3e4r5t", driver = "com.mysql.jdbc.Driver").createSession.conn
    null
  }
  
  describe("Generate an Empty Database") {
    it("should generate an empty database with a specified database name") {
      
      val generator = createGenerator()
      val schemaMetaData: SchemaMetaData = new SchemaMetaData("madz_empty_database_test")
      val generatedDbName = generator generateDatabase schemaMetaData
      
      Database.forURL("jdbc:mysql://localhost:3306/madz_empty_database_test", "root", "1q2w3e4r5t", driver = "com.mysql.jdbc.Driver") withSession {
         val q = Q.queryNA[String]("show tables;")
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