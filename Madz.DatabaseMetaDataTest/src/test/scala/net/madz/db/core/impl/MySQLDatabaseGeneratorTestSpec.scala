package net.madz.db.core.impl

import java.sql.Connection

import scala.collection.mutable.ListBuffer
import scala.slick.jdbc.{ StaticQuery => Q }
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession

import org.scalatest.Assertions
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSpec

import net.madz.db.core.impl.mysql.MySQLDatabaseGeneratorImpl
import net.madz.db.core.meta.DottedPath
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum
import net.madz.db.core.meta.immutable.types.TableType
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder
import net.madz.db.core.meta.mutable.mysql.impl.MySQLColumnMetaDataBuilderImpl
import net.madz.db.core.meta.mutable.mysql.impl.MySQLSchemaMetaDataBuilderImpl
import net.madz.db.core.meta.mutable.mysql.impl.MySQLTableMetaDataBuilderImpl

class MySQLDatabaseGeneratorTestSpec extends FunSpec with BeforeAndAfterEach with MySQLCommandLine {

  var conn: Connection = null
  var generator: MySQLDatabaseGeneratorImpl = null

  override def beforeEach {
    conn = Database.forURL(urlRoot, user, password, prop).createSession.conn
    generator = new MySQLDatabaseGeneratorImpl()
  }

  override def afterEach {
    Database.forURL(urlRoot, user, password, prop) withSession {
      Q.queryNA[String](drop_database_query).execute
    }
    if (null != conn) conn.close()
  }

