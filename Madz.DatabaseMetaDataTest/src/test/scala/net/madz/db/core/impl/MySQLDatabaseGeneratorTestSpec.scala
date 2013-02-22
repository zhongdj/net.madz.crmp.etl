package net.madz.db.core.impl

import java.sql.Connection
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
import net.madz.db.core.meta.mutable.mysql.impl.MySQLIndexMetaDataBuilderImpl
import net.madz.db.core.meta.immutable.types.KeyTypeEnum
import net.madz.db.core.meta.mutable.impl.BaseIndexMetaDataBuilder
import net.madz.db.core.meta.immutable.mysql.enums.MySQLIndexMethod

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

      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = makeSchema("utf8", "utf8_bin")

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
      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = makeSchema("utf8", "utf8_bin")
      val tableMetaDataBuilder: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, "test_table")
      tableMetaDataBuilder.setRemarks("Test Table Comments")
      tableMetaDataBuilder.setType(TableType.table)
      tableMetaDataBuilder.setCharacterSet("gbk")
      tableMetaDataBuilder.setCollation("gbk_bin")
      tableMetaDataBuilder.setEngine(MySQLEngineEnum.MyISAM)
      val atLeastOneColumn: MySQLColumnMetaDataBuilder = new MySQLColumnMetaDataBuilderImpl(tableMetaDataBuilder, "test_column")
      //Which setter is useful?
      atLeastOneColumn.setCharacterMaximumLength(20)
      atLeastOneColumn.setCharacterSet(null)
      atLeastOneColumn.setColumnKey("")
      atLeastOneColumn.setColumnType("bigint(20)")
      atLeastOneColumn.setExtra("")
      atLeastOneColumn.setNumericPrecision(20)
      atLeastOneColumn.setNumericScale(20)
      atLeastOneColumn.setAutoIncremented(false)
      atLeastOneColumn.setCharacterOctetLength(20)
      atLeastOneColumn.setDefaultValue(null)
      atLeastOneColumn.setNullable(false)
      atLeastOneColumn.setOrdinalPosition(new java.lang.Short("1"))
      //val pk : MySQLIndexMetaData = new MySQLIndexMetaDataBuilderImpl
      //columnMetaDataBuilder.setPrimaryKey(pk)
      atLeastOneColumn.setRadix(2)
      atLeastOneColumn.setRemarks("Test Column Comment")
      atLeastOneColumn.setSize(20)

      tableMetaDataBuilder.appendColumnMetaDataBuilder(atLeastOneColumn)
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

      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = makeSchema("utf8", "utf8_bin")

      val tableMetaDataBuilder1: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, "table_with_all_data_types_p1")
      makeColumns(tableMetaDataBuilder1, columns_in_table1)
      val tableMetaDataBuilder2: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, "table_with_all_data_types_p2")
      makeColumns(tableMetaDataBuilder2, columns_in_table2)
      val tableMetaDataBuilder3: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, "table_with_all_data_types_p3")
      makeColumns(tableMetaDataBuilder3, columns_in_table3)
      val tableMetaDataBuilder4: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, "table_with_all_data_types_p4")
      makeColumns(tableMetaDataBuilder4, columns_in_table4)
      val tableMetaDataBuilder5: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, "table_with_all_data_types_p5")
      makeColumns(tableMetaDataBuilder5, columns_in_table5)

      val schemaMetaData: MySQLSchemaMetaData = schemaMetaDataBuilder getMetaData

      //add table meta data into schemaMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)

      Database.forURL(urlRoot, user, password, prop) withSession {
        Q.queryNA[String]("use information_schema").execute
        Assertions.expectResult(columns_in_table1)(queryColumns("table_with_all_data_types_p1"))
        Assertions.expectResult(columns_in_table2)(queryColumns("table_with_all_data_types_p2"))
        Assertions.expectResult(columns_in_table3)(queryColumns("table_with_all_data_types_p3"))
        Assertions.expectResult(columns_in_table4)(queryColumns("table_with_all_data_types_p4"))
        Assertions.expectResult(columns_in_table5)(queryColumns("table_with_all_data_types_p5"))
      }

    }

    it("should generate columns with modifier as NULLABLE(true or false)") {
      val tableName = "nullable_column_test_table"
      val columns: List[MySQLColumn] =
        MySQLColumn(tableName, "nullable_COLUMN", 1, null, true, "bit", 0, 0, 1, 0, null, null, "bit(1)", "", "", "") ::
          MySQLColumn(tableName, "not_nullable_COLUMN", 2, null, false, "bit", 0, 0, 1, 0, null, null, "bit(1)", "", "", "") :: Nil

      verifyColumnFeature(tableName, columns)
    }

    it("should generate columns with modifier as DEFAULT value") {
      val tableName = "default_value_test_table"
      val columns: List[MySQLColumn] =
        MySQLColumn(tableName, "default_value_COLUMN", 1, "b'1'", true, "bit", 0, 0, 1, 0, null, null, "bit(1)", "", "", "") ::
          MySQLColumn(tableName, "no_default_value_COLUMN", 2, null, false, "bit", 0, 0, 1, 0, null, null, "bit(1)", "", "", "") :: Nil

      verifyColumnFeature(tableName, columns)
    }

    it("should generate columns with specific collation") {
      val tableName = "collation_test_table"
      val columns: List[MySQLColumn] =
        MySQLColumn(tableName, "VARCHAR_COLUMN", 1, null, false, "varchar", 255, 510, 0, 0, "gbk", "gbk_bin", "varchar(255)", "", "", "") ::
          MySQLColumn(tableName, "CHAR_COLUMN", 2, null, false, "char", 255, 510, 0, 0, "gbk", "gbk_chinese_ci", "char(255)", "", "", "") :: Nil

      verifyColumnFeature(tableName, columns)
    }

    def verifyColumnFeature(tableName: String, columns: List[MySQLColumn]): Unit = {
      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = makeSchema("utf8", "utf8_bin")
      val tableMetaDataBuilder1: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, tableName)
      makeColumns(tableMetaDataBuilder1, columns)
      val schemaMetaData: MySQLSchemaMetaData = schemaMetaDataBuilder getMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)
      Database.forURL(urlRoot, user, password, prop) withSession {
        Q.queryNA[String]("use information_schema").execute
        Assertions.expectResult(columns)(queryColumns(tableName))
      }
    }
  }

  describe("Generate correct indexes") {

    it("should generate with single column PK") {
      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = makeSchema("utf8", "utf8_bin")
      val tableName: String = "single_column_pk_table"
      val tableBuilder: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, tableName)
      val rawColumn = MySQLColumn(tableName, "pk_COLUMN", 1, null, false, "INTEGER", 0, 0, 32, 0, null, null, "INTEGER(32)", "", "", "")
      val column = makeColumn(tableBuilder, rawColumn)
      val pkIndex = new MySQLIndexMetaDataBuilderImpl(tableBuilder, "PRIMARY")
      pkIndex.setKeyType(KeyTypeEnum.primaryKey)

      //More attributes?

      val entry = new pkIndex.Entry(pkIndex, 0, column, 1.shortValue)
      //Do I need to bind them?
      column.appendUniqueIndexEntry(entry)
      pkIndex.addEntry(entry)
      tableBuilder.appendIndexMetaDataBuilder(pkIndex)

      val schemaMetaData: MySQLSchemaMetaData = schemaMetaDataBuilder getMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)

      Database.forURL(urlRoot, user, password, prop) withSession {
        Q.queryNA[String]("use information_schema").execute
        val indexes = Q.query[(String, String), MySQLStatistic]("""
           SELECT
                TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, NON_UNIQUE, INDEX_SCHEMA, 
                INDEX_NAME, SEQ_IN_INDEX, COLUMN_NAME, COLLATION, CARDINALITY,
                SUB_PART, PACKED, NULLABLE, INDEX_TYPE, COMMENT, INDEX_COMMENT
            FROM
                statistics
            WHERE
                TABLE_SCHEMA=? AND TABLE_NAME=?
        """).list((databaseName, tableName))

        Assertions.expectResult(1)(indexes.size)
        val expect = MySQLStatistic("def", "madz_database_generator_test", "single_column_pk_table", false, "madz_database_generator_test",
          "PRIMARY", 1, "pk_COLUMN", "A", 0, 0, false, false, "BTREE", "", "")
        Assertions.expectResult(expect)(indexes(0))

      }
    }

    it("should generate with auto incremental PK") {
      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = makeSchema("utf8", "utf8_bin")
      val tableName: String = "single_column_pk_table"
      val tableBuilder: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, tableName)
      val rawColumn = MySQLColumn(tableName, "pk_COLUMN", 1, null, false, "INTEGER", 0, 0, 32, 0, null, null, "INTEGER(32)", "", "", "")
      //difference
      val column = makeColumn(tableBuilder, rawColumn, true)

      val pkIndex = new MySQLIndexMetaDataBuilderImpl(tableBuilder, "PRIMARY")
      pkIndex.setKeyType(KeyTypeEnum.primaryKey)

      //More attributes?

      val entry = new pkIndex.Entry(pkIndex, 0, column, 1.shortValue)
      //Do I need to bind them?
      column.appendUniqueIndexEntry(entry)
      pkIndex.addEntry(entry)
      tableBuilder.appendIndexMetaDataBuilder(pkIndex)

      val schemaMetaData: MySQLSchemaMetaData = schemaMetaDataBuilder getMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)

      Database.forURL(urlRoot, user, password, prop) withSession {
        Q.queryNA[String]("use information_schema").execute
        val indexes = Q.query[(String, String), MySQLStatistic]("""
           SELECT
                TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, NON_UNIQUE, INDEX_SCHEMA, 
                INDEX_NAME, SEQ_IN_INDEX, COLUMN_NAME, COLLATION, CARDINALITY,
                SUB_PART, PACKED, NULLABLE, INDEX_TYPE, COMMENT, INDEX_COMMENT
            FROM
                statistics
            WHERE
                TABLE_SCHEMA=? AND TABLE_NAME=?
        """).list((databaseName, tableName))

        Assertions.expectResult(1)(indexes.size)
        val expect = MySQLStatistic("def", "madz_database_generator_test", "single_column_pk_table", false, "madz_database_generator_test",
          "PRIMARY", 1, "pk_COLUMN", "A", 0, 0, false, false, "BTREE", "", "")
        Assertions.expectResult(expect)(indexes(0))
        val generatedColumns = queryColumns(tableName)
        Assertions.assert("auto_increment".equalsIgnoreCase(generatedColumns(0).extra))
        Assertions.assert("PRI".equalsIgnoreCase(generatedColumns(0).columnKey))

      }

    }

    it("should generate with composite PK with multiple columns in a specific order") {

      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = makeSchema("utf8", "utf8_bin")
      val tableName: String = "single_column_pk_table"
      val tableBuilder: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, tableName)
      val rawColumn1 = MySQLColumn(tableName, "VARCHAR_COLUMN", 1, null, false, "varchar", 255, 510, 0, 0, "gbk", "gbk_bin", "varchar(255)", "", "", "")
      val rawColumn2 = MySQLColumn(tableName, "CHAR_COLUMN", 2, null, false, "char", 255, 510, 0, 0, "gbk", "gbk_chinese_ci", "char(255)", "", "", "")

      val column1 = makeColumn(tableBuilder, rawColumn1)
      val column2 = makeColumn(tableBuilder, rawColumn2)

      val pkIndex = new MySQLIndexMetaDataBuilderImpl(tableBuilder, "PRIMARY")
      pkIndex.setKeyType(KeyTypeEnum.primaryKey)

      //More attributes?

      val entry1 = new pkIndex.Entry(pkIndex, 0, column1, 1.shortValue)
      val entry2 = new pkIndex.Entry(pkIndex, 0, column2, 2.shortValue)
      //Do I need to bind them?
      column1.appendUniqueIndexEntry(entry1)
      column2.appendUniqueIndexEntry(entry2)
      pkIndex.addEntry(entry1)
      pkIndex.addEntry(entry2)
      tableBuilder.appendIndexMetaDataBuilder(pkIndex)

      val schemaMetaData: MySQLSchemaMetaData = schemaMetaDataBuilder getMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)

      Database.forURL(urlRoot, user, password, prop) withSession {
        Q.queryNA[String]("use information_schema").execute
        val indexes = Q.query[(String, String), MySQLStatistic]("""
           SELECT
                TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, NON_UNIQUE, INDEX_SCHEMA, 
                INDEX_NAME, SEQ_IN_INDEX, COLUMN_NAME, COLLATION, CARDINALITY,
                SUB_PART, PACKED, NULLABLE, INDEX_TYPE, COMMENT, INDEX_COMMENT
            FROM
                statistics
            WHERE
                TABLE_SCHEMA=? AND TABLE_NAME=?
            ORDER BY 
                SEQ_IN_INDEX ASC
        """).list((databaseName, tableName))

        Assertions.expectResult(2)(indexes.size)
        val expect1 = MySQLStatistic("def", "madz_database_generator_test", "single_column_pk_table", false, "madz_database_generator_test",
          "PRIMARY", 1, "VARCHAR_COLUMN", "A", 0, 0, false, false, "BTREE", "", "")
        val expect2 = MySQLStatistic("def", "madz_database_generator_test", "single_column_pk_table", false, "madz_database_generator_test",
          "PRIMARY", 2, "CHAR_COLUMN", "A", 0, 0, false, false, "BTREE", "", "")
        Assertions.expectResult(expect1)(indexes(0))
        Assertions.expectResult(expect2)(indexes(1))

      }

    }

    it("should generate HASH mode index") {
      
      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = makeSchema("utf8", "utf8_bin")
      val tableName: String = "single_column_pk_table"
      val tableBuilder: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, tableName)
      tableBuilder.setEngine(MySQLEngineEnum.MEMORY)
      val rawColumn = MySQLColumn(tableName, "pk_COLUMN", 1, null, false, "INTEGER", 0, 0, 32, 0, null, null, "INTEGER(32)", "", "", "")
      val column = makeColumn(tableBuilder, rawColumn)
      val pkIndex = new MySQLIndexMetaDataBuilderImpl(tableBuilder, "PRIMARY")
      pkIndex.setKeyType(KeyTypeEnum.primaryKey)
      pkIndex.setIndexMethod(MySQLIndexMethod.hash)
      //More attributes?

      val entry = new pkIndex.Entry(pkIndex, 0, column, 1.shortValue)
      //Do I need to bind them?
      column.appendUniqueIndexEntry(entry)
      pkIndex.addEntry(entry)
      tableBuilder.appendIndexMetaDataBuilder(pkIndex)

      val schemaMetaData: MySQLSchemaMetaData = schemaMetaDataBuilder getMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)

      Database.forURL(urlRoot, user, password, prop) withSession {
        Q.queryNA[String]("use information_schema").execute
        val indexes = Q.query[(String, String), MySQLStatistic]("""
           SELECT
                TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, NON_UNIQUE, INDEX_SCHEMA, 
                INDEX_NAME, SEQ_IN_INDEX, COLUMN_NAME, COLLATION, CARDINALITY,
                SUB_PART, PACKED, NULLABLE, INDEX_TYPE, COMMENT, INDEX_COMMENT
            FROM
                statistics
            WHERE
                TABLE_SCHEMA=? AND TABLE_NAME=?
        """).list((databaseName, tableName))

        Assertions.expectResult(1)(indexes.size)
        val expect = MySQLStatistic("def", "madz_database_generator_test", "single_column_pk_table", false, "madz_database_generator_test",
          "PRIMARY", 1, "pk_COLUMN", null, 0, 0, false, false, "HASH", "", "")
        Assertions.expectResult(expect)(indexes(0))

      }
    
    }

    it("should generate nullable index") {
      
      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = makeSchema("utf8", "utf8_bin")
      val tableName: String = "single_column_pk_table"
      val tableBuilder: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, tableName)
      val rawColumn = MySQLColumn(tableName, "pk_COLUMN", 1, null, false, "INTEGER", 0, 0, 32, 0, null, null, "INTEGER(32)", "", "", "")
      val column = makeColumn(tableBuilder, rawColumn)
      val pkIndex = new MySQLIndexMetaDataBuilderImpl(tableBuilder, "PRIMARY")
      pkIndex.setKeyType(KeyTypeEnum.primaryKey)
      pkIndex.setNull(true)
      //More attributes?

      val entry = new pkIndex.Entry(pkIndex, 0, column, 1.shortValue)
      //Do I need to bind them?
      column.appendUniqueIndexEntry(entry)
      pkIndex.addEntry(entry)
      tableBuilder.appendIndexMetaDataBuilder(pkIndex)

      val schemaMetaData: MySQLSchemaMetaData = schemaMetaDataBuilder getMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)

      Database.forURL(urlRoot, user, password, prop) withSession {
        Q.queryNA[String]("use information_schema").execute
        val indexes = Q.query[(String, String), MySQLStatistic]("""
           SELECT
                TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, NON_UNIQUE, INDEX_SCHEMA, 
                INDEX_NAME, SEQ_IN_INDEX, COLUMN_NAME, COLLATION, CARDINALITY,
                SUB_PART, PACKED, NULLABLE, INDEX_TYPE, COMMENT, INDEX_COMMENT
            FROM
                statistics
            WHERE
                TABLE_SCHEMA=? AND TABLE_NAME=?
        """).list((databaseName, tableName))

        Assertions.expectResult(1)(indexes.size)
        val expect = MySQLStatistic("def", "madz_database_generator_test", "single_column_pk_table", false, "madz_database_generator_test",
          "PRIMARY", 1, "pk_COLUMN", "A", 0, 0, false, false, "BTREE", "", "")
        Assertions.expectResult(expect)(indexes(0))

      }
    
    
    }

    it("should generate with single column UNIQUE KEY") {
      
      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = makeSchema("utf8", "utf8_bin")
      val tableName: String = "single_column_unique_key_table"
      val tableBuilder: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, tableName)
      val rawColumn = MySQLColumn(tableName, "unique_key_COLUMN", 1, null, false, "INTEGER", 0, 0, 32, 0, null, null, "INTEGER(32)", "", "", "")
      val column = makeColumn(tableBuilder, rawColumn)
      val uniqueIndex = new MySQLIndexMetaDataBuilderImpl(tableBuilder, "TEST UNIQUE INDEX")
      uniqueIndex.setKeyType(KeyTypeEnum.uniqueKey)
      uniqueIndex.setNull(true)
      //More attributes?

      val entry = new uniqueIndex.Entry(uniqueIndex, 0, column, 1.shortValue)
      //Do I need to bind them?
      column.appendUniqueIndexEntry(entry)
      uniqueIndex.addEntry(entry)
      tableBuilder.appendIndexMetaDataBuilder(uniqueIndex)

      val schemaMetaData: MySQLSchemaMetaData = schemaMetaDataBuilder getMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)

      Database.forURL(urlRoot, user, password, prop) withSession {
        Q.queryNA[String]("use information_schema").execute
        val indexes = Q.query[(String, String), MySQLStatistic]("""
           SELECT
                TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, NON_UNIQUE, INDEX_SCHEMA, 
                INDEX_NAME, SEQ_IN_INDEX, COLUMN_NAME, COLLATION, CARDINALITY,
                SUB_PART, PACKED, NULLABLE, INDEX_TYPE, COMMENT, INDEX_COMMENT
            FROM
                statistics
            WHERE
                TABLE_SCHEMA=? AND TABLE_NAME=?
        """).list((databaseName, tableName))

        Assertions.expectResult(1)(indexes.size)
        val expect = MySQLStatistic("def", "madz_database_generator_test", "single_column_unique_key_table", false, "madz_database_generator_test",
          "TEST UNIQUE INDEX", 1, "unique_key_COLUMN", "A", 0, 0, false, true, "BTREE", "", "")
        Assertions.expectResult(expect)(indexes(0))

      }
    
    }

    it("should generate with composite UNIQUE KEY with multiple columns in a specific order") {
      
      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = makeSchema("utf8", "utf8_bin")
      val tableName: String = "composite_unique_key_table"
      val tableBuilder: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, tableName)
      val rawColumn1 = MySQLColumn(tableName, "unique_key_1_COLUMN", 1, null, false, "INTEGER", 0, 0, 32, 0, null, null, "INTEGER(32)", "", "", "")
      val rawColumn2 = MySQLColumn(tableName, "unique_key_2_COLUMN", 1, null, false, "INTEGER", 0, 0, 32, 0, null, null, "INTEGER(32)", "", "", "")
      val column1 = makeColumn(tableBuilder, rawColumn1)
      val column2 = makeColumn(tableBuilder, rawColumn2)
      val uniqueIndex = new MySQLIndexMetaDataBuilderImpl(tableBuilder, "TEST UNIQUE INDEX")
      uniqueIndex.setKeyType(KeyTypeEnum.uniqueKey)
      uniqueIndex.setNull(true)
      //More attributes?

      val entry1 = new uniqueIndex.Entry(uniqueIndex, 0, column1, 1.shortValue)
      val entry2 = new uniqueIndex.Entry(uniqueIndex, 0, column2, 2.shortValue)
      //Do I need to bind them?
      column1.appendUniqueIndexEntry(entry1)
      column2.appendUniqueIndexEntry(entry2)
      uniqueIndex.addEntry(entry1)
      uniqueIndex.addEntry(entry2)
      tableBuilder.appendIndexMetaDataBuilder(uniqueIndex)

      val schemaMetaData: MySQLSchemaMetaData = schemaMetaDataBuilder getMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)

      Database.forURL(urlRoot, user, password, prop) withSession {
        Q.queryNA[String]("use information_schema").execute
        val indexes = Q.query[(String, String), MySQLStatistic]("""
           SELECT
                TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, NON_UNIQUE, INDEX_SCHEMA, 
                INDEX_NAME, SEQ_IN_INDEX, COLUMN_NAME, COLLATION, CARDINALITY,
                SUB_PART, PACKED, NULLABLE, INDEX_TYPE, COMMENT, INDEX_COMMENT
            FROM
                statistics
            WHERE
                TABLE_SCHEMA=? AND TABLE_NAME=?
        """).list((databaseName, tableName))

        Assertions.expectResult(2)(indexes.size)
        val expect = MySQLStatistic("def", "madz_database_generator_test", "composite_unique_key_table", false, "madz_database_generator_test",
          "TEST UNIQUE INDEX", 1, "unique_key_1_COLUMN", "A", 0, 0, false, true, "BTREE", "", "") ::
          MySQLStatistic("def", "madz_database_generator_test", "composite_unique_key_table", false, "madz_database_generator_test",
          "TEST UNIQUE INDEX", 2, "unique_key_2_COLUMN", "A", 0, 0, false, true, "BTREE", "", "") :: Nil
        Assertions.expectResult(expect)(indexes)

      }
    
    }

    it("should generate with auto incremental KEY (non-PK)") {
      
      val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = makeSchema("utf8", "utf8_bin")
      val tableName: String = "npk_incremental_table"
      val tableBuilder: MySQLTableMetaDataBuilder = makeTable(schemaMetaDataBuilder, tableName)
      val rawColumn = MySQLColumn(tableName, "npk_incremental_COLUMN", 1, null, false, "INTEGER", 0, 0, 32, 0, null, null, "INTEGER(32)", "", "", "")
      //difference
      val column = makeColumn(tableBuilder, rawColumn, true)

      val npkIncrementalIndex = new MySQLIndexMetaDataBuilderImpl(tableBuilder, "NON_PK_INCREMENTAL")
      npkIncrementalIndex.setKeyType(KeyTypeEnum.uniqueKey)

      //More attributes?

      val entry = new npkIncrementalIndex.Entry(npkIncrementalIndex, 0, column, 1.shortValue)
      //Do I need to bind them?
      column.appendUniqueIndexEntry(entry)
      npkIncrementalIndex.addEntry(entry)
      tableBuilder.appendIndexMetaDataBuilder(npkIncrementalIndex)

      val schemaMetaData: MySQLSchemaMetaData = schemaMetaDataBuilder getMetaData
      val generatedDbName = generator.generateDatabase(schemaMetaData, conn, databaseName)

      Database.forURL(urlRoot, user, password, prop) withSession {
        Q.queryNA[String]("use information_schema").execute
        val indexes = Q.query[(String, String), MySQLStatistic]("""
           SELECT
                TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, NON_UNIQUE, INDEX_SCHEMA, 
                INDEX_NAME, SEQ_IN_INDEX, COLUMN_NAME, COLLATION, CARDINALITY,
                SUB_PART, PACKED, NULLABLE, INDEX_TYPE, COMMENT, INDEX_COMMENT
            FROM
                statistics
            WHERE
                TABLE_SCHEMA=? AND TABLE_NAME=?
        """).list((databaseName, tableName))

        Assertions.expectResult(1)(indexes.size)
        val expect = MySQLStatistic("def", "madz_database_generator_test", "npk_incremental_table", false, "madz_database_generator_test",
          "NON_PK_INCREMENTAL", 1, "npk_incremental_COLUMN", "A", 0, 0, false, false, "BTREE", "", "")
        Assertions.expectResult(expect)(indexes(0))
        val generatedColumns = queryColumns(tableName)
        Assertions.assert("auto_increment".equalsIgnoreCase(generatedColumns(0).extra))
        Assertions.assert("PRI".equalsIgnoreCase(generatedColumns(0).columnKey))
      }

    }

