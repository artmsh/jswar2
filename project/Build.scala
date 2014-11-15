import sbt._
import Keys._

object ApplicationBuild extends Build {

    val appName         = "WarCraft_2_on_JavaScript"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "org.scalatest" %% "scalatest" % "1.9.1" % "test",
//      "org.scala-tools.sbinary" % "sbinary_2.10" % "0.4.1",
      "se.radley" %% "play-plugins-enumeration" % "1.1.0",
      "com.github.mpilquist" %% "scodec" % "1.0.0-SNAPSHOT",
      "org.specs2" %% "specs2-core" % "2.4.11" % "test",
      "com.typesafe.akka" %% "akka-cluster" % "2.2.0",
      "org.julienrf" %% "play-json-variants" % "1.0.0"
    )

  val main = Project(appName, file(".")).enablePlugins(play.PlayScala).settings(
      // Add your own project settings here
      version := appVersion,
      libraryDependencies ++= appDependencies,
      scalaVersion := "2.10.3",
      testOptions in Test := Nil,
      resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
    )

}
