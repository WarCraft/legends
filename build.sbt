lazy val commonSettings = Seq(
  organization := "gg.warcraft",
  version := "16.0.0-SNAPSHOT",
  scalaVersion := "2.13.11",
  scalacOptions ++= Seq(
    "-language:implicitConversions"
  ),
  resolvers ++= Seq(
    Resolver.mavenLocal
  )
)

lazy val assemblySettings = Seq(
  assembly / assemblyJarName := s"${name.value}-${version.value}-all.jar",
  assembly / assemblyOption ~= { _.withIncludeScala(false) },
  assembly / assemblyMergeStrategy := {
    case PathList("META-INF", _ @_*) => MergeStrategy.discard
    case "module-info.class"         => MergeStrategy.discard
    case it                          => (assembly / assemblyMergeStrategy).value(it)
  }
)

lazy val api = (project in file("legends-api"))
  .settings(
    name := "legends-api",
    commonSettings,
    libraryDependencies ++= Seq(
      "gg.warcraft" %% "monolith-api" % "16.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "chat-api" % "16.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "gathering-api" % "16.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "housing-api" % "16.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "combat-api" % "16.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "siege-api" % "16.0.0-SNAPSHOT" % Provided
    ) ++ Seq(
      "org.scalatest" %% "scalatest" % "3.2.+" % Test
    )
  )

lazy val spigot = (project in file("legends-spigot"))
  .settings(
    name := "legends-spigot",
    commonSettings,
    assemblySettings,
    resolvers ++= Seq(
      "PaperMC" at "https://papermc.io/repo/repository/maven-public/",
      "SpigotMC" at "https://repo.md-5.net/content/groups/public/",
      "ProtocolLib" at "https://repo.dmulloy2.net/nexus/repository/public/",
      "Votifier" at "https://jitpack.io"
    ),
    libraryDependencies ++= Seq(
      "com.destroystokyo.paper" % "paper-api" % "1.16.5-R0.1-SNAPSHOT" % Provided,
      "com.github.NuVotifier" % "NuVotifier" % "2.6.+" % Provided,
      "gg.warcraft" %% "monolith-spigot" % "16.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "chat-spigot" % "16.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "gathering-spigot" % "16.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "housing-spigot" % "16.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "combat-spigot" % "16.0.0-SNAPSHOT" % Provided,
      "gg.warcraft" %% "siege-spigot" % "16.0.0-SNAPSHOT" % Provided,
      "LibsDisguises" % "LibsDisguises" % "10.0.+" % Provided intransitive ()
    )
  )
  .dependsOn(api)
