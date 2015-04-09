sbtPlugin := true

name := "sbt-notify"

organization := "com.raunu"

version <<= sbtVersion(v =>
  if (v.startsWith("0.11") || v.startsWith("0.12") || v.startsWith("0.13")) "0.1.0-SNAPSHOT"
  else sys.error("Unsupported sbt version %s" format v)
)

description := "sbt plugin to create system notifications of test failures or errors for red/green testing"

scalacOptions ++= Seq("-feature", "-deprecation")

sbtVersion in Global := "0.13.8"

scalaVersion in Global := "2.10.4"

licenses <<= version(v => Seq("MIT" -> url(
  "https://github.com/pallay/sbt-notify/blob/%s/LICENSE" format v)))

//packageBin in Compile := file(s"${name.value}_${scalaBinaryVersion.value}.jar")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

resolvers += "Mulesoft" at "http://repository.mulesoft.org/releases"

resolvers += Opts.resolver.sonatypeReleases

resolvers += Opts.resolver.mavenLocalFile
