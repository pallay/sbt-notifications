publishMavenStyle := true

pomIncludeRepository := { _ => false }

publishTo := Some(Classpaths.sbtPluginReleases)

publishArtifact in Test := false

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
