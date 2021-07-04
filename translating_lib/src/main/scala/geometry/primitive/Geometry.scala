package geometry.primitive

abstract class Geometry {
  def length: Double
  def area: Double
  def serialize: Array[Byte]
  def wkt: String
}