  describe("Generate an Empty Database") {
    it("should generate an empty database with a specified database name") {

      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = new MySQLSchemaMetaDataBuilderImpl(new DottedPath(databaseName))
      schemaMetaDataBuilder setCharSet "utf8"
      schemaMetaDataBuilder setCollation "utf8_bin"

      val schemaMetaData: MySQLSchemaMetaData = schemaMetaDataBuilder getMetaData

      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)

      Database.forURL(urlRoot + databaseName, user, password, driver = "com.mysql.jdbc.Driver") withSession {
        val q = Q.queryNA[String](show_tables_query)
        Assertions.expectResult(0)(q.list().size)
        Q.queryNA[String]("USE INFORMATION_SCHEMA;").execute
        val db = Q.query[String, MySQLSchema]("""
            SELECT 
                CATALOG_NAME,SCHEMA_NAME, DEFAULT_CHARACTER_SET_NAME,DEFAULT_COLLATION_NAME,SQL_PATH
            FROM 
                SCHEMATA
            WHERE
                SCHEMA_NAME=?
            """).list(databaseName)

        Assertions.expectResult(1)(db.size)
        Assertions.expectResult("utf8")(db(0).defaultCharset)
        Assertions.expectResult("utf8_bin")(db(0).defaultCollation)
      }

    }
  }

  describe("Generate correct tables") {

    it("should generate table with specific storage engine, such as InnoDB, charsetEncoding") {
      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = new MySQLSchemaMetaDataBuilderImpl(new DottedPath(databaseName))
      schemaMetaDataBuilder setCharSet "utf8"
      schemaMetaDataBuilder setCollation "utf8_bin"
      val tableMetaDataBuilder: MySQLTableMetaDataBuilder = new MySQLTableMetaDataBuilderImpl(schemaMetaDataBuilder, "test_table")
      tableMetaDataBuilder.setRemarks("Test Table Comments")
      tableMetaDataBuilder.setType(TableType.table)
      tableMetaDataBuilder.setCharacterSet("gbk")
      tableMetaDataBuilder.setCollation("gbk_bin")
      tableMetaDataBuilder.setEngine(MySQLEngineEnum.MyISAM)
      val columnMetaDataBuilder: MySQLColumnMetaDataBuilder = new MySQLColumnMetaDataBuilderImpl(tableMetaDataBuilder, "test_column")
      //Which setter is useful?
      columnMetaDataBuilder.setCharacterMaximumLength(20)
      columnMetaDataBuilder.setCharacterSet(null)
      columnMetaDataBuilder.setColumnKey("")
      columnMetaDataBuilder.setColumnType("bigint(20)")
      columnMetaDataBuilder.setExtra("")
      columnMetaDataBuilder.setNumericPrecision(20)
      columnMetaDataBuilder.setNumericScale(20)
      columnMetaDataBuilder.setAutoIncremented(false)
      columnMetaDataBuilder.setCharacterOctetLength(20)
      columnMetaDataBuilder.setDefaultValue(null)
      columnMetaDataBuilder.setNullable(false)
      columnMetaDataBuilder.setOrdinalPosition(new java.lang.Short("1"))
      //val pk : MySQLIndexMetaData = new MySQLIndexMetaDataBuilderImpl
      //columnMetaDataBuilder.setPrimaryKey(pk)
      columnMetaDataBuilder.setRadix(2)
      columnMetaDataBuilder.setRemarks("Test Column Comment")
      columnMetaDataBuilder.setSize(20)

      tableMetaDataBuilder.appendColumnMetaDataBuilder(columnMetaDataBuilder)
      schemaMetaDataBuilder.appendTableMetaDataBuilder(tableMetaDataBuilder)

      val schemaMetaData: MySQLSchemaMetaData = schemaMetaDataBuilder.getMetaData()
      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)

      Database.forURL(urlRoot + databaseName, user, password, driver = "com.mysql.jdbc.Driver") withSession {
        val q = Q.queryNA[String](show_tables_query)
        Assertions.expectResult(1)(q.list().size)
        Q.queryNA[String]("USE INFORMATION_SCHEMA;").execute
        val db = Q.query[String, MySQLSchema]("""
            SELECT 
                CATALOG_NAME,SCHEMA_NAME, DEFAULT_CHARACTER_SET_NAME,DEFAULT_COLLATION_NAME,SQL_PATH
            FROM 
                SCHEMATA
            WHERE
                SCHEMA_NAME=?
            """).list(databaseName)

        Assertions.expectResult(1)(db.size)
        Assertions.expectResult("utf8")(db(0).defaultCharset)
        Assertions.expectResult("utf8_bin")(db(0).defaultCollation)

        val tables = Q.query[(String, String), MySQLTable](""" 
            SELECT 
                TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, TABLE_TYPE, ENGINE, VERSION, ROW_FORMAT, TABLE_ROWS, AVG_ROW_LENGTH,
                DATA_LENGTH, MAX_DATA_LENGTH, INDEX_LENGTH, DATA_FREE, AUTO_INCREMENT, CREATE_TIME, UPDATE_TIME,CHECK_TIME,
                TABLE_COLLATION, CHECKSUM, CREATE_OPTIONS, TABLE_COMMENT
            FROM 
                TABLES
            WHERE 
                TABLE_SCHEMA=? AND TABLE_NAME=?
            """).list((databaseName, "test_table"))

        Assertions.expectResult(1)(tables.size)
        Assertions.expectResult("gbk_bin")(tables(0).tableCollation)
        Assertions.expectResult("BASE TABLE")(tables(0).tableType.toUpperCase)
        Assertions.expectResult("Test Table Comments")(tables(0).tableComment)
        Assertions.expectResult(MySQLEngineEnum.MyISAM.name.toLowerCase)(tables(0).engine.toLowerCase)
      }

    }

  }

  describe("Generate correct columns") {

    it("should generate with all kinds of data type defined in MySQL Manual") {
      
      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = new MySQLSchemaMetaDataBuilderImpl(new DottedPath(databaseName))
      schemaMetaDataBuilder setCharSet "utf8"
      schemaMetaDataBuilder setCollation "utf8_bin"
      
      val tableMetaDataBuilder: MySQLTableMetaDataBuilder = new MySQLTableMetaDataBuilderImpl(schemaMetaDataBuilder, "test_table")
      tableMetaDataBuilder.setRemarks("Test Table Comments")
      tableMetaDataBuilder.setType(TableType.table)
      tableMetaDataBuilder.setCharacterSet("gbk")
      tableMetaDataBuilder.setCollation("gbk_bin")
      tableMetaDataBuilder.setEngine(MySQLEngineEnum.MyISAM)
      val columnMetaDataBuilder: MySQLColumnMetaDataBuilder = new MySQLColumnMetaDataBuilderImpl(tableMetaDataBuilder, "test_column")
      
      val builder: MySQLSchemaMetaDataBuilder = null //(new DottedPath(databaseName))
      //builder.build(conn)
      val schemaMetaData: MySQLSchemaMetaData = null //builder.getCopy()

      //add table meta data into schemaMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)

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

    it("should generate BTREE mode index") {
      pending
    }

    it("should generate HASH mode index") {
      pending
    }

    it("should generate nullable index") {
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
  val create_database_query = "CREATE DATABASE " + databaseName + ";"
  val show_tables_query = "SHOW tables;"

}