from pyspark.sql.functions import expr

from tutorial.test_base import SparkBase


class TestGeospatial(SparkBase):

    def test_st_geom_from_text(self):
        # When creating geometry column based GeomFromString function
        gdf = self.__create_geometry_frame()

        # Then geometries collected should be as expected
        wkts = [row[0].wkt() for row in gdf.select("geometry").collect()]

        assert set(wkts) == {
            'LINESTRING(0.0 0.0, 1.0 0.0, 1.0 1.0, 0.0 1.0)',
            'POINT(21.0 52.0)',
            'POLYGON((0.0 0.0, 1.0 0.0, 1.0 1.0, 0.0 1.0, 0.0 0.0))'
        }

    def test_st_length(self):
        # Given creating geometry column based GeomFromString function
        gdf = self.__create_geometry_frame()

        # When using st length function
        gdf_with_length = gdf.withColumn("length", expr("ST_Length(geometry)"))

        #Then lengths should be as expected
        lengths = [row[0] for row in gdf_with_length.select("length").collect()]

        assert set(lengths) == {0.0, 3.0, 4.0}

    def test_st_area(self):
        # Given creating geometry column based GeomFromString function
        gdf = self.__create_geometry_frame()

        # When using st area function
        gdf_with_length = gdf.withColumn("area", expr("ST_Area(geometry)"))

        #Then areas should be as expected
        lengths = [row[0] for row in gdf_with_length.select("area").collect()]

        assert set(lengths) == {0.0, 1.0}

    def __create_geometry_frame(self):
        # Given list of wkt strings
        wkt_strings = [
            ["POINT(21 52)"],
            ["POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))"],
            ["LINESTRING((0 0, 1 0, 1 1, 0 1))"]
        ]

        # And df with wkt string based on previous list
        df = self.spark.createDataFrame(wkt_strings). \
            selectExpr("_1 AS geom_wkt")

        return df.withColumn("geometry", expr("ST_GeomFromString(geom_wkt)"))

