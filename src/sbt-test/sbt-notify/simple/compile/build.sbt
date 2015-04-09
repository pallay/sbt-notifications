//import AssemblyKeys._

version := "0.1.0"
 
scalaVersion := "2.10.4"

//assemblySettings
//
//jarName in assembly := "foo.jar"

//TaskKey[Unit]("check") <<= (crossTarget) map { (crossTarget) =>
//  val process = sbt.Process("java", Seq("-jar", (crossTarget / "foo.jar").toString))
//  val out = (process!!)
//  if (out.trim != "bye") error("unexpected output: " + out)
//  ()
//}