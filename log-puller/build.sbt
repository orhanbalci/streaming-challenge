import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.0.1"
ThisBuild / organization     := "net.orhanbalci"
ThisBuild / organizationName := "orhanbalci"

lazy val root = (project in file("."))
  .settings(
    name := "log-puller",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.23",
    libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka" % "0.21.1",
    libraryDependencies += "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "1.0.2",
    libraryDependencies += "io.circe" %% "circe-parser" % "0.12.0-M1",
    libraryDependencies += "io.circe" %% "circe-core" % "0.12.0-M1",
    libraryDependencies += "io.circe" %% "circe-generic" % "0.12.0-M1",
    libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1206-jdbc42"

  )

 // See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
