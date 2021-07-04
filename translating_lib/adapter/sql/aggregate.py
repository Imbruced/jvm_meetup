from pyspark.sql.types import StructType, StructField, StringType, ArrayType, IntegerType

from sql.logic import create_aggregate
from tutorial.test_base import SparkBase


class TestAggregate(SparkBase):

    def test_agg_example(self):
        # Given data frame with input data
        dataframe = self.spark.createDataFrame(
            [["1", [2, 5, 6]],
             ["2", [2, 4, 9]],
             ["1", [1, 8, 9]]],
            schema=StructType([
                StructField("id", StringType(), False),
                StructField("elements", ArrayType(IntegerType(), False))
            ])
        )

        # When creating an aggregate
        result_df = create_aggregate(dataframe, 3, self.spark)

        result_df.show()

        # Then count should be appropriate
        assert result_df.count() == 2

        # And elements should be as expected
        collected_elements = {el[0]: set(el[1]) for el in result_df.collect()}

        assert collected_elements["1"] == {6, 9}
        assert collected_elements["2"] == {9}