//    it("should generate with single column index") {
//      pending
//    }
//
//    it("should generate with multiple columns index in a specific order") {
//      pending
//    }

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

  def makeSchema(charsetName: String, collationName: String): MySQLSchemaMetaDataBuilder = {
    val schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder = new MySQLSchemaMetaDataBuilderImpl(new DottedPath(databaseName))
    schemaMetaDataBuilder setCharSet "utf8"
    schemaMetaDataBuilder setCollation "utf8_bin"
    schemaMetaDataBuilder
  }

  def makeTable(schemaMetaDataBuilder: MySQLSchemaMetaDataBuilder, tableName: String): MySQLTableMetaDataBuilder = {
    val tableBuilder: MySQLTableMetaDataBuilder = new MySQLTableMetaDataBuilderImpl(schemaMetaDataBuilder, tableName)
    tableBuilder.setRemarks("Test Table Comments")
    tableBuilder.setType(TableType.table)
    tableBuilder.setCharacterSet("latin7")
    tableBuilder.setCollation("latin7_bin")
    tableBuilder.setEngine(MySQLEngineEnum.InnoDB)
    schemaMetaDataBuilder.appendTableMetaDataBuilder(tableBuilder)
    tableBuilder
  }

  def makeColumns(tableBuilder: MySQLTableMetaDataBuilder, columns: List[MySQLColumn]): Unit = {

    columns.foreach(rawColumn => {
      makeColumn(tableBuilder, rawColumn)
    })
  }

  def makeColumn(tableBuilder: MySQLTableMetaDataBuilder, rawColumn: MySQLColumn, increment: Boolean = false): MySQLColumnMetaDataBuilder = {
    val result = new MySQLColumnMetaDataBuilderImpl(tableBuilder, rawColumn.columnName)
    result.setCharacterMaximumLength(rawColumn.characterMaximumLengh)
    result.setCharacterSet(rawColumn.characterSetName)
    result.setCollationName(rawColumn.collationName)
    result.setDefaultValue(rawColumn.columnDefault)
    result.setColumnType(rawColumn.columnType)
    result.setNumericPrecision(rawColumn.numberPrecision.intValue)
    result.setNumericScale(rawColumn.numberScale.intValue)
    result.setAutoIncremented(increment)
    result.setNullable(rawColumn.isNullable)
    result.setOrdinalPosition(rawColumn.ordinalPosition.shortValue)
    result.setRemarks(rawColumn.columnComment)
    //result.setSize(rawColumn.)
    //result.setSqlTypeName(x$1)
    tableBuilder.appendColumnMetaDataBuilder(result)
    result
  }

  def queryColumns(tableName: String): List[MySQLColumn] = {
    Q.query[(String, String), MySQLColumn]("""
             SELECT
                 TABLE_NAME, COLUMN_NAME, ORDINAL_POSITION, COLUMN_DEFAULT, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, CHARACTER_OCTET_LENGTH, NUMERIC_PRECISION, NUMERIC_SCALE, CHARACTER_SET_NAME, COLLATION_NAME, COLUMN_TYPE, COLUMN_KEY, EXTRA, COLUMN_COMMENT
             FROM
                 COLUMNS
             WHERE
                 TABLE_SCHEMA=? AND TABLE_NAME=?
             ORDER BY
                 ORDINAL_POSITION ASC
         """).list((databaseName, tableName))
  }
}