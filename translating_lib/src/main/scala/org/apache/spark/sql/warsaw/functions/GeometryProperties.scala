package org.apache.spark.sql.warsaw.functions

import geometry.parser.WktParser
import geometry.primitive.Geometry
import geometry.serde.GeometrySerializer
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.expressions.codegen.CodegenFallback
import org.apache.spark.sql.catalyst.util.GenericArrayData
import org.apache.spark.sql.types.{DataType, DoubleType}
import org.apache.spark.sql.warsaw.udt.GeometryType
import org.apache.spark.unsafe.types.UTF8String

object GeometryProperties {

  case class ST_GeomFromString(inputExpressions: Seq[Expression]) extends Expression with CodegenFallback {
    override def nullable: Boolean = false

    override def eval(inputRow: InternalRow): Any = {
      assert(inputExpressions.length == 1)
      val wkt = inputExpressions(0).eval(inputRow).asInstanceOf[UTF8String]
      new GenericArrayData(GeometrySerializer.serialize(WktParser.parse(wkt).orNull))
    }

    override def dataType: DataType = GeometryType

    override def children: Seq[Expression] = inputExpressions
  }

  case class ST_Length(inputExpressions: Seq[Expression]) extends Expression with CodegenFallback {
    override def nullable: Boolean = true

    override def eval(inputRow: InternalRow): Any = {
      assert(inputExpressions.length == 1)
      val byteData = inputExpressions(0).eval(inputRow).asInstanceOf[GenericArrayData]
      val geometryData = GeometrySerializer.deserialize(byteData.toByteArray())
      geometryData.map(_.length).orNull
    }

    override def dataType: DataType = DoubleType

    override def children: Seq[Expression] = inputExpressions
  }

  case class ST_Area(inputExpressions: Seq[Expression]) extends Expression with CodegenFallback {
    override def nullable: Boolean = true

    override def eval(inputRow: InternalRow): Any = {
      assert(inputExpressions.length == 1)
      val byteData = inputExpressions(0).eval(inputRow).asInstanceOf[GenericArrayData]
      val geometryData = GeometrySerializer.deserialize(byteData.toByteArray())
      geometryData.map(_.area).orNull
    }

    override def dataType: DataType = DoubleType

    override def children: Seq[Expression] = inputExpressions
  }

}
