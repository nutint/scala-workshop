import AssemblyKeys._
import Keys._

name := "scala-workshop"

version := "1.0"

scalaVersion := "2.12.3"

lazy val akkaVersion = "2.5.19"
lazy val akkaHttpVersion = "10.1.8"
lazy val scalaTestVersion = "3.0.5"
lazy val macwireVersion = "2.3.2"
lazy val circeVersion = "0.10.0"

scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "io.spray" %% "spray-json" % "1.3.3",
  "ch.megard" %% "akka-http-cors" % "0.3.1",
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.mockito" % "mockito-all" % "1.8.4" % "test",
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "net.cakesolutions" %% "validated-config" % "1.1.3",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.4.2",
  "com.google.firebase" % "firebase-admin" % "6.5.0",
  "com.itextpdf" % "itextpdf" % "5.5.13",
  "com.itextpdf" % "html2pdf" % "2.0.0",
  "ch.megard" %% "akka-http-cors" % "0.3.1",
  "com.chuusai" %% "shapeless" % "2.3.3",
  "com.softwaremill.macwire" %% "macros" % macwireVersion % "provided",
  "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % "provided",
  "com.softwaremill.macwire" %% "util" % macwireVersion,
  "com.softwaremill.macwire" %% "proxy" % macwireVersion,
  "org.typelevel" %% "cats-core" % "1.6.0",
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeVersion
)


assemblySettings

mergeStrategy in assembly := {
  case x if Assembly.isConfigFile(x) => MergeStrategy.concat
  case PathList(ps@_*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) => MergeStrategy.rename
  case PathList("META-INF", xs@_*) =>
    xs map {
      _.toLowerCase
    } match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) => MergeStrategy.discard
      case ps@(x :: _) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") => MergeStrategy.discard
      case "plexus" :: _ => MergeStrategy.discard
      case "services" :: _ => MergeStrategy.filterDistinctLines
      case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) => MergeStrategy.filterDistinctLines
      case _ => MergeStrategy.first
    }
  case PathList(_*) => MergeStrategy.first
}


mainClass in assembly := Some("com.nat.scalaworkshop.ServerApp")