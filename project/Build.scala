import sbt._
import Keys._

object ApplicationBuild extends Build {

    val appName         = "WarCraft_2_on_JavaScript"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
      "org.scala-tools.sbinary" % "sbinary_2.10" % "0.4.1",
      "se.radley" %% "play-plugins-enumeration" % "1.1.0",
      "com.github.mpilquist" %% "scodec" % "1.0.0.SNAP3"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here
      scalaVersion := "2.10.1",
      testOptions in Test := Nil,
      resolvers += Resolver.typesafeIvyRepo("releases")
    )

}
