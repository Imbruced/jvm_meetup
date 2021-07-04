package computation

import geometry.primitive.Point

object LengthCalculator {
  def calculateLength(points: Seq[Point]): Double = points.zip(points.tail).map {
    case (pointLeft, pointRight) => DistanceCalculator.findDistance(pointLeft, pointRight)
  }.foldLeft(0.0)(_ + _)
}
