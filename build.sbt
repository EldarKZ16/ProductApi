name := "ProductApi"

version := "0.1-SNAPSHOT"

scalaVersion := "2.13.5"

lazy val http4sVersion    = "0.23.1"
lazy val logbackVersion   = "1.2.3"
lazy val circeVersion     = "0.14.1"
lazy val jodaTimeVersion  = "2.10.5"
lazy val configVersion    = "1.4.1"
lazy val scalaTestVersion = "3.2.9"

libraryDependencies ++= Seq(
  "org.http4s"      %% "http4s-dsl"          % http4sVersion,
  "org.http4s"      %% "http4s-blaze-server" % http4sVersion,
  "org.http4s"      %% "http4s-blaze-client" % http4sVersion,
  "org.http4s"      %% "http4s-circe"        % http4sVersion,
  "ch.qos.logback"  %  "logback-classic"     % logbackVersion,
  "io.circe"        %% "circe-generic"       % circeVersion,
  "joda-time"       %  "joda-time"           % jodaTimeVersion,
  "com.typesafe"    %  "config"              % configVersion,
  "org.scalatest"   %% "scalatest"           % scalaTestVersion % Test
)

