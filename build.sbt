lazy val commonSettings = Seq(
  organization := "gg.warcraft",
  version := "15.0.0-SNAPSHOT",
  scalaVersion := "2.13.1",
  scalacOptions ++= Seq(
    "-language:implicitConversions"
  ),
  resolvers ++= Seq(
    Resolver.mavenLocal
  )
)

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := s"${name.value}-${version.value}-all.jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", it @ _*) => MergeStrategy.discard
    case "module-info.class"           => MergeStrategy.discard
    case it                            => (assemblyMergeStrategy in assembly).value(it)
  }
)

lazy val commonDependencies = Seq(
  "gg.warcraft" %% "monolith-api" % "15.0.0-SNAPSHOT",
  "gg.warcraft" %% "combat-api" % "15.0.0-SNAPSHOT",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

lazy val api = (project in file("legends-api"))
  .settings(
    name := "legends-api",
    commonSettings,
    libraryDependencies ++= commonDependencies
  )

lazy val app = (project in file("legends-app"))
  .settings(
    name := "legends-app",
    commonSettings,
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(api)

lazy val spigot = (project in file("legends-spigot"))
  .settings(
    name := "legends-spigot",
    commonSettings,
    assemblySettings,
    resolvers ++= Seq(
      "PaperMC" at "https://papermc.io/repo/repository/maven-public/"
    ),
    libraryDependencies ++= commonDependencies ++ Seq(
      "com.destroystokyo.paper" % "paper-api" % "1.15.1-R0.1-SNAPSHOT" % Provided
    )
  )
  .dependsOn(app)
