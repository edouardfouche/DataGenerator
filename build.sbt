name := "DataGenerator"
organization:= "io.github.edouardfouche"

version := "0.1.1"

scalaVersion := "2.12.8"
crossScalaVersions := Seq("2.11.8", "2.12.8") // prefix with "+" to perform for both .e.g, "+ compile"

libraryDependencies ++= Seq(
  // Last stable release
  "org.scalanlp" %% "breeze" % "0.13.1",
  // The visualization library is distributed separately as well.
  // It depends on LGPL code
  //"org.scalanlp" %% "breeze-viz" % "0.13.1"
)

libraryDependencies += "org.jfree" % "jfreechart" % "1.0.19" // LPGL license

//libraryDependencies += "org.jzy3d" % "jzy3d-api" % "0.9.1"
libraryDependencies += "org.jzy3d" % "jzy3d-api" % "1.0.0" //from "http://maven.jzy3d.org/releases/"
resolvers += "Jzy3d Maven Release Repository" at "http://maven.jzy3d.org/releases"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0"
resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

fork in run := true

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

////// Sonatype

useGpg := true
pgpReadOnly := false

ThisBuild / organization := "io.github.edouardfouche.DataGenerator"
ThisBuild / organizationName := "edouardfouche"
ThisBuild / organizationHomepage := Some(url("https://github.com/edouardfouche"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/edouardfouche/MCDE"),
    "scm:git@github.com:edouardfouche/MCDE.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "edouardfouche",
    name  = "Edouard Fouché",
    email = "edouard.fouche@kit.edu",
    url   = url("https://github.com/edouardfouche")
  )
)

ThisBuild / description := "A bunch of little data generators to simulate multivariate dependencies."
ThisBuild / licenses := List("AGPLv3" -> url("https://www.gnu.org/licenses/agpl-3.0.en.html"))
ThisBuild / homepage := Some(url("https://github.com/edouardfouche/DataGenerator"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true

publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

////////////////