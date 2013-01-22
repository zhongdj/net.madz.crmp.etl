package net.madz.db.core.impl

import scala.slick.session.Database
import scala.collection.mutable.ListBuffer
import scala.slick.jdbc.{ StaticQuery => Q }
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession

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