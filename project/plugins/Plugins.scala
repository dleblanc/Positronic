import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val android = "org.scala-tools.sbt" % "android-plugin" % "0.4.1"
  lazy val eclipse = "de.element34" % "sbt-eclipsify" % "0.5.1"
}
