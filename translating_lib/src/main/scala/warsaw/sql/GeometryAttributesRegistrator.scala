package warsaw.sql

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.warsaw.functions.UdfRegistrator
import org.apache.spark.sql.warsaw.udt.UdtRegistratorWrapper

object GeometryAttributesRegistrator {
  def registerAll(spark: SparkSession): Unit = {
    UdtRegistratorWrapper.registerGeometry()
    UdfRegistrator.registerAll(spark)
  }
}
