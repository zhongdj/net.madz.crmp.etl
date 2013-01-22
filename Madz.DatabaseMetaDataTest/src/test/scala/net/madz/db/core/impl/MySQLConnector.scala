package net.madz.db.core.impl

import java.util.Properties

trait MySQLConnector {

  def urlRoot = { "jdbc:mysql://localhost:3306/" }
  def user = { "root" }
  def password = { "1q2w3e4r5t" }
  def prop: Properties = {
    val p = new Properties()
    p.put("driver", "com.mysql.jdbc.Driver")
    p
  }

}