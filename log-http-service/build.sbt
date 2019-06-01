import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.0.1"
ThisBuild / organization     := "net.orhanbalci"
ThisBuild / organizationName := "orhanbalci"

lazy val root = (project in file("."))
  .settings(
    name := "log-http-service",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.23",
    libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka" % "0.21.1",
    libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.8"

  )

 // See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
