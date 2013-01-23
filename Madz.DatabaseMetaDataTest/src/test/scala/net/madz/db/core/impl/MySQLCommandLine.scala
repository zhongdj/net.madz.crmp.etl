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
  
}