package scala.notify

import notify.{NotificationCentreNotifier, NotifyResultFormat}
import org.scalatest._

class NotifierTest extends FeatureSpec {

//  private def s(): Keys.TaskStreams = {
//    val logger = new Logger {
//      override def trace(t: => Throwable): Unit = ()
//
//      override def log(level: Level.Value, message: => String): Unit = ()
//
//      override def success(message: => String): Unit = ()
//     }
//  }

  feature("Trigger a notification") {

    scenario("On a mac, returns a passed notification") {
      val notificationResultFormat = new NotifyResultFormat(Some("some id"), "A title", "Test failed message", sticky = true, None)
      val notification = new NotificationCentreNotifier()
      notification.notify(notificationResultFormat)

      assert(notification != null)
    }

  }
}