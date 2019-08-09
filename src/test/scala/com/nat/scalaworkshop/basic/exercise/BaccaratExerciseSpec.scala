package com.nat.scalaworkshop.basic.exercise

import org.scalatest.{FreeSpec, Matchers}

class BaccaratExerciseSpec  extends FreeSpec with Matchers {

  import BaccaratExercise._

  "[da, d2] against [d3, d5] should correct" ignore {
    val hand1 = Hand(
      Card("d", "a"),
      Card("d", "2")
    )

    val hand2 = Hand(
      Card("d", "3"),
      Card("d", "5")
    )

    hand1 vs hand2 shouldBe Lose
  }

  "[d2, s2] against [da, s3] should correct" ignore {
    val hand1 = Hand(
      Card("d", "2"),
      Card("S", "2")
    )

    val hand2 = Hand(
      Card("d", "a"),
      Card("s", "3")
    )

    hand1 vs hand2 shouldBe Win
  }

  "feel free to add any other tests at this point" in pending
}
