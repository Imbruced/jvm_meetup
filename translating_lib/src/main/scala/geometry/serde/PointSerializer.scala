package geometry.serde

import geometry.primitive.Point

import java.nio.{ByteBuffer, ByteOrder}

object PointSerializer {
  def serialize(point: Point): Array[Byte] = {
    val byteBuffer = ByteBuffer.allocate(16)
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
    byteBuffer.putDouble(point.x)
    byteBuffer.putDouble(point.y)
    byteBuffer.array()
  }

  def deserialize(byteBuffer: ByteBuffer): Point = {
    val x = byteBuffer.getDouble()
    val y = byteBuffer.getDouble()

    Point(x, y)
  }
}
