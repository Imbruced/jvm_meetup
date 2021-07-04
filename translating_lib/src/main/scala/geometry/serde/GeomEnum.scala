package geometry.serde

import enumeratum.{Enum, EnumEntry}
import enumeratum.EnumEntry.{Lowercase, Snakecase}
import geometry.primitive.{Geometry, Linestring, Polygon}

import java.nio.{ByteBuffer, ByteOrder}
import scala.collection.immutable

sealed trait GeomEnum extends EnumEntry with Snakecase with Lowercase with Serializable {
  def number: Int = ???
  def bytes: Array[Byte] = {
    val byteBuffer = ByteBuffer.allocate(4)
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
    byteBuffer.putInt(number).array()
  }

  def deserialize(array: ByteBuffer): Geometry
}

object GeomEnum extends Enum[GeomEnum] {
  case object POINT extends GeomEnum {
    override def number: Int = 1

    override def deserialize(array: ByteBuffer): Geometry = PointSerializer.deserialize(array)

  }

  case object POLYGON extends GeomEnum {
    override def number: Int = 2
    override def deserialize(byteBuffer: ByteBuffer): Geometry = Polygon(
      PointArraySerializer.deserialize(byteBuffer).toSeq
    )
  }

  case object LINESTRING extends GeomEnum {
    override def number: Int = 3
    override def deserialize(byteBuffer: ByteBuffer): Geometry =
      Linestring(PointArraySerializer.deserialize(byteBuffer).toSeq)
  }

  override def values: immutable.IndexedSeq[GeomEnum] = findValues
  def findByValue(value: Int): Option[GeomEnum] = values.find(_.number == value)
}
