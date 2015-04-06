sbtPlugin := true

name := "sbt-notify"

organization := "com.raunu"

version <<= sbtVersion(v =>
  if (v.startsWith("0.11") || v.startsWith("0.12") || v.startsWith("0.13")) "0.1.0"
  else sys.error("Unsupported sbt version %s" format v)
)

scalacOptions ++= Seq("-feature", "-deprecation")

sbtVersion in Global := "0.13.8"

scalaVersion in Global := "2.10.4"

resolvers += Opts.resolver.sonatypeReleases

publishTo := Some(Classpaths.sbtPluginReleases)

publishMavenStyle := false

publishArtifact in Test := false

licenses <<= version(v => Seq("MIT" -> url(
  "https://github.com/pallay/sbt-notify/blob/%s/LICENSE" format v)))

packageBin in Compile := file(s"${name.value}_${scalaBinaryVersion.value}.jar")

pomExtra := (
  <scm>
    <url>git@github.com:pallay/sbt-notify.git</url>
    <connection>scm:git:git@github.com:pallay/sbt-notify.git</connection>
  </scm>
  <developers>
    <developer>
      <id>raunu</id>
      <name>Pallay Raunu</name>
      <url>https://github.com/pallay</url>
    </developer>
  </developers>
)