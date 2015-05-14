package notify

import sbt._
import Keys._

object NotifyingTests extends sbt.Plugin {

  import NotifyKeys._

  object NotifyKeys {
    val images             = SettingKey[NotifyDefaultImages]("images", "Object defining paths of test images used for notification.")
    val exceptionFormatter = SettingKey[(String, Throwable) => NotifyResultFormat]("exception-formatter", "Function used to format test exception.")
    val groupFormatter     = SettingKey[(GroupResult => NotifyResultFormat)]("group-formatter", "Function used to format a test group result.")
    val aggregateFormatter = SettingKey[(AggregateResult => NotifyResultFormat)]("aggregate-formatter", "Function used to format an aggregation of test results.")
    val defaultImagePath   = SettingKey[File]("default-image-path", "Default path used to resolve test images.")
    val notifier           = SettingKey[Notifier]("notifier", "Interface used to notify test results to users.")
  }

  val Notify = config("notify") extend Test

  override lazy val projectSettings = notifySettings

  private def notifyingTestListenerTask: Def.Initialize[sbt.Task[sbt.TestReportListener]] =
    (groupFormatter in Notify, exceptionFormatter in Notify, aggregateFormatter in Notify, notifier in Notify, streams) map {
      (resultFormatter, exceptionFormatter, aggregateFormatter, notifier, out) =>
        new NotifyingTestsListener(resultFormatter, exceptionFormatter, aggregateFormatter, notifier, out.log)
    }

  val notifySettings: Seq[Setting[_]] = inConfig(Notify)(Seq(
    images <<= defaultImagePath apply { path =>
      def setIfExists(name: String) = {
        val file = path / name
        if(file.exists) Some(file.getAbsolutePath) else None
      }
      NotifyDefaultImages(setIfExists("pass.png"), setIfExists("fail.png"), setIfExists("error.png"))
    },
    exceptionFormatter := { (name: String, t: Throwable) =>
      NotifyResultFormat(
        Some("%s Exception" format name),
        "Exception in Test: %s" format name,
        t.getMessage, sticky=true, None
      )
    },
    notifier := Notifier(),
    groupFormatter <<= images {
      (imgs) =>
        (res: GroupResult) =>
          NotifyResultFormat(
            Some(res.name),
            res.name,
            res.status match {
              case TestResult.Error  => "Had Errors"
              case TestResult.Passed => "Passed"
              case TestResult.Failed => "Failed"
            },
            res.status match {
              case TestResult.Error | TestResult.Failed => true
              case _ => false
            },
            res.status match {
              case TestResult.Error  => imgs.errorIcon
              case TestResult.Passed => imgs.passIcon
              case TestResult.Failed => imgs.failIcon
            }
          )
    },
    aggregateFormatter <<= images {
      (imgs) =>
        (res: AggregateResult) =>
          NotifyResultFormat(
            Some("All Tests"),
            res.status match {
              case TestResult.Error  => "Oops"
              case TestResult.Passed => "Nice job"
              case TestResult.Failed => "Try harder"
            },
            "%d Tests \n- %d Failed\n- %d Errors\n- %d Passed\n- %d Skipped" format(
              res.count, res.failures, res.errors, res.passed, res.skipped
            ),
            res.status match {
              case TestResult.Error | TestResult.Failed => true
              case _ => false
            },
            res.status match {
              case TestResult.Error  => imgs.errorIcon
              case TestResult.Passed => imgs.passIcon
              case TestResult.Failed => imgs.failIcon
            }
          )
    },
    defaultImagePath := file(System.getProperty("user.home")) / ".sbt" / "plugins" / "sbt-notify" / "icons"
  )) ++ Seq(
    testListeners <+= notifyingTestListenerTask
  )
}