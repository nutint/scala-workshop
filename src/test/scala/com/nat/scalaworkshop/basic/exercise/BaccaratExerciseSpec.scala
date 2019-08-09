package com.nat.scalaworkshop.basic.exercise

import org.scalatest.{FreeSpec, Matchers}

class BaccaratExerciseSpec  extends FreeSpec with Matchers {

  import BaccaratExercise._

  "versus engine should work correctly" ignore {
    val hand1 = Hand(
      Card("d", "a"),
      Card("d", "2")
    )

    val hand2 = Hand(
      Card("d", "3"),
      Card("d", "5")
    )

    hand1 vs hand2 shouldBe false
  }
}
