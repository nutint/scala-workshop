package com.nat.scalaworkshop.basic

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import scala.util.{Success, Failure}

class FutureExample {

  val delayedComputation = (computation: Int => Int, fakeDelayValue: Int) => (x: Int) => {
    Thread.sleep(fakeDelayValue)
    computation(x)
  }

  val power = (x: Int) => x * x

  val powerWithout10 = (x: Int) =>
    if(x == 10) throw new Exception("unable to compute 10")
    else x * x

  def getDuration(startTime: Long): Long =
    System.currentTimeMillis() - startTime


  def demo: Unit = timedExecuting(5000, dependenciesBetweenFutures _)

  def timedExecuting(waitTime: Int, fn: Long => Unit): Unit = {
    val t0 = System.currentTimeMillis()
    println(Console.RED + "Start executing")
    fn(t0)
    println(Console.BLUE + s"Total time usage = ${getDuration(t0)} millis" + Console.RESET)
    println(Console.BLUE + s"Wait time = $waitTime millis" + Console.RESET)
    Thread.sleep(waitTime)
  }

  def sequentialExecuting(startTime: Long): Unit = {
    val result = delayedComputation(power, 1000)(5)
    println(s"result from very long computation is $result")
    val result2 = delayedComputation(power, 2000)(7)
    println(s"result from very long computation is $result2")
  }

  def parallelExecuting(startTime: Long): Unit = {
    val result = Future(delayedComputation(power, 1000)(5))
    val result2 = Future(delayedComputation(power, 2000)(7))
    result.onComplete {
      case Success(x) => println(s"result1 = $x (${getDuration(startTime)} ms)")
      case Failure(ex) => println(s"result1 failed by $ex (${getDuration(startTime)} ms)")
    }
    result2.onComplete {
      case Success(x) => println(s"result2 = $x (${getDuration(startTime)} ms)")
      case Failure(ex) => println(s"result2 failed by $ex (${getDuration(startTime)} ms)")
    }
  }

  def alotOfParallelExecution(startTime: Long): Unit = {
    val futureFn = (x: Int) => Future(delayedComputation(power, 500)(x))
    val futureResult: List[Future[Int]] = (1 to 10)
      .map(futureFn)
      .toList
    Future.sequence(futureResult)
      .map(_.sum)
      .foreach(total => {
        println(s"Total summary result of power 1 to 10 is $total (${getDuration(startTime)} ms)")
      })
  }

  def dependenciesBetweenFutures(startTime: Long): Unit = {
    val result: Future[Int] = for {
      result1 <- Future(delayedComputation(power, 1000)(5))
      result2 <- Future(delayedComputation(powerWithout10, 1000)(10))
      result3 <- Future(delayedComputation(power, 1000)(result2 - result1))
    } yield result1 + result2 + result3

    result.onComplete {
      case Success(x) => println(s"result= $x (${getDuration(startTime)} ms)")
      case Failure(ex) => println(s"result failed by $ex (${getDuration(startTime)} ms)")
    }
  }


}
