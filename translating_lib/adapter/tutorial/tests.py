import pytest

from tutorial.context import JvmContext
from py4j.java_gateway import java_import


class TestsExamples:

    testing_port = 8998

    def test_should_create_and_manipulate_local_date_object(self):
        with JvmContext(self.testing_port) as context:
            local_date = context.jvm.java.time.LocalDate.parse("2020-01-02")

            two_days_after = local_date.plusDays(2)

            assert two_days_after.toString() == "2020-01-04"

    def test_should_create_collections(self):
        with JvmContext(self.testing_port) as context:
            int_cls = context.gateway.jvm.int

            array_with_int = context.gateway.new_array(int_cls, 2)

            array_with_int[0] = 1
            array_with_int[1] = 2
            array_with_int[0] = 24

            with pytest.raises(IndexError):
                array_with_int[2] = 3

            elements_sum = 0
            for element in array_with_int:
                elements_sum += element

            assert elements_sum == 26

    def test_creating_point(self):
        with JvmContext(self.testing_port) as context:
            java_import(context.gateway.jvm, 'geometry.primitive.Point')
            assert context.jvm.Point(21.0, 52.0).length() == 0
            assert context.jvm.Point(21.0, 52.0).area() == 0

    def test_creating_polygon(self):
        with JvmContext(self.testing_port) as context:
            java_import(context.gateway.jvm, 'geometry.primitive.Point')
            java_import(context.gateway.jvm, 'geometry.primitive.Polygon')
            Point = context.jvm.Point
            Polygon = context.jvm.Polygon

            points = [
                Point(0.0, 0.0), Point(0.0, 1.0), Point(1.0, 1.0), Point(1.0, 0.0), Point(0.0, 0.0)
            ]

            point_array = context.gateway.new_array(Point, 5)

            for index in range(5):
                point_array[index] = points[index]

            assert Polygon.create(point_array).length() == 4
            assert Polygon.create(point_array).area() == 1

    def test_linestring_creation(self):
        with JvmContext(self.testing_port) as context:
            java_import(context.gateway.jvm, 'geometry.primitive.Point')
            java_import(context.gateway.jvm, 'geometry.primitive.Linestring')
            Point = context.jvm.Point
            Linestring = context.jvm.Linestring

            points = [
                Point(0.0, 0.0), Point(0.0, 1.0), Point(1.0, 1.0), Point(1.0, 0.0)
            ]

            point_array = context.gateway.new_array(Point, 4)

            for index in range(4):
                point_array[index] = points[index]

            assert Linestring.create(point_array).length() == 3
            assert Linestring.create(point_array).area() == 0
