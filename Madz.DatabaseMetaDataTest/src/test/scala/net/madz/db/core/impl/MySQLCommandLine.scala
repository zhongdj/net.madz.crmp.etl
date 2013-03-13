package net.madz.db.core.impl

import java.sql.Timestamp

import scala.slick.jdbc.GetResult
import scala.slick.jdbc.{ StaticQuery => Q }
import scala.slick.session.Database
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession

import net.madz.db.core.meta.immutable.mysql.MySQLTinyInt
import net.madz.db.core.meta.immutable.mysql.datatype.DataType
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLBigInt
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLBinary
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLBit
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLBlob
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLChar
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLDate
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLDateTime
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLDecimal
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLDouble
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLEnum
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLFloat
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLInt
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLInteger
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLLongBlob
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLLongText
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLMediumBlob
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLMediumInt
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLMediumText
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLSet
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLSmallInt
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLText
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLTime
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLTimeStamp
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLTinyBlob
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLTinyText
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLVarbinary
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLVarchar
import net.madz.db.core.meta.immutable.mysql.datatype.MySQLYear

trait MySQLCommandLine extends MySQLConnector {

  def exec(statement: String): Unit = {
    Database.forURL(urlRoot, user, password, prop) withSession {
      Q.queryNA[String](statement).execute
    }
  }

  def exec(statements: List[String]): Unit = {
    Database.forURL(urlRoot, user, password, prop) withSession {
      statements foreach (Q.queryNA[String](_) execute)
    }
  }

  case class MySQLSchema(val catalog: String, val schemaName: String, val defaultCharset: String, val defaultCollation: String, val sqlPath: String)
  case class MySQLStatistic(val tableCatalog: String, val tableSchema: String, val tableName: String, val nonUnique: Boolean, val indexSchema: String,
    val indexName: String, val seqInIndex: Int, val columnName: String, val collation: String, val cardinality: Long,
    val subPart: Int, val packed: Boolean, val nullable: Boolean, val indexType: String, val comment: String,
    val indexComment: String)
  case class MySQLReferentialConstraint(val constraintCatalog: String, val constraintSchema: String, val constraintName: String, val uniqueConstraintCatalog: String, val uniqueConstraintSchema: String,
    val uniqueConstraintName: String, val matchOption: String, val updateRule: String, val deleteRule: String, val tableName: String,
    val referencedTableName: String)
  case class MySQLKeyColumnUsage(val constraintCatalog: String, val constraintSchema: String, val constraintName: String, val tableCatalog: String, val tableSchema: String,
    val tableName: String, val columnName: String, val ordinalPosition: Int, val positionInUniqueConstraint: Int, val referencedTableSchema: String,
    val referencedTableName: String, val referencedColumnName: String)
  case class MySQLTable(val tableCatalog: String, val tableSchema: String, val tableName: String, val tableType: String, val engine: String,
    val version: Int, val rowFormat: String, val tableRows: Long, val avgRowLength: Long, val dataLength: Long,
    val maxDataLength: Long, val indexLength: Long, val dataFree: Long, val autoIncrement: Boolean, val createTime: Timestamp,
    val updateTime: Timestamp, val checkTime: Timestamp, val tableCollation: String, val checkSum: Long, val createOptions: String,
    val tableComment: String)
  case class MySQLColumn(val tableName: String, val columnName: String, val ordinalPosition: Int, val columnDefault: String, val isNullable: Boolean,
    val dataType: String, val characterMaximumLengh: Long, val characterOctetLength: Long, val numberPrecision: Long, val numberScale: Long,
    val characterSetName: String, val collationName: String, val columnType: String, val columnKey: String, val extra: String,
    val columnComment: String, val mysqlDataType: DataType) {

    override def equals(other: Any): Boolean = {
      if (null == other) false

      if (!other.isInstanceOf[MySQLColumn]) false
      val oth = other.asInstanceOf[MySQLColumn]

      if ((null == tableName || null == oth.tableName) && tableName != oth.tableName) false
      else if (null != tableName && !tableName.equals(oth.tableName)) false

      if ((null == columnName || null == oth.columnName) && columnName != oth.columnName) false
      else if (null != columnName && !columnName.equals(oth.columnName)) false

      if (ordinalPosition != oth.ordinalPosition) false

      if ((null == columnDefault || null == oth.columnDefault) && columnDefault != oth.columnDefault) false
      else if (null != columnDefault && !columnDefault.equals(oth.columnDefault)) false

      if (isNullable != oth.isNullable) false

      if ((null == dataType || null == oth.dataType) && dataType != oth.dataType) false
      else if (null != dataType && !dataType.equals(oth.dataType)) false

      if (characterMaximumLengh != oth.characterMaximumLengh) false
      if (characterOctetLength != oth.characterOctetLength) false
      if (numberPrecision != oth.numberPrecision) false
      if (numberScale != oth.numberScale) false

      if ((null == characterSetName || null == oth.characterSetName) && characterSetName != oth.characterSetName) false
      else if (null != characterSetName && !characterSetName.equals(oth.characterSetName)) false

      if ((null == collationName || null == oth.collationName) && collationName != oth.collationName) false
      else if (null != collationName && !collationName.equals(oth.collationName)) false

      if ((null == columnType || null == oth.columnType) && columnType != oth.columnType) false
      else if (null != columnType && !columnType.equals(oth.columnType)) false

      if ((null == columnKey || null == oth.columnKey) && columnKey != oth.columnKey) false
      else if (null != columnKey && !columnKey.equals(oth.columnKey)) false

      if ((null == extra || null == oth.extra) && extra != oth.extra) false
      else if (null != extra && !extra.equals(oth.extra)) false

      if ((null == columnComment || null == oth.columnComment) && columnComment != oth.columnComment) false
      else if (null != columnComment && !columnComment.equals(oth.columnComment)) false

      true
    }
  }

