from py4j.java_gateway import java_import
from pyspark.sql import SparkSession


imports = [
    "warsaw.sql.GeometryAttributesRegistrator"
]


def register_functions(spark: SparkSession) -> bool:
    for import_lib in imports:
        java_import(spark._jvm, import_lib)
        try:
            spark._jvm.GeometryAttributesRegistrator.registerAll(spark._jsparkSession)
        except Exception as e:
            pass
        else:
            return True
    return False

