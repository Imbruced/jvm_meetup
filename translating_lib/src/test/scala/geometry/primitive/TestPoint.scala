package geometry.primitive

import com.softwaremill.diffx.scalatest.DiffMatcher
import geometry.serde.GeometrySerializer
import org.scalatest.GivenWhenThen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor3}

class TestPoint extends AnyFunSuite with GivenWhenThen with TableDrivenPropertyChecks with DiffMatcher {

  for ((statement: String, point: Point, expectedLength: Double) <- Fixtures.lengthAreaPointSamples) {
    test("should correctly calculate area and length of point " + statement) {
      Then("point area should be 0.0")

      point.length shouldBe expectedLength
      point.area shouldBe expectedLength
    }
  }

  test("should serialize and deserialize Point") {
    Given("point with non empty coordinates")
    val point = Point(21.00, 52.00)

    When("serializing point object")
    val serialized = GeometrySerializer.serialize(point)

    Then("serialized object should match to expected byte array")
    serialized shouldBe Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 53, 64, 0, 0, 0, 0, 0, 0, 74, 64)

    When("deserializing already serialized point")
    val pointDeserialized = GeometrySerializer.deserialize(serialized)

    Then("deserialized point should be the same as on start")
    pointDeserialized.get.wkt should matchTo(point.wkt)

  }

  object Fixtures {
    val lengthAreaPointSamples: TableFor3[String, Point, Double] = Table(
      ("statement", "inputPoints", "expectedArea"),
      ("point with integer coordinates", Point(21, 52), 0.0),
      ("point with double coordinates", Point(21.04, 52.68), 0.0),
      ("point with negative coordinates", Point(-21.04, -52.68), 0.0),
      ("point with zero coordinates", Point(0, 0), 0.0)
    )
  }
}
