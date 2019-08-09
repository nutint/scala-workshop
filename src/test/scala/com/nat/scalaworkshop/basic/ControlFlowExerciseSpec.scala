package com.nat.scalaworkshop.basic

import org.scalatest.{FreeSpec, Matchers}

class ControlFlowExerciseSpec extends FreeSpec with Matchers {
  val controlFlowExample = new ControlFlowExercise

  "is even should work correctly" ignore {
    controlFlowExample isPalindrome "Hello" shouldBe "No"
    controlFlowExample isPalindrome "Wow" shouldBe "Yes"
    controlFlowExample isPalindrome "Anna" shouldBe "Yes"
    controlFlowExample isPalindrome "Civic" shouldBe "Yes"
    controlFlowExample isPalindrome "Kayak" shouldBe "Yes"
    controlFlowExample isPalindrome "Level" shouldBe "Yes"
    controlFlowExample isPalindrome "Clock" shouldBe "No"

  }

  "conversion from int to date should work correctly" ignore {
    controlFlowExample fromWeekdayStringToInt "MoNDay" shouldBe 1
    controlFlowExample fromWeekdayStringToInt "Tuesday" shouldBe 2
    controlFlowExample fromWeekdayStringToInt "Wednesday" shouldBe 3
    controlFlowExample fromWeekdayStringToInt "Thursday" shouldBe 4
    controlFlowExample fromWeekdayStringToInt "Friday" shouldBe 5
    controlFlowExample fromWeekdayStringToInt "Saturday" shouldBe 6
    controlFlowExample fromWeekdayStringToInt "Sunday" shouldBe 7
    controlFlowExample fromWeekdayStringToInt "xx" shouldBe 0
    controlFlowExample fromWeekdayStringToInt "77" shouldBe 0
    controlFlowExample fromWeekdayStringToInt "asdfasdf" shouldBe 0
  }
}
