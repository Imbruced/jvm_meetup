package geometry.primitive

import com.softwaremill.diffx.scalatest.DiffMatcher
import geometry.serde.GeometrySerializer
import org.scalatest.GivenWhenThen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor3}

class TestLinestring
    extends AnyFunSuite
    with GivenWhenThen
    with TableDrivenPropertyChecks
    with Matchers
    with DiffMatcher {

  for ((statement: String, inputPoints: Seq[Point], expectedArea: Double) <- Fixtures.areaLinestringSamples) {
    test("should correctly calculate area of polygon " + statement) {
      val polygon = Linestring(inputPoints)

      polygon.area shouldBe expectedArea
    }
  }

  for ((statement: String, inputPoints: Seq[Point], expectedLength: Double) <- Fixtures.lengthLinestringSamples) {
    test("should correctly calculate length of polygon " + statement) {
      val polygon = Linestring(inputPoints)

      polygon.length shouldBe expectedLength
    }
  }

  test("should correctly serialize and deserialize linestring") {
    Given("linestring with 5 points")
    val linestring = Linestring(
      points = Seq(Point(0.0, 0.0), Point(1.0, 1.0), Point(2.0, 3.0), Point(5.0, 6.0), Point(7.0, 8.0))
    )

    When("serializing geometry")
    val serializedGeometry = GeometrySerializer.serialize(linestring)

    Then("linestring should be correctly serialized")
    serializedGeometry shouldBe Array(3, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, -16, 63, 0, 0, 0, 0, 0, 0, -16, 63, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, 0, 0, 0, 0, 8, 64, 0, 0, 0, 0, 0, 0,
      20, 64, 0, 0, 0, 0, 0, 0, 24, 64, 0, 0, 0, 0, 0, 0, 28, 64, 0, 0, 0, 0, 0, 0, 32, 64)

    When("deserializing already serialized linestring")
    val deserializedGeom = GeometrySerializer.deserialize(serializedGeometry)

    Then("linestring should be the same as on the beginning")
    deserializedGeom.get.wkt should matchTo(linestring.wkt)
  }

  object Fixtures {
    val areaLinestringSamples: TableFor3[String, Seq[Point], Double] = Table(
      ("statement", "inputPoints", "expectedArea"),
      ("line", Seq(Point(0, 0), Point(0, 1), Point(1, 1), Point(1, 0)), 0)
    )

    val lengthLinestringSamples: TableFor3[String, Seq[Point], Double] = Table(
      ("statement", "inputPoints", "expectedLength"),
      ("simple line", Seq(Point(0, 0), Point(0, 1), Point(1, 1), Point(1, 0)), 3.0)
    )
  }
}
