package geometry.primitive

import geometry.serde.GeometrySerializer
import org.scalatest.GivenWhenThen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor3}
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class TestPolygon extends AnyFunSuite with GivenWhenThen with TableDrivenPropertyChecks {

  for ((statement: String, inputPoints: Seq[Point], expectedArea: Double) <- Fixtures.areaPolygonSamples) {
    test("should correctly calculate area of polygon " + statement) {
      val polygon = Polygon(inputPoints)

      polygon.area shouldBe expectedArea
    }
  }

  for ((statement: String, inputPoints: Seq[Point], expectedLength: Double) <- Fixtures.lengthPolygonSamples) {
    test("should correctly calculate length of polygon " + statement) {
      val polygon = Polygon(inputPoints)

      polygon.length shouldBe expectedLength
    }
  }

  test("should correctly serialize and deserialize polygon") {
    Given("polygon with non empty coordinates")
    val polygon = Polygon(
      Seq(
        Point(0.0, 0.0),
        Point(1.0, 0.0),
        Point(1.0, 1.0),
        Point(0.0, 1.0),
        Point(0.0, 0.0)
      )
    )

    When("serializing given polygon")
    val serializedPolygon = GeometrySerializer.serialize(polygon)

    Then("serialized polygon should match with byte array")
    serializedPolygon shouldBe Array(2, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, -16, 63, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -16, 63, 0, 0, 0, 0, 0, 0, -16, 63, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, -16, 63, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    When("deserializing already serialized polygon")
    val deserializedPolygon = GeometrySerializer.deserialize(serializedPolygon)

    Then("deserialized polygon should ne the same as staring one")
    deserializedPolygon.get.wkt shouldBe polygon.wkt
  }

  object Fixtures {
    val areaPolygonSamples: TableFor3[String, Seq[Point], Double] = Table(
      ("statement", "inputPoints", "expectedArea"),
      ("rectangle", Seq(Point(0, 0), Point(0, 1), Point(1, 1), Point(1, 0), Point(0, 0)), 1),
      ("perpendicular triangle", Seq(Point(0, 0), Point(0, 2), Point(2, 0), Point(0, 0)), 2),
      ("isosceles triangle", Seq(Point(0, 0), Point(0.5, 5), Point(1, 0), Point(0, 0)), 2.5),
      (
        "polygon with 5 vertices",
        Seq(
          Point(20, 35),
          Point(10, 30),
          Point(10, 10),
          Point(30, 5),
          Point(45, 20),
          Point(20, 35)
        ),
        675
      ),
      (
        "polygon with 5 vertices and not integer coordinates",
        Seq(
          Point(45, 45),
          Point(37.857142857142854, 20),
          Point(30, 20),
          Point(10, 20),
          Point(15, 40),
          Point(45, 45)
        ),
        635.7142857142857
      )
    )

    val lengthPolygonSamples: TableFor3[String, Seq[Point], Double] = Table(
      ("statement", "inputPoints", "expectedLength"),
      ("rectangle", Seq(Point(0, 0), Point(0, 1), Point(1, 1), Point(1, 0), Point(0, 0)), 4.0)
    )
  }

}
