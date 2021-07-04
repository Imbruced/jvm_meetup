package org.apache.spark.sql.warsaw

import org.apache.spark.sql.SparkSession
import warsaw.sql.GeometryAttributesRegistrator

trait SparkTestBase {

  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("warsaw-data-engineering")
    .getOrCreate()

  GeometryAttributesRegistrator.registerAll(spark)

}
