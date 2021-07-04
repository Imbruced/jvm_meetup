package sparkbussinesslogic

import org.apache.spark.sql.DataFrame

//
//Dataframe in format
//  id: String
//  numbers: Array[Int]
//

object BusinessLogicCalculator {

  def calculate(dataframe: DataFrame, divisionNumber: Int): DataFrame = {
    import dataframe.sparkSession.implicits._

    dataframe
      .as[PersonNumber]
      .map { personNumber =>
        val elementsFiltered = personNumber.elements.filter(_ % divisionNumber == 0)
        PersonNumberFiltered(personNumber.id, elementsFiltered)
      }
      .groupByKey(_.id)
      .reduceGroups { (left, right) => left.copy(elements = (left.elements ++ right.elements).distinct) }
      .map(_._2)
      .toDF()
  }

  case class PersonNumber(id: String, elements: Array[Int])
  case class PersonNumberFiltered(id: String, elements: Array[Int])
}
