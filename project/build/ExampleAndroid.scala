import sbt._

trait Defaults {
  def androidPlatformName = "android-4"
}

class ExampleAndroid(info: ProjectInfo) extends ParentProject(info) {
  override def shouldCheckOutputDirectories = false
  override def updateAction = task { None } 

  lazy val main  = project(".", "ExampleAndroid", new MainProject(_))
  lazy val tests = project("tests",  "tests", new TestProject(_), main)

  class MainProject(info: ProjectInfo) extends AndroidProject(info) with Defaults {    
    val scalatest = "org.scalatest" % "scalatest" % "1.0" % "test"
    val mockito = "org.mockito" % "mockito-core" % "1.8.2" % "test"
    val scalacheck = "org.scala-tools.testing" % "scalacheck_2.7.7" % "1.6" % "test"
  }

  class TestProject(info: ProjectInfo) extends AndroidTestProject(info) with Defaults

}
