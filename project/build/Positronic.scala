import sbt._
import de.element34.sbteclipsify._

class MainProject(info: ProjectInfo) extends AndroidProject(info) with Eclipsify {
	override def androidPlatformName = "android-8"

	val scalaToolsSnapshots = ScalaToolsSnapshots
	val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test"
	val mockito = "org.mockito" % "mockito-core" % "1.8.2" % "test"
	val scalacheck = "org.scala-tools.testing" % "scalacheck_2.8.1" % "1.7" % "test"
}
