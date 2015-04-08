package notify

import org.scalatools.testing.{Event => TEvent, Logger => TLogger, Result => TResult}
import sbt._

trait Notifier {
  /** Sends the message to the notifier system. */
  def notify(msg: NotifyResultFormat): Unit
}

object Notifier {
  def apply(): Notifier = {

    // TODO - Fix !! blocking calls
    def isMacNotificationFriendly = try {
      Process("which terminal-notifier").!! matches ".*terminal-notifier\\s+"
    } catch {
      case e: Exception => false
    }

    def isLibNotifyBinFriendly = try {
      Process("which notify-send").!! matches ".*notify-send\\s+"
    } catch {
      case e: Exception => false
    }

    def isGrowlNotifyFriendly = try {
      Process("where growlnotify").!! replaceAll("[\n\r]", " ") matches ".*growlnotify.*"
    } catch {
      case e: Exception => false
    }

    def isMac = System.getProperty("os.name").toLowerCase.indexOf("mac") >= 0

    if(isMac && isMacNotificationFriendly) new NotificationCentreNotifier
    else if (isMac) new NullNotifier // TODO - new MacNotifier
    else if(isLibNotifyBinFriendly) new LibNotifyBinNotifier
    else if(isGrowlNotifyFriendly) new GrowlNotifier
    else new NullNotifier
  }
}

final class NotificationCentreNotifier extends Notifier {
  override def notify(msg: NotifyResultFormat): Unit = {
    val args = Seq(
      "-appIcon", msg.imagePath.getOrElse(""),
      "-title", msg.title,
      "-message", msg.message,
      "-activate", "com.apple.Terminal") // Terminal is always installed on a Mac
    val sender = Process("terminal-notifier" +: args)
    sender !
  }
  override def toString = "terminal-notifier"
}

// TODO - implement MacNotifier
//final class MacNotifier extends Notifier {
//  override def notify(msg: NotifyResultFormat): Unit = {
//    val img = msg.imagePath.getOrElse("")
//    val base = meow.Growl title(msg.title) identifier(msg.id.getOrElse(msg.title)) message(msg.message)
//    val rich = if(img.isEmpty) base else base.image(img)
//    (if(msg.sticky) rich.sticky() else rich).meow
//  }
//  override def toString = "mac-growl"
//}

final class NullNotifier extends Notifier {
  override def notify(msg: NotifyResultFormat): Unit = ()
  override def toString = "<no notifier system found on this system>"
}

// Note: This class uses notify-send which requires libnotify-bin to be installed on Ubuntu.
final class LibNotifyBinNotifier extends Notifier {
  override def notify(msg: NotifyResultFormat): Unit = {
    val args = Seq(
      // TODO - Urgency
      // TODO - Categories
      // time-to-expire
      "-t", if(msg.sticky) "500" else "100",
      // icon - TODO - Ubuntu default icon.
      "-i", msg.imagePath.getOrElse(""),
      msg.title, msg.message
      )
    val sender = Process("notify-send" +: args)
    sender!
  }
  override def toString = "notify-send"
}

// Note: This class uses growlnotify which may be installed on windows
final class GrowlNotifier extends Notifier  {
  override def notify(msg: NotifyResultFormat): Unit = {
    val args = Seq(
      "/t:sbt test",
      "/silent:true",
      "/s:" + msg.sticky.toString,
      msg.title + " " + msg.message
      )
    val sender = Process("growlnotify.exe" +: args)
    sender.!
  }
  override def toString = "growlnotify"
}
