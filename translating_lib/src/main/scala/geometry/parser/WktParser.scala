package geometry.parser

import geometry.primitive.{Geometry, Linestring, Point, Polygon}
import org.apache.spark.unsafe.types.UTF8String

object WktParser {

  private val pointRegex = raw"(\d+\.*\d*) (\d+\.*\d*)".r

  def parse(wkt: UTF8String): Option[Geometry] = {
    val wktString = wkt.toString
    val geometryType = wktString.split("\\(").headOption.map(_.toLowerCase())
    geometryType.map {
      case "polygon"    => parsePolygon(wktString)
      case "linestring" => parseLinestring(wktString)
      case "point"      => parsePoint(wktString)
    }
  }

  def parsePolygon(wkt: String): Polygon = {
    val points = pointRegex.findAllIn(wkt).toList
    Polygon(points.map {
      case pointRegex(x, y) => Point(x.toDouble, y.toDouble)
    })
  }
  def parseLinestring(wkt: String): Linestring = {
    val points = pointRegex.findAllIn(wkt).toList
    Linestring(points.map {
      case pointRegex(x, y) => Point(x.toDouble, y.toDouble)
    })
  }
  def parsePoint(wkt: String): Point = {
    val points = pointRegex.findAllIn(wkt).toList
    points.map {
      case pointRegex(x, y) => Point(x.toDouble, y.toDouble)
    }.head
  }
}
