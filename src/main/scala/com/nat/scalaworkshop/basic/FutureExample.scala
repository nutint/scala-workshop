package com.nat.scalaworkshop.basic

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import scala.util.{Success, Failure}

class FutureExample {

  val delayedComputation = (computation: Int => Int, delayValue: Int) => (x: Int) => {
    Thread.sleep(delayValue)
    computation(x)
  }

  val power = (x: Int) => x * x

  def demo: Unit = timedExecuting(5000, parallelExecuting _)

  def timedExecuting(waitTime: Int, fn: () => Unit): Unit = {
    val t0 = System.currentTimeMillis()
    fn()
    val t1 = System.currentTimeMillis()
    println(Console.BLUE + s"Total time usage = ${t1 - t0} millis" + Console.RESET)
    Thread.sleep(waitTime)
  }

  def sequentialExecuting: Unit = {
    println(Console.RED + "Start executing")
    val result = delayedComputation(power, 1000)(5)
    println(s"result from very long computation is $result")
    val result2 = delayedComputation(power, 2000)(7)
    println(s"result from very long computation is $result2")
    println("Finished executing" + Console.RESET)
  }

  def parallelExecuting: Unit = {
    println(Console.RED + "Start executing")
    val result = Future(delayedComputation(power, 1000)(5))
    println(s"result from very long computation is $result")
    val result2 = Future(delayedComputation(power, 2000)(7))
    println(s"result from very long computation is $result2")
    result.onComplete {
      case Success(x) => println(s"result1 = $x")
      case Failure(ex) => println(s"result1 failed by $ex")
    }
    result2.onComplete {
      case Success(x) => println(s"result2 = $x")
      case Failure(ex) => println(s"result2 failed by $ex")
    }
    println("Finished executing" + Console.RESET)
  }


}
