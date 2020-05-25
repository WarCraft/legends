lazy val commonSettings = Seq(
  organization := "gg.warcraft",
  version := "15.0.0-SNAPSHOT",
  scalaVersion := "2.13.2",
  scalacOptions ++= Seq(
    "-language:implicitConversions"
  ),
  resolvers ++= Seq(
    Resolver.mavenLocal,
    "Spigot" at "https://repo.md-5.net/content/groups/public/"
  )
)

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := s"${name.value}-${version.value}-all.jar",
  assemblyOption in assembly :=
    (assemblyOption in assembly).value.copy(includeScala = false),
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", _ @_*) => MergeStrategy.discard
    case "module-info.class"         => MergeStrategy.discard
    case it                          => (assemblyMergeStrategy in assembly).value(it)
  }
)

lazy val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

lazy val api = (project in file("legends-api"))
  .settings(
    name := "legends-api",
    commonSettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      "gg.warcraft" %% "monolith-api" % "15.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "chat-api" % "15.0.0-SNAPSHOT" % Provided,
      // "gg.warcraft" %% "gathering-api" % "15.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "combat-api" % "15.0.0-SNAPSHOT" % Provided
    )
  )

lazy val spigot = (project in file("legends-spigot"))
  .settings(
    name := "legends-spigot",
    commonSettings,
    assemblySettings,
    resolvers ++= Seq(
      "PaperMC" at "https://papermc.io/repo/repository/maven-public/"
    ),
    libraryDependencies ++= commonDependencies ++ Seq(
      "com.destroystokyo.paper" % "paper-api" % "1.15.2-R0.1-SNAPSHOT" % Provided,
      "gg.warcraft" %% "monolith-spigot" % "15.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "combat-spigot" % "15.0.0-SNAPSHOT" % Provided,
      "LibsDisguises" % "LibsDisguises" % "10.0.12" % Provided
    )
  )
  .dependsOn(api)
