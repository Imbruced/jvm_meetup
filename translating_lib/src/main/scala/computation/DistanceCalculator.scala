package computation

import geometry.primitive.Point

object DistanceCalculator {
  def findDistance(pointA: Point, pointB: Point): Double = {
    math.pow(pointA.x - pointB.x, 2) + math.pow(pointA.y - pointB.y, 2)
  }
}
