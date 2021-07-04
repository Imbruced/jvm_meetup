package org.apache.warsaw.sql

import org.apache.spark.sql.warsaw.udt.UdtRegistratorWrapper

object UdtRegistrator {
  def registerGeometry(): Unit = UdtRegistratorWrapper.registerGeometry()
}
