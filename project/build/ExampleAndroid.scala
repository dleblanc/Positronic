import sbt._
import de.element34.sbteclipsify._

class MainProject(info: ProjectInfo) extends AndroidProject(info) with Eclipsify {
	def androidPlatformName = "android-4"

	val scalaToolsSnapshots = ScalaToolsSnapshots
	val scalatest = "org.scalatest" % "scalatest" % "1.0.1-for-scala-2.8.0.RC1-SNAPSHOT" % "test"
	val mockito = "org.mockito" % "mockito-core" % "1.8.2" % "test"
	val scalacheck = "org.scala-tools.testing" % "scalacheck_2.8.0.RC1" % "1.7" % "test"
}
