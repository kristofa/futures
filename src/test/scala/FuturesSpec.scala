import com.twitter.util.{Future, Await}
import org.specs2.mutable._
import SumAndMultiplicationOps._

/**
 * Shows the usages of the Twitter Futures api using SumAndMultiplicationOps.
 *
 * All the tests execute 2 sums and a multiplication of the results of both sums but the
 * execution happens using different ways. Some implementations are more readable then others.
 * For some the sums run in parallel, others run sequential.
 *
 * There are also some tests that show how to deal with exceptions.
 */
class FuturesSpec extends Specification {

  // Executes sequentially to make logging of tests useful
  sequential

  """
     Executes using for comprehension.
     The logging shows that all 3 operations are executed sequentially.
     for comprehension = sequential composition.
  """ in {
    val result =
      for (
        sumResult1 <- sum(1, 2, "forComp");
        sumResult2 <- sum(3, 4, "forComp");
        multiplication <- multiply(sumResult1, sumResult2, "forComp")
      ) yield multiplication
    Await.result(result) ==== 21
  }


  """
    Executes using flatMap combinator.
    This gives the same result as the for comprehension, but I find the for
    comprehension more readable.
  """ in {
    val result =
      sum(1, 2, "flatMap").flatMap(
            sum1 =>
              sum(3, 4, "flatMap").flatMap(
                 sum2 => multiply(sum1, sum2, "flatMap")
                )
          )
    Await.result(result) ==== 21
  }

  """
     Executes using collect combinator = concurrent composition.
     If you look at the log messages you will see that sums are
     executed in parallel in this case.
  """ in {
    val result =
      Future.collect(Seq(sum(1, 2, "collect"), sum(3, 4, "collect")))
        .flatMap(
          seq => multiply(seq(0), seq(1), "collect")
        )
    Await.result(result) ==== 21
  }

  """
     Executes using join combinator = concurrent composition.
     If you look at the log messages you will see that sums are
     executed in parallel in this case.
  """ in {
    Await.result(
      Future.join(sum(1, 2, "join"), sum(3, 4, "join"))
        .flatMap {
          case (resultSum1, resultSum2)
            => multiply(resultSum1, resultSum2, "join")
      }
    ) ==== 21
  }

  """
    The 2nd sum fails with an exception.
    As a result multiplication won't be executed and exception is thrown when getting
    result of Future.
  """ in {
    val result = Future.join(sum(1, 2, "exception"), failingSum(3, 4, "exception"))
      .flatMap {
        case (resultSum1, resultSum2) => multiply(resultSum1, resultSum2, "join")
      }
    Await.result(result) must throwA[IllegalStateException]
  }

  """
    The 2nd sum fails with an exception but we 'rescue' the exception. In this case
    we re-execute the operation using for comprehensions which should not throw an exception.
    In real code rescuing the exception could be used to for example implement a retry
    in case of a timeout. rescue is the equivalent of flatMap but operates over exceptions
    instead of values.
  """ in {
    val result = Future.join(sum(1, 2, "rescue-tobeRescued"), failingSum(3, 4, "rescue-tobeRescued"))
      .flatMap {
      case (resultSum1, resultSum2) => multiply(resultSum1, resultSum2, "rescue-notExecuted")
    }.rescue {
      case e: IllegalStateException =>
        for (
          sumResult1 <- sum(1, 2, "rescue-rescue");
          sumResult2 <- sum(3, 4, "rescue-rescue");
          multiplication <- multiply(sumResult1, sumResult2, "rescue-rescue")
        ) yield multiplication
    }
    Await.result(result) ==== 21
  }

  """
     Shows usage of ensure. Ensure is executed both in case of succes or failure.
     This shows the succes case.
  """ in {
    var ensureExecuted = false
    val result = Future.join(sum(1, 2, "join_ensure"), sum(3, 4, "join_ensure"))
      .flatMap {
        case (resultSum1, resultSum2)
          => multiply(resultSum1, resultSum2, "join_ensure")
      }.ensure {
        println("[join_ensure] ensure executed")
        ensureExecuted = true
      }
    Await.result(result) ==== 21
    ensureExecuted ==== true
  }

}