  // Result set getters
  implicit val getSchemaResult = GetResult(r => MySQLSchema(r.<<, r.<<, r.<<, r.<<, r.<<))
  implicit val getStatisticResult = GetResult(r => MySQLStatistic(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  implicit val getReferentialResult = GetResult(r => MySQLReferentialConstraint(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  implicit val getKeyColumnUsageResult = GetResult(r => MySQLKeyColumnUsage(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  implicit val getTableResult = GetResult(r => MySQLTable(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  implicit val getColumnResult = GetResult(r => MySQLColumn(r.<<, r.<<, r.<<, r.<<, r.nextString.equalsIgnoreCase("YES"), r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, null))
  //  implicit def scalaIntToJavaInt(sintValue: Int) = new java.lang.Integer(sintValue)
  implicit def scalaIntToJavaShort(sintValue: Int) = new java.lang.Short(sintValue toString)
  //  implicit def scalaShortToJavaShort(shortValue: Short) = new java.lang.Short(shortValue)

  val columns_in_table1 =
    MySQLColumn("table_with_all_data_types_p1", "BIT_COLUMN", 1, null, true, "bit", 0, 0, 1, 0, null, null, "bit(1)", "", "", "", new MySQLBit()) ::
      MySQLColumn("table_with_all_data_types_p1", "BIT_PLUS_COLUMN", 2, null, true, "bit", 0, 0, 2, 0, null, null, "bit(2)", "", "", "", new MySQLBit(2)) ::
      MySQLColumn("table_with_all_data_types_p1", "TINYINT_COLUMN", 3, null, true, "tinyint", 0, 0, 3, 0, null, null, "tinyint(1)", "", "", "", new MySQLTinyInt(1)) ::
      MySQLColumn("table_with_all_data_types_p1", "TINYINT_PLUS_COLUMN", 4, null, true, "tinyint", 0, 0, 3, 0, null, null, "tinyint(8)", "", "", "", new MySQLTinyInt(8)) ::
      MySQLColumn("table_with_all_data_types_p1", "TINYINT_UNSIGNED_COLUMN", 5, null, true, "tinyint", 0, 0, 3, 0, null, null, "tinyint(8) unsigned", "", "", "", new MySQLTinyInt(8, true)) ::
      MySQLColumn("table_with_all_data_types_p1", "BOOL_COLUMN", 6, null, true, "tinyint", 0, 0, 3, 0, null, null, "tinyint(1)", "", "", "", new MySQLTinyInt(1)) ::
      MySQLColumn("table_with_all_data_types_p1", "BOOLEAN_COLUMN", 7, null, true, "tinyint", 0, 0, 3, 0, null, null, "tinyint(1)", "", "", "", new MySQLTinyInt(1)) ::
      MySQLColumn("table_with_all_data_types_p1", "SMALLINT_COLUMN", 8, null, true, "smallint", 0, 0, 5, 0, null, null, "smallint(16)", "", "", "", new MySQLSmallInt(8)) ::
      MySQLColumn("table_with_all_data_types_p1", "SMALLINT_UNSIGNED_COLUMN", 9, null, true, "smallint", 0, 0, 5, 0, null, null, "smallint(16) unsigned", "", "", "", new MySQLSmallInt(16, true)) ::
      MySQLColumn("table_with_all_data_types_p1", "MEDIUMINT_COLUMN", 10, null, true, "mediumint", 0, 0, 7, 0, null, null, "mediumint(24)", "", "", "", new MySQLMediumInt(24)) ::
      MySQLColumn("table_with_all_data_types_p1", "MEDIUMINT_UNSIGNED_COLUMN", 11, null, true, "mediumint", 0, 0, 7, 0, null, null, "mediumint(24) unsigned", "", "", "", new MySQLMediumInt(24, true)) ::
      MySQLColumn("table_with_all_data_types_p1", "INT_COLUMN", 12, null, true, "int", 0, 0, 10, 0, null, null, "int(32)", "", "", "", new MySQLInt(32)) ::
      MySQLColumn("table_with_all_data_types_p1", "INT_UNSIGNED_COLUMN", 13, null, true, "int", 0, 0, 10, 0, null, null, "int(32) unsigned", "", "", "", new MySQLInt(32, true)) ::
      MySQLColumn("table_with_all_data_types_p1", "INTEGER_COLUMN", 14, null, true, "int", 0, 0, 10, 0, null, null, "int(32)", "", "", "", new MySQLInteger(32)) ::
      MySQLColumn("table_with_all_data_types_p1", "INTEGER_UNSIGNED_COLUMN", 15, null, true, "int", 0, 0, 10, 0, null, null, "int(32) unsigned", "", "", "", new MySQLInteger(32, true)) ::
      MySQLColumn("table_with_all_data_types_p1", "BIGINT_COLUMN", 16, null, true, "bigint", 0, 0, 19, 0, null, null, "bigint(64)", "", "", "", new MySQLBigInt(64)) ::
      MySQLColumn("table_with_all_data_types_p1", "BIGINT_UNSIGNED_COLUMN", 17, null, true, "bigint", 0, 0, 20, 0, null, null, "bigint(64) unsigned", "", "", "", new MySQLBigInt(64, true)) ::
      MySQLColumn("table_with_all_data_types_p1", "FLOAT_COLUMN", 18, null, true, "float", 0, 0, 7, 4, null, null, "float(7,4)", "", "", "", new MySQLFloat(7, 4)) ::
      MySQLColumn("table_with_all_data_types_p1", "DOUBLE_COLUMN", 19, null, true, "double", 0, 0, 64, 30, null, null, "double(64,30)", "", "", "", new MySQLDouble(64, 30)) ::
      MySQLColumn("table_with_all_data_types_p1", "DOUBLE_PLUS_COLUMN", 20, null, true, "double", 0, 0, 128, 30, null, null, "double(128,30)", "", "", "", new MySQLDouble(128, 30)) ::
      MySQLColumn("table_with_all_data_types_p1", "DECIMAL_COLUMN", 21, null, true, "decimal", 0, 0, 10, 0, null, null, "decimal(10,0)", "", "", "", new MySQLDecimal(10, 0)) ::
      MySQLColumn("table_with_all_data_types_p1", "DECIMAL_NO_SCALE_COLUMN", 22, null, true, "decimal", 0, 0, 65, 0, null, null, "decimal(65,0)", "", "", "", new MySQLDecimal(65, 0)) ::
      MySQLColumn("table_with_all_data_types_p1", "DECIMAL_SCALE_COLUMN", 23, null, true, "decimal", 0, 0, 65, 30, null, null, "decimal(65,30)", "", "", "", new MySQLDecimal(65, 30)) ::
      MySQLColumn("table_with_all_data_types_p1", "DATE_COLUMN", 24, null, true, "date", 0, 0, 0, 0, null, null, "date", "", "", "", new MySQLDate()) ::
      MySQLColumn("table_with_all_data_types_p1", "DATETIME_COLUMN", 25, null, true, "datetime", 0, 0, 0, 0, null, null, "datetime", "", "", "", new MySQLDateTime()) ::
      MySQLColumn("table_with_all_data_types_p1", "TIMESTAMP_COLUMN", 26, "2010-12-10 14:12:09", false, "timestamp", 0, 0, 0, 0, null, null, "timestamp", "", "", "", new MySQLTimeStamp) ::
      MySQLColumn("table_with_all_data_types_p1", "TIME_COLUMN", 27, null, true, "time", 0, 0, 0, 0, null, null, "time", "", "", "", new MySQLTime) ::
      MySQLColumn("table_with_all_data_types_p1", "YEAR_COLUMN", 28, null, true, "year", 0, 0, 0, 0, null, null, "year(4)", "", "", "", new MySQLYear) ::
      MySQLColumn("table_with_all_data_types_p1", "YEAR_PLUS_COLUMN", 29, null, true, "year", 0, 0, 0, 0, null, null, "year(4)", "", "", "", new MySQLYear) ::
      MySQLColumn("table_with_all_data_types_p1", "CHAR_COLUMN", 30, null, true, "char", 255, 255, 0, 0, "latin7", "latin7_general_ci", "char(255)", "", "", "", new MySQLChar(255)) ::
      MySQLColumn("table_with_all_data_types_p1", "BINARY_COLUMN", 31, null, true, "binary", 255, 255, 0, 0, null, null, "binary(255)", "", "", "", new MySQLBinary(255)) :: Nil

  val columns_in_table2 =
    MySQLColumn("table_with_all_data_types_p2", "VARBINARY_COLUMN", 1, null, true, "varbinary", 65532, 65532, 0, 0, null, null, "varbinary(65532)", "", "", "", new MySQLVarbinary(65532)) :: Nil

  val columns_in_table3 =
    MySQLColumn("table_with_all_data_types_p3", "TINYBLOB_COLUMN", 1, null, true, "tinyblob", 255, 255, 0, 0, null, null, "tinyblob", "", "", "", new MySQLTinyBlob) ::
      MySQLColumn("table_with_all_data_types_p3", "TINYTEXT_COLUMN", 2, null, true, "tinytext", 255, 255, 0, 0, "latin7", "latin7_general_ci", "tinytext", "", "", "", new MySQLTinyText) ::
      MySQLColumn("table_with_all_data_types_p3", "BLOB_COLUMN", 3, null, true, "blob", 65535, 65535, 0, 0, null, null, "blob", "", "", "", new MySQLBlob) ::
      MySQLColumn("table_with_all_data_types_p3", "TEXT_COLUMN", 4, null, true, "text", 65535, 65535, 0, 0, "latin7", "latin7_general_ci", "text", "", "", "", new MySQLText) ::
      MySQLColumn("table_with_all_data_types_p3", "MEDIUMBLOB_COLUMN", 5, null, true, "mediumblob", 16777215L, 16777215L, 0, 0, null, null, "mediumblob", "", "", "", new MySQLMediumBlob) ::
      MySQLColumn("table_with_all_data_types_p3", "MEDIUMTEXT_COLUMN", 6, null, true, "mediumtext", 16777215L, 16777215L, 0, 0, "latin7", "latin7_general_ci", "mediumtext", "", "", "", new MySQLMediumText) ::
      MySQLColumn("table_with_all_data_types_p3", "LONGBLOB_COLUMN", 7, null, true, "longblob", 4294967295L, 4294967295L, 0, 0, null, null, "longblob", "", "", "", new MySQLLongBlob) ::
      MySQLColumn("table_with_all_data_types_p3", "LONGTEXT_COLUMN", 8, null, true, "longtext", 4294967295L, 4294967295L, 0, 0, "latin7", "latin7_general_ci", "longtext", "", "", "", new MySQLLongText) ::
      MySQLColumn("table_with_all_data_types_p3", "ENUM_COLUMN", 9, null, true, "enum", 1, 1, 0, 0, "latin7", "latin7_general_ci", "enum('A','B','C')", "", "", "", new MySQLEnum().addValue("A").addValue("B").addValue("C")) ::
      MySQLColumn("table_with_all_data_types_p3", "SET_COLUMN", 10, null, true, "set", 9, 9, 0, 0, "latin7", "latin7_general_ci", "set('HLJ','JX','BJ')", "", "", "", new MySQLSet().addValue("HLJ").addValue("JX").addValue("BJ")) :: Nil
  val columns_in_table4 =
    MySQLColumn("table_with_all_data_types_p4", "VARCHAR_COLUMN", 1, null, true, "varchar", 65532, 65532, 0, 0, "latin7", "latin7_general_ci", "varchar(65532)", "", "", "", new MySQLVarchar(65532, "latin7", "latin7_general_ci")) :: Nil
  val columns_in_table5 =
    MySQLColumn("table_with_all_data_types_p5", "VARCHAR_BINARY_COLUMN", 1, null, true, "varchar", 65532, 65532, 0, 0, "latin7", "latin7_bin", "varchar(65532)", "", "", "", new MySQLVarchar(65532, "latin7", "latin7_bin")) :: Nil

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
      `YEAR_COLUMN`                     YEAR(4)                         DEFAULT null,
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

}