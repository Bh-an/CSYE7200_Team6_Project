ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  "org.locationtech.geotrellis" %% "geotrellis-raster" % "3.6.3",
  "org.apache.commons" % "commons-lang3" % "3.12.0",
  "org.scala-lang" % "scala-compiler" % scalaVersion.value
)



lazy val root = (project in file("."))
  .settings(
    name := "analyze"
  )
