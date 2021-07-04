package org.apache.spark.sql.warsaw.functions

import org.apache.spark.sql.{SparkSession}

object UdfRegistrator {

  def registerAll(sparkSession: SparkSession): Unit = {
    Catalog.expressions.foreach(f =>
      sparkSession.sessionState.functionRegistry.createOrReplaceTempFunction(f.getClass.getSimpleName.dropRight(1), f)
    )
  }
}
