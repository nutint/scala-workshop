package com.nat.scalaworkshop.basic

import org.scalatest.{FreeSpec, Matchers}

class FunctionSpec extends FreeSpec with Matchers {

  "function composition" - {

    "same type function composition" in {
      val fn1 = (x: Int) => x * x
      val fn2 = (x: Int) => x - 1
      val fn3 = (x: Int) => x + 7

      val composed1 = fn1 compose fn2 compose fn3 // composed1 = fn1(fn2(fn3(x)))
      val composed2 = fn1 andThen fn2 andThen fn3

      composed1(2) shouldBe 64
      composed2(2) shouldBe 10
    }

    "different type function composition" in {

      val intToString = (x: Int) => x.toString
      val stringToInt = (x: String) => x.toInt

      val intToInt = intToString andThen stringToInt
      val stringToString = stringToInt andThen intToString

//      val xx = stringToInt andThen stringToInt

      intToInt(1) shouldBe 1
      stringToString("1") shouldBe "1"
    }

  }
}
