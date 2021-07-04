package geometry.primitive

import computation.DistanceCalculator
import geometry.serde.GeomEnum.POINT
import geometry.serde.PointSerializer

case class Point(x: Double, y: Double) extends Geometry {

  def distance(point: Point): Double = DistanceCalculator.findDistance(this, point)

  override def length: Double = 0.0

  override def area: Double = 0.0

  override def serialize: Array[Byte] =
    POINT.bytes ++ PointSerializer.serialize(this)

  override def wkt: String = s"POINT($coordinates)"

  def coordinates: String = s"$x $y"
}

object Point {
  def apply(x: Double, y: Double): Point = new Point(x, y)
}
