package geometry.primitive

import computation.LengthCalculator
import geometry.serde.GeomEnum.POLYGON
import geometry.serde.PointArraySerializer

import scala.math.abs

class Polygon(points: Seq[Point]) extends Geometry {

  override def length: Double = LengthCalculator.calculateLength(points)

  override def area: Double = {
    val sumOfOfProducts = zipPoints.map {
      case (pointA, pointB) => pointA.x * pointB.y - pointA.y * pointB.x
    }.sum

    abs(sumOfOfProducts) * 0.5
  }

  private def zipPoints: Seq[(Point, Point)] = points.zip(points.tail)

  override def serialize: Array[Byte] = POLYGON.bytes ++ PointArraySerializer.serialize(this.points.toArray)

  override def wkt: String =
    s"""POLYGON(${points.map { _.coordinates }.mkString(", ")})"""

}

object Polygon {
  def apply(points: Seq[Point]): Polygon = new Polygon(points)
  def create(points: Array[Point]): Polygon = new Polygon(points)
}
