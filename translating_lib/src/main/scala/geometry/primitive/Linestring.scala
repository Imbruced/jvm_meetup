package geometry.primitive

import computation.LengthCalculator
import geometry.serde.GeomEnum.{LINESTRING, POINT}
import geometry.serde.{PointArraySerializer, PointSerializer}

case class Linestring(points: Seq[Point]) extends Geometry {

  override def length: Double = LengthCalculator.calculateLength(points)

  override def area: Double = 0.0

  override def serialize: Array[Byte] = LINESTRING.bytes ++ PointArraySerializer.serialize(this.points.toArray)

  override def wkt: String =
    s"""LINESTRING(${points.map { _.coordinates }.mkString(", ")})"""
}

object Linestring {
  def create(points: Array[Point]): Linestring = new Linestring(points)
}
