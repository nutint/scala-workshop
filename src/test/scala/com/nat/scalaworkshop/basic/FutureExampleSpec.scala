package com.nat.scalaworkshop.basic

import org.scalatest.{AsyncFreeSpec, Matchers}

import scala.concurrent.Future

class FutureExampleSpec extends AsyncFreeSpec with Matchers {
  "using with very long computation" in {

    val veryLongComputation = (x: Int) => {
      Thread.sleep(1000)
      x * x
    }

    Future(veryLongComputation(5)) map {
      case 25 => assert(true)
      case _ => assert(false)
    }

    Future(veryLongComputation(2))
      .map(veryLongComputation)
      .map(veryLongComputation)
      .map { _ shouldBe 256 }
  }
}
