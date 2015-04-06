package notify

import sbt.{SuiteResult, TestEvent, TestResult, TestsListener}

/**
 * Encapsulates the information about the test results for a group
 * @param name   name of test group
 * @param status final TestResult.Value for a test groups
 */
case class GroupResult(name: String, status: TestResult.Value)

/**
 * Encapsulates the information about all the test results
 * @param status   final result of the tests
 * @param count    total number of tests in a run
 * @param failures number of tests that failed
 * @param errors   number of tests that had errors
 * @param passed   number of tests that passed
 * @param skipped  number of tests that were skipped
 */
case class AggregateResult(status: TestResult.Value, count: Int, passed: Int, failures: Int, errors: Int, skipped: Int)

/**
 * The default format of the notification
 * @param id        id
 * @param title     title of message
 * @param message   message to be displayed
 * @param sticky    whether the message to be kept visible
 * @param imagePath image to be displayed
 */
case class NotifyResultFormat(id: Option[String], title: String, message: String, sticky: Boolean, imagePath: Option[String])

class NotifyingTestsListener(groupFormatter: GroupResult => NotifyResultFormat,
                             exceptionFormatter:(String, Throwable) => NotifyResultFormat,
                             aggregateFormatter: AggregateResult => NotifyResultFormat,
                             notifier: Notifier,
                             log: sbt.Logger) extends TestsListener {

  private var skipped, errors, passed, failures = 0

  /**
   * Called once at the beginning
   */
  def doInit(): Unit = {
    passed   = 0
    failures = 0
    errors   = 0
    skipped  = 0
  }

  /**
   * Called for each class or equivalent grouping
   */
  def startGroup(name: String) = {}

  /**
   * Called for each test event or equivalent
   */
  def testEvent(event: TestEvent) = {
    val result = SuiteResult(event.detail)
    passed   += result.passedCount
    failures += result.failureCount
    errors   += result.errorCount
    skipped  += result.skippedCount
  }

  /**
   * Called when test group is finished
   */
  def endGroup(name: String, result: TestResult.Value) = notifier.notify(groupFormatter(GroupResult(name, result)))

  /**
   * Called when all the tests are complete
   */
  def doComplete(status: TestResult.Value) = {
    val all = passed + failures + errors + skipped
    notifier.notify(aggregateFormatter(AggregateResult(status, all, passed, failures, errors, skipped)))
  }

  /**
   * Called if there is an error during the test
   */
  def endGroup(name: String, t: Throwable) = notifier.notify(exceptionFormatter(name, t))

}
