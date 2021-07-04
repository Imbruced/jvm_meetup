package geometry.serde

import geometry.primitive.Point

import java.nio.{ByteBuffer, ByteOrder}

object PointArraySerializer {
  def serialize(points: Array[Point]): Array[Byte] = {
    val byteBuffer = ByteBuffer.allocate(4)
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
    byteBuffer.putInt(points.length)

    byteBuffer.array() ++ points.flatMap(PointSerializer.serialize)
  }

  def deserialize(byteBuffer: ByteBuffer): Array[Point] = {
    val arrayLength = byteBuffer.getInt()
    (0 until arrayLength).map(_ => PointSerializer.deserialize(byteBuffer)).toArray
  }
}
