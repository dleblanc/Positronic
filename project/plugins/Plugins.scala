import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val android = "org.scala-tools.sbt" % "sbt-android-plugin" % "0.5.0"
  lazy val eclipsify = "de.element34" % "sbt-eclipsify" % "0.7.0"
}
