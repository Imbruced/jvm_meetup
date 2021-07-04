package org.apache.spark.sql.warsaw.functions

import org.apache.spark.sql.catalyst.analysis.FunctionRegistry.FunctionBuilder
import org.apache.spark.sql.warsaw.functions.GeometryProperties.{ST_Area, ST_GeomFromString, ST_Length}

object Catalog {
  val expressions: Seq[FunctionBuilder] = Seq(
    ST_GeomFromString,
    ST_Length,
    ST_Area
  )
}
