package geometry.serde

import geometry.primitive.Geometry
import org.apache.spark.sql.catalyst.util.ArrayData

import java.nio.{ByteBuffer, ByteOrder}

object GeometrySerializer {
  def serialize(geom: Geometry): Array[Byte] = geom.serialize
  def deserialize(bytes: Array[Byte]): Option[Geometry] = {
    val byteBuffer = ByteBuffer.wrap(bytes)
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN)

    val geomType = byteBuffer.getInt

    GeomEnum
      .findByValue(geomType)
      .map(
        _.deserialize(byteBuffer)
      )
  }
}
