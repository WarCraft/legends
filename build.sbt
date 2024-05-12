lazy val commonSettings = Seq(
  organization := "gg.warcraft",
  version := "20.2.4-SNAPSHOT",
  scalaVersion := "2.13.12",
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
      "gg.warcraft" %% "monolith-api" % "20.2.4-SNAPSHOT" % Provided,
      "gg.warcraft" %% "chat-api" % "20.2.4-SNAPSHOT" % Provided,
      "gg.warcraft" %% "gathering-api" % "20.2.4-SNAPSHOT" % Provided,
      "gg.warcraft" %% "housing-api" % "20.2.4-SNAPSHOT" % Provided,
      "gg.warcraft" %% "combat-api" % "20.2.4-SNAPSHOT" % Provided,
      "gg.warcraft" %% "siege-api" % "20.2.4-SNAPSHOT" % Provided
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
      "io.papermc.paper" % "paper-api" % "1.20.2-R0.1-SNAPSHOT" % Provided,
      "com.github.NuVotifier" % "NuVotifier" % "2.6.+" % Provided,
      "gg.warcraft" %% "monolith-spigot" % "20.2.4-SNAPSHOT" % Provided,
      "gg.warcraft" %% "chat-spigot" % "20.2.4-SNAPSHOT" % Provided,
      "gg.warcraft" %% "gathering-spigot" % "20.2.4-SNAPSHOT" % Provided,
      "gg.warcraft" %% "housing-spigot" % "20.2.4-SNAPSHOT" % Provided,
      "gg.warcraft" %% "combat-spigot" % "20.2.4-SNAPSHOT" % Provided,
      "gg.warcraft" %% "siege-spigot" % "20.2.4-SNAPSHOT" % Provided,
      "LibsDisguises" % "LibsDisguises" % "10.0.+" % Provided intransitive ()
    )
  )
  .dependsOn(api)
