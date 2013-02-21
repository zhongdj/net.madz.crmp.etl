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

}