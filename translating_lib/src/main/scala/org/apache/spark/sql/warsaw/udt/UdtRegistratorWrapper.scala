package org.apache.spark.sql.warsaw.udt

import geometry.primitive.Geometry
import org.apache.spark.sql.types.UDTRegistration

object UdtRegistratorWrapper {
  def registerGeometry(): Unit = UDTRegistration.register(classOf[Geometry].getName, classOf[GeometryType].getName)
}
