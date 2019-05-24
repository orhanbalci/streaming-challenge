import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "net.orhanbalci"
ThisBuild / organizationName := "log-generator"

lazy val root = (project in file("."))
  .settings(
    name := "log-generator",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.8.0" 
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
