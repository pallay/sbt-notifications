package notify

/**
 * Container for test image paths
 */
import sbt._

class NotifyTestImages(pass: Option[String], fail: Option[String], error: Option[String]) {

  import NotifyTestImages._

  def passIcon  = Some(pass.getOrElse(defaultPass))
  def failIcon  = Some(fail.getOrElse(defaultFail))
  def errorIcon = Some(error.getOrElse(defaultError))

  final def copy(pass: Option[String] = passIcon, fail: Option[String] = failIcon, error: Option[String] = errorIcon): NotifyTestImages = new NotifyTestImages(pass, fail, error)

  override def toString = s"NotifyTestImages("+passIcon+","+failIcon+","+errorIcon+")"
}

object NotifyTestImages {

  def apply(pass: Option[String], fail: Option[String], error: Option[String]) = new NotifyTestImages(pass, fail, error)

  def iconPath(iconFileName: String): String = {
    val image = file(System.getProperty("user.home")) / ".sbt" / "sbt-notify" / "icons" / iconFileName
    if(!image.exists) {
      IO.createDirectory(image.getParentFile)
      IO.transfer(getClass.getClassLoader.getResourceAsStream(iconFileName), image)
    }
    image.getAbsolutePath
  }

  lazy val defaultPass  = iconPath("pass.png")
  lazy val defaultFail  = iconPath("fail.png")
  lazy val defaultError = iconPath("error.png")

}
