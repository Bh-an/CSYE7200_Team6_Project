name := "image-processing"

version := "1.0"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "javax.media" % "jai_core" % "1.1.3",
  "javax.media" % "jai_codec" % "1.1.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-encoding", "UTF-8",
  "-Xfatal-warnings"
)
