val cats = "org.typelevel" %% "cats" % "0.4.1"
val scalaTest = "org.scalatest" %% "scalatest" % "2.2.4" % "test"
val simulacrum = "com.github.mpilquist" %% "simulacrum" % "0.7.0"

val circeCore = "io.circe" %% "circe-core" % "0.3.0"
val circeGeneric = "io.circe" %% "circe-generic" % "0.3.0"
val circeParser = "io.circe" %% "circe-parser" % "0.3.0"

val finchCore = "com.github.finagle" %% "finch-core" % "0.10.0"
val finchCirce = "com.github.finagle" %% "finch-circe" % "0.10.0"

val http4sDsl = "org.http4s" %% "http4s-dsl" % "0.12"
val http4sBlaze = "org.http4s" %% "http4s-blaze-server" % "0.12"

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

lazy val root = project.enablePlugins(ScalaJSPlugin)

lazy val formless = project.in(file(".")).settings(settings)
  .aggregate(core, finch)
  .dependsOn(core)

lazy val core = project.settings(settings)

lazy val finch = project.settings(settings)
  .settings(libraryDependencies ++= Seq(finchCore, finchCirce))
  .dependsOn(core)

lazy val http4s = project.settings(settings)
  .settings(libraryDependencies ++= Seq(http4sDsl, http4sBlaze))
  .dependsOn(core)

val scalaJsReactVersion = "0.10.4"
val scalaCssVersion = "0.3.1"
lazy val ui = project.enablePlugins(ScalaJSPlugin).settings(settings)
  .settings(workbenchSettings)
  .settings(
    Seq(
      bootSnippet := "FormlessMain.main()",
      libraryDependencies ++= Seq(
         "com.github.japgolly.scalajs-react" %%% "core" % scalaJsReactVersion,
          "com.github.japgolly.scalajs-react" %%% "extra" % scalaJsReactVersion,
          "com.github.japgolly.scalacss" %%% "core" % scalaCssVersion,
          "com.github.japgolly.scalacss" %%% "ext-react" % scalaCssVersion
      ),
      jsDependencies ++= Seq(
        "org.webjars.bower" % "react" % "0.14.7"
          /        "react-with-addons.js"
          minified "react-with-addons.min.js"
          commonJSName "React",
        "org.webjars.bower" % "react" % "0.14.7"
          /         "react-dom.js"
          minified  "react-dom.min.js"
          dependsOn "react-with-addons.js"
          commonJSName "ReactDOM",
        "org.webjars" % "jquery" % "1.11.1"
          /         "jquery.js"
          minified  "jquery.min.js",
        "org.webjars" % "bootstrap" % "3.3.2"
          /         "bootstrap.js"
          minified  "bootstrap.min.js"
          dependsOn "jquery.js"
      )
    )
  )
