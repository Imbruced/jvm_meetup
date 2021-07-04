package org.apache.spark.sql.warsaw.udt

import geometry.primitive.Geometry
import geometry.serde.GeometrySerializer
import org.apache.spark.sql.catalyst.util.{ArrayData, GenericArrayData}
import org.apache.spark.sql.types.{ArrayType, ByteType, DataType, UserDefinedType}

class GeometryType extends UserDefinedType[Geometry] {
  override def sqlType: DataType = ArrayType(ByteType, containsNull = false)
  override def pyUDT: String = "wde.sql.types.GeometryType"
  override def userClass: Class[Geometry] = classOf[Geometry]
  override def serialize(obj: Geometry): GenericArrayData =
    new GenericArrayData(GeometrySerializer.serialize(obj))

  override def deserialize(datum: Any): Geometry = {
    datum match {
      case values: ArrayData =>
        GeometrySerializer.deserialize(values.toByteArray()).orNull
    }
  }

}

case object GeometryType extends GeometryType with scala.Serializable
