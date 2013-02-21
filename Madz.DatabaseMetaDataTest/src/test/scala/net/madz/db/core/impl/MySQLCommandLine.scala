package net.madz.db.core.impl

import scala.slick.session.Database
import scala.collection.mutable.ListBuffer
import scala.slick.jdbc.{ StaticQuery => Q }
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession
import java.sql.Timestamp
import scala.slick.jdbc.GetResult

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
    val indexName: String, val seqInIndex: Int, val columnName: String, val collation: String, val cardinality: Long, val subPart: Int, val packed: Boolean,
    val nullable: Boolean, val indexType: String, val comment: String, val indexComment: String)
  case class MySQLReferentialConstraint(val constraintCatalog: String, val constraintSchema: String, val constraintName: String, val uniqueConstraintCatalog: String, val uniqueConstraintSchema: String, val uniqueConstraintName: String, val matchOption: String, val updateRule: String, val deleteRule: String, val tableName: String,
    val referencedTableName: String)
  case class MySQLKeyColumnUsage(val constraintCatalog: String, val constraintSchema: String, val constraintName: String, val tableCatalog: String, val tableSchema: String, val tableName: String, val columnName: String, val ordinalPosition: Int, val positionInUniqueConstraint: Int, val referencedTableSchema: String, val referencedTableName: String, val referencedColumnName: String)
  case class MySQLTable(val tableCatalog: String, val tableSchema: String, val tableName: String, val tableType: String, val engine: String, val version: Int, val rowFormat: String, val tableRows: Long, val avgRowLength: Long, val dataLength: Long, val maxDataLength: Long, val indexLength: Long, val dataFree: Long, val autoIncrement: Boolean, val createTime: Timestamp, val updateTime: Timestamp, val checkTime: Timestamp,
    val tableCollation: String, val checkSum: Long, val createOptions: String, val tableComment: String)
  case class MySQLColumn(val tableCatalog: String, val tableSchema: String, val tableName: String, val columnName: String, val ordinalPosition: Int, val columnDefault: String, val isNullable: String,
    val dataType: String, val characterMaximumLengh: Long, val chararcterOctetLength: Long, val numberPrecision: Long, val numberScale: Long, val characterSetName: String, val collationName: String, val columnType: String, val columnKey: String, val extra: String, val privileges: String, val columnComment: String)

  // Result set getters
  implicit val getSchemaResult = GetResult(r => MySQLSchema(r.<<, r.<<, r.<<, r.<<, r.<<))
  implicit val getStatisticResult = GetResult(r => MySQLStatistic(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  implicit val getReferentialResult = GetResult(r => MySQLReferentialConstraint(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  implicit val getKeyColumnUsageResult = GetResult(r => MySQLKeyColumnUsage(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  implicit val getTableResult = GetResult(r => MySQLTable(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  implicit val getColumnResult = GetResult(r => MySQLColumn(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))

  case class ColumnMetaData(
    val TABLE_NAME: String, val COLUMN_NAME: String, val ORDINAL_POSITION: Integer,
    val COLUMN_DEFAULT: String, val IS_NULLABLE: Boolean, val DATA_TYPE: String,
    val CHARACTER_MAXIMUM_LENGTH: java.lang.Long, val NUMERIC_PRECISION: Integer,
    val NUMERIC_SCALE: Integer, val CHARACTER_SET_NAME: String, val COLLATION_NAME: String,
    val COLUMN_TYPE: String)

  val columns_in_table1 =
    ColumnMetaData("table_with_all_data_types_p1", "BIT_COLUMN", 1, null, true, "bit", 0, 1, 0, null, null, "bit(1)") ::
      ColumnMetaData("table_with_all_data_types_p1", "BIT_PLUS_COLUMN", 2, null, true, "bit", 0, 2, 0, null, null, "bit(2)") ::
      ColumnMetaData("table_with_all_data_types_p1", "TINYINT_COLUMN", 3, null, true, "tinyint", 0, 3, 0, null, null, "tinyint(1)") ::
      ColumnMetaData("table_with_all_data_types_p1", "TINYINT_PLUS_COLUMN", 4, null, true, "tinyint", 0, 3, 0, null, null, "tinyint(8)") ::
      ColumnMetaData("table_with_all_data_types_p1", "TINYINT_UNSIGNED_COLUMN", 5, null, true, "tinyint", 0, 3, 0, null, null, "tinyint(8) unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "BOOL_COLUMN", 6, null, true, "tinyint", 0, 3, 0, null, null, "tinyint(1)") ::
      ColumnMetaData("table_with_all_data_types_p1", "BOOLEAN_COLUMN", 7, null, true, "tinyint", 0, 3, 0, null, null, "tinyint(1)") ::
      ColumnMetaData("table_with_all_data_types_p1", "SMALLINT_COLUMN", 8, null, true, "smallint", 0, 5, 0, null, null, "smallint(16)") ::
      ColumnMetaData("table_with_all_data_types_p1", "SMALLINT_UNSIGNED_COLUMN", 9, null, true, "smallint", 0, 5, 0, null, null, "smallint(16) unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "MEDIUMINT_COLUMN", 10, null, true, "mediumint", 0, 7, 0, null, null, "mediumint(24)") ::
      ColumnMetaData("table_with_all_data_types_p1", "MEDIUMINT_UNSIGNED_COLUMN", 11, null, true, "mediumint", 0, 7, 0, null, null, "mediumint(24) unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "INT_COLUMN", 12, null, true, "int", 0, 10, 0, null, null, "int(32)") ::
      ColumnMetaData("table_with_all_data_types_p1", "INT_UNSIGNED_COLUMN", 13, null, true, "int", 0, 10, 0, null, null, "int(32) unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "INTEGER_COLUMN", 14, null, true, "int", 0, 10, 0, null, null, "int(32)") ::
      ColumnMetaData("table_with_all_data_types_p1", "INTEGER_UNSIGNED_COLUMN", 15, null, true, "int", 0, 10, 0, null, null, "int(32) unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "BIGINT_COLUMN", 16, null, true, "bigint", 0, 19, 0, null, null, "bigint(64)") ::
      ColumnMetaData("table_with_all_data_types_p1", "BIGINT_UNSIGNED_COLUMN", 17, null, true, "bigint", 0, 20, 0, null, null, "bigint(64) unsigned") ::
      ColumnMetaData("table_with_all_data_types_p1", "FLOAT_COLUMN", 18, null, true, "float", 0, 7, 4, null, null, "float(7,4)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DOUBLE_COLUMN", 19, null, true, "double", 0, 64, 30, null, null, "double(64,30)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DOUBLE_PLUS_COLUMN", 20, null, true, "double", 0, 128, 30, null, null, "double(128,30)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DECIMAL_COLUMN", 21, null, true, "decimal", 0, 10, 0, null, null, "decimal(10,0)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DECIMAL_NO_SCALE_COLUMN", 22, null, true, "decimal", 0, 65, 0, null, null, "decimal(65,0)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DECIMAL_SCALE_COLUMN", 23, null, true, "decimal", 0, 65, 30, null, null, "decimal(65,30)") ::
      ColumnMetaData("table_with_all_data_types_p1", "DATE_COLUMN", 24, null, true, "date", 0, 0, 0, null, null, "date") ::
      ColumnMetaData("table_with_all_data_types_p1", "DATETIME_COLUMN", 25, null, true, "datetime", 0, 0, 0, null, null, "datetime") ::
      ColumnMetaData("table_with_all_data_types_p1", "TIMESTAMP_COLUMN", 26, "2010-12-10 14:12:09", false, "timestamp", 0, 0, 0, null, null, "timestamp") ::
      ColumnMetaData("table_with_all_data_types_p1", "TIME_COLUMN", 27, null, true, "time", 0, 0, 0, null, null, "time") ::
      ColumnMetaData("table_with_all_data_types_p1", "YEAR_COLUMN", 28, null, true, "year", 0, 0, 0, null, null, "year(2)") ::
      ColumnMetaData("table_with_all_data_types_p1", "YEAR_PLUS_COLUMN", 29, null, true, "year", 0, 0, 0, null, null, "year(4)") ::
      ColumnMetaData("table_with_all_data_types_p1", "CHAR_COLUMN", 30, null, true, "char", 255, 0, 0, "latin7", "latin7_general_ci", "char(255)") ::
      ColumnMetaData("table_with_all_data_types_p1", "BINARY_COLUMN", 31, null, true, "binary", 255, 0, 0, null, null, "binary(255)") :: Nil

  val columns_in_table2 =
    ColumnMetaData("table_with_all_data_types_p2", "VARBINARY_COLUMN", 1, null, true, "varbinary", 65532, 0, 0, null, null, "varbinary(65532)") :: Nil

  val columns_in_table3 =
    ColumnMetaData("table_with_all_data_types_p3", "TINYBLOB_COLUMN", 1, null, true, "tinyblob", 255, 0, 0, null, null, "tinyblob") ::
      ColumnMetaData("table_with_all_data_types_p3", "TINYTEXT_COLUMN", 2, null, true, "tinytext", 255, 0, 0, "latin7", "latin7_general_ci", "tinytext") ::
      ColumnMetaData("table_with_all_data_types_p3", "BLOB_COLUMN", 3, null, true, "blob", 65535, 0, 0, null, null, "blob") ::
      ColumnMetaData("table_with_all_data_types_p3", "TEXT_COLUMN", 4, null, true, "text", 65535, 0, 0, "latin7", "latin7_general_ci", "text") ::
      ColumnMetaData("table_with_all_data_types_p3", "MEDIUMBLOB_COLUMN", 5, null, true, "mediumblob", 16777215L, 0, 0, null, null, "mediumblob") ::
      ColumnMetaData("table_with_all_data_types_p3", "MEDIUMTEXT_COLUMN", 6, null, true, "mediumtext", 16777215L, 0, 0, "latin7", "latin7_general_ci", "mediumtext") ::
      ColumnMetaData("table_with_all_data_types_p3", "LONGBLOB_COLUMN", 7, null, true, "longblob", 4294967295L, 0, 0, null, null, "longblob") ::
      ColumnMetaData("table_with_all_data_types_p3", "LONGTEXT_COLUMN", 8, null, true, "longtext", 4294967295L, 0, 0, "latin7", "latin7_general_ci", "longtext") ::
      ColumnMetaData("table_with_all_data_types_p3", "ENUM_COLUMN", 9, null, true, "enum", 1, 0, 0, "latin7", "latin7_general_ci", "enum('A','B','C')") ::
      ColumnMetaData("table_with_all_data_types_p3", "SET_COLUMN", 10, null, true, "set", 9, 0, 0, "latin7", "latin7_general_ci", "set('HLJ','JX','BJ')") :: Nil
  val columns_in_table4 =
    ColumnMetaData("table_with_all_data_types_p4", "VARCHAR_COLUMN", 1, null, true, "varchar", 65532, 0, 0, "latin7", "latin7_general_ci", "varchar(65532)") :: Nil
  val columns_in_table5 =
    ColumnMetaData("table_with_all_data_types_p5", "VARCHAR_BINARY_COLUMN", 1, null, true, "varchar", 65532, 0, 0, "latin7", "latin7_bin", "varchar(65532)") :: Nil

}