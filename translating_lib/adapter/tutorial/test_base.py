from pyspark.sql import SparkSession

from sql.register import register_functions


class SparkBase:

    @property
    def spark(self):
        spark = SparkSession.builder. \
            appName("warsaw-data-engineering"). \
            master("local[*]"). \
            config("spark.jars",
                   "/home/pawel/Desktop/prezentacja/warsaw_data_engineering/translating_lib/target/scala-2.12/translating_lib-assembly-0.1.jar").\
            getOrCreate()

        register_functions(spark)

        setattr(self, "__spark", spark)
        return getattr(self, "__spark")
