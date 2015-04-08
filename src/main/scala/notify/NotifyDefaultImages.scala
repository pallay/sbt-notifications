package notify

/**
 * Container for test image paths
 */
import sbt._

class NotifyDefaultImages(pass: Option[String], fail: Option[String], error: Option[String]) {

  import NotifyDefaultImages._

  def passIcon  = Some(pass.getOrElse(defaultPass))
  def failIcon  = Some(fail.getOrElse(defaultFail))
  def errorIcon = Some(error.getOrElse(defaultError))

  final def copy(pass: Option[String] = passIcon, fail: Option[String] = failIcon, error: Option[String] = errorIcon): NotifyDefaultImages = new NotifyDefaultImages(pass, fail, error)

  override def toString = s"NotifyDefaultImages("+passIcon+","+failIcon+","+errorIcon+")"
}

object NotifyDefaultImages {

  def apply(pass: Option[String], fail: Option[String], error: Option[String]) = new NotifyDefaultImages(pass, fail, error)

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
