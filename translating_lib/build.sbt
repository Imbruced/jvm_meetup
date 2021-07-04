name := "translating_lib"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "net.sf.py4j" % "py4j" % "0.10.9.2",
  "org.apache.spark" %% "spark-sql" % "3.1.2" % "provided",
  "com.beachape" %% "enumeratum" % "1.6.1",
  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
  "com.softwaremill.diffx" %% "diffx-scalatest" % "0.5.2" % Test
)
