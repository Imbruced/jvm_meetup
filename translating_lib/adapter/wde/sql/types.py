from typing import List

from pyspark.sql.types import UserDefinedType, ArrayType, ByteType

from wde.sql.binary import BinaryParser


class Geometry:

    def wkt(self) -> str:
        return NotImplemented("geometry should implement wkt method")


class Point(Geometry):
    def __init__(self, x: float, y: float):
        self.x = x
        self.y = y

    def format_coordinates(self) -> str:
        return f"{self.x} {self.y}"

    def wkt(self):
        return f"POINT({self.format_coordinates()})"


class Polygon(Geometry):

    def __init__(self, points: List[Point]):
        self.points = points

    def wkt(self):
        coordinates = [point.format_coordinates() for point in self.points]
        coordinates_string = ", ".join(coordinates)
        return f"POLYGON(({coordinates_string}))"


class Linestring(Geometry):

    def __init__(self, points: List[Point]):
        self.points = points

    def wkt(self):
        coordinates = [point.format_coordinates() for point in self.points]
        coordinates_string = ", ".join(coordinates)
        return f"LINESTRING({coordinates_string})"


class Parser:
    def parse(self, binary_parser: BinaryParser):
        raise NotImplemented("parser has to implement parser method")


class PointParser(Parser):
    def parse(self, binary_parser: BinaryParser):
        x = binary_parser.read_double()
        y = binary_parser.read_double()
        return Point(x, y)


class PolygonParser:

    def parse(self, binary_parser: BinaryParser):
        number_of_points = binary_parser.read_int()

        points = []
        for _ in range(number_of_points):
            x = binary_parser.read_double()
            y = binary_parser.read_double()
            points.append(Point(x, y))

        return Polygon(points)


class LinestringParser:

    def parse(self, binary_parser: BinaryParser):
        number_of_points = binary_parser.read_int()

        points = []
        for _ in range(number_of_points):
            x = binary_parser.read_double()
            y = binary_parser.read_double()
            points.append(Point(x, y))

        return Linestring(points)


parsers = {
    1: PointParser(),
    2: PolygonParser(),
    3: LinestringParser()
}


class GeometryType(UserDefinedType):
    @classmethod
    def sqlType(cls):
        return ArrayType(ByteType(), containsNull=False)

    def fromInternal(self, obj):
        return self.deserialize(obj)

    def toInternal(self, obj):
        pass

    def serialize(self, obj):
        pass

    def deserialize(self, datum):
        binary_parser = BinaryParser([el - 256 if el >= 128 else el for el in datum], 0)

        geom_type = binary_parser.read_int()
        geom = parsers[geom_type].parse(binary_parser)
        return geom

    @classmethod
    def module(cls):
        return "wde.sql.types.GeometryType"

    def needConversion(self):
        return True

    @classmethod
    def scalaUDT(cls):
        return "org.apache.udt.GeometryType"
