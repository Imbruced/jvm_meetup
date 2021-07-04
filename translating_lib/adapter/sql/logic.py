# BusinessLogicCalculator
from py4j.java_gateway import java_import
from pyspark.sql import DataFrame, SparkSession


def create_aggregate(dataframe: DataFrame, division_number: int, spark: SparkSession) -> DataFrame:
    jvm = spark._jvm

    java_import(jvm, "sparkbussinesslogic.BusinessLogicCalculator")

    return DataFrame(jvm.BusinessLogicCalculator.calculate(
        dataframe._jdf, division_number
    ), spark._wrapped)
