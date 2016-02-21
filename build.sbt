val cats = "org.typelevel" %% "cats" % "0.4.1"
val scalaTest = "org.scalatest" %% "scalatest" % "2.2.4" % "test"
val simulacrum = "com.github.mpilquist" %% "simulacrum" % "0.7.0"

val circeCore = "io.circe" %% "circe-core" % "0.3.0"
val circeGeneric = "io.circe" %% "circe-generic" % "0.3.0"
val circeParser = "io.circe" %% "circe-parser" % "0.3.0"

val finchCore = "com.github.finagle" %% "finch-core" % "0.10.0"
val finchCirce = "com.github.finagle" %% "finch-circe" % "0.10.0"

val http4sDsl = "org.http4s" %% "http4s-dsl" % "0.12.1"
val http4sBlaze = "org.http4s" %% "http4s-blaze-server" % "0.12.1"
val http4sCirce = "org.http4s" %% "http4s-circe" % "0.12.1"

val macroParadise = compilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full)
val kindProjector = compilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")

val compilerOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",       // yes, this is 2 args
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import"     // 2.11 only
)

val settings = Seq(
  scalaVersion := "2.11.7",
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    Resolver.sonatypeRepo("snapshots"), Resolver.sonatypeRepo("releases")
  ),
  libraryDependencies ++= Seq(
    cats, simulacrum, kindProjector,
    circeCore, circeGeneric, circeParser,
    scalaTest, macroParadise
  )
)

lazy val formless = project.in(file(".")).settings(settings)
  .aggregate(core, finch, http4s)
  .dependsOn(core)

lazy val core = project.settings(settings)

lazy val finch = project.settings(settings)
  .settings(libraryDependencies ++= Seq(finchCore, finchCirce))
  .dependsOn(core)

lazy val http4s = project.settings(settings)
  .settings(libraryDependencies ++= Seq(http4sDsl, http4sBlaze, http4sCirce))
  .dependsOn(core)
