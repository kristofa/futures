import com.twitter.util.{FuturePool, Future}

/**
 * Implements sum and multiplication operations
 * and returns results as futures.
 *
 * The operations sleep for a while before returning response and
 * add some logging. This is done to allow spotting execution order and
 * sequential or parallel execution.
 */
object SumAndMultiplicationOps {

  private val sleepTimeMs = 50

  def sum(a: Int, b: Int, logPrefix: String = ""): Future[Int] = {
    FuturePool.unboundedPool {
      println(s"[$logPrefix] starting sum $a + $b")
      Thread.sleep(sleepTimeMs)
      println(s"[$logPrefix] returning $a + $b")
      a + b
    }
  }

  def multiply(a: Int, b: Int, logPrefix: String = ""): Future[Int] = {
    FuturePool.unboundedPool {
      println(s"[$logPrefix] starting multiplication $a * $b")
      Thread.sleep(sleepTimeMs)
      println(s"[$logPrefix] returning $a * $b")
      a * b
    }
  }

  def failingSum(a: Int, b: Int, logPrefix: String = ""): Future[Int] = {
    FuturePool.unboundedPool {
      println(s"[$logPrefix] starting failingSum $a + $b")
      Thread.sleep(sleepTimeMs)
      println(s"[$logPrefix] throwing exception!")
      throw new IllegalStateException(s"[$logPrefix] Exception when calculating sum: $a + $b")
    }
  }

}
