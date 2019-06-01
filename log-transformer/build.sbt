// give the user a nice default project!

lazy val root = (project in file(".")).

  settings(
    inThisBuild(List(
      organization := "net.orhanbalci",
      scalaVersion := "2.11.12"
    )),
    name := "log-transformer",
    version := "0.0.1",

    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled"),
    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    parallelExecution in Test := false,
    fork := true,

    coverageHighlighting := true,

    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-streaming" % "2.2.2" ,
      "org.apache.spark" %% "spark-sql" % "2.2.2" ,
      "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.2.2" 

    ),

    // uses compile classpath for the run task, including "provided" jar (cf http://stackoverflow.com/a/21803413/3827)
    //run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated,

    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    pomIncludeRepository := { x => false },

   resolvers ++= Seq(
      "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/",
      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Second Typesafe repo" at "http://repo.typesafe.com/typesafe/maven-releases/",
      Resolver.sonatypeRepo("public")
    ),

    pomIncludeRepository := { x => false },

    assemblyMergeStrategy in assembly := {
        case PathList("org","aopalliance", xs @ _* ) => MergeStrategy.last
        case PathList("javax","inject", xs @ _*) => MergeStrategy.last
        case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
        case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
        case PathList("org", "apache", xs @ _*) => MergeStrategy.last
        case PathList("com", "google", xs @ _*) => MergeStrategy.last
        case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
        case PathList("com", "codahale", xs @ _*) =>MergeStrategy.last
        case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
        case x =>
            val oldStrategy = (assemblyMergeStrategy in assembly).value
            oldStrategy(x)
    }

  )
