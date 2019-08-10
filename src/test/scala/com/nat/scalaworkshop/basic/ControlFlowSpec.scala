package com.nat.scalaworkshop.basic

import org.scalatest.{FreeSpec, Matchers}

class ControlFlowSpec extends FreeSpec with Matchers {

  val controlFlow = new ControlFlow

   "is even should work correctly" in {
     controlFlow isEven 2 shouldBe true
     controlFlow isEven 4 shouldBe true
     controlFlow isEven 6 shouldBe true
     controlFlow isEven 8 shouldBe true
     controlFlow isEven 10 shouldBe true
     controlFlow isEven 3 shouldBe false
     controlFlow isEven 5 shouldBe false
     controlFlow isEven 7 shouldBe false
     controlFlow isEven 9 shouldBe false
     controlFlow isEven 11 shouldBe false
   }

  "conversion from int to date should work correctly" in {
    controlFlow intToDate 1 shouldBe "Monday"
    controlFlow intToDate 2 shouldBe "Tuesday"
    controlFlow intToDate 3 shouldBe "Wednesday"
    controlFlow intToDate 4 shouldBe "Thursday"
    controlFlow intToDate 5 shouldBe "Friday"
    controlFlow intToDate 6 shouldBe "Saturday"
    controlFlow intToDate 7 shouldBe "Sunday"
    controlFlow intToDate 8 shouldBe ""
    controlFlow intToDate 89 shouldBe ""
    controlFlow intToDate 9999 shouldBe ""
  }

  "printPlus1TransformedInt should work correctly" in {
    controlFlow.printPlus1TransformedInt(1, x => s"output is $x") shouldBe "output is 2"
    controlFlow.printPlus1TransformedInt(2, x => s"output is $x") shouldBe "output is 3"
    controlFlow.printPlus1TransformedInt(3, x => s"output is $x") shouldBe "output is 4"
  }
}
