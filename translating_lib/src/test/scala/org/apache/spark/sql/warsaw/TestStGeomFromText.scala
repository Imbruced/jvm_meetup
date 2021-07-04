package org.apache.spark.sql.warsaw

import com.softwaremill.diffx.scalatest.DiffMatcher
import org.apache.spark.sql.functions.expr
import org.scalatest.GivenWhenThen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TestStGeomFromText extends AnyFunSuite with GivenWhenThen with DiffMatcher with Matchers with SparkTestBase {

  import spark.implicits._

  test("should correctly create data frame from wkt geometries") {
    Given("Geometries with different data types")
    val geometries = Seq(
      (1, "POINT(21 52)"),
      (2, "POLYGON((0.0 0.0, 1.0 0.0, 1.0 1.0, 0.0 1.0, 0.0 0.0))"),
      (3, "LINESTRING(0.0 0.0, 1.0 0.0, 1.0 1.0, 0.0 1.0)")
    )
    val wktDf = geometries.toDF("id", "geom")

    When("running st geom from text")
    val geometryDf = wktDf.withColumn("geometry", expr("ST_GeomFromString(geom)"))

    Then("other geometry functions should work on geometry created column")
    geometryDf
      .withColumn("length", expr("ST_Length(geometry)").alias("length"))
      .select("length")
      .as[Double]
      .collect()
      .toList should matchTo(List(0.0, 4.0, 3.0))

  }
}
