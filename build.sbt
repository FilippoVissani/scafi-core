val scalatest = "org.scalatest" %% "scalatest" % "3.2.15" % "test"
val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
val logback = "ch.qos.logback" % "logback-classic" % "1.3.5"

ThisBuild / scalaVersion := "2.13.6"
name := "scafi-core"
libraryDependencies ++= Seq(
  scalatest,
  scalaLogging,
  logback,
)