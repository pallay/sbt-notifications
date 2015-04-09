
{
  val pluginVersion = System.getProperty("plugin.version")
  if(pluginVersion == null)
    throw new RuntimeException("""|The system property 'plugin.version' is not defined.
                                  |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
  else addSbtPlugin("com.raunu" % "sbt-notify" % pluginVersion)
}

//addSbtPlugin("com.raunu" % "sbt-notify" % sys.props("project.version"))

//libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4"
