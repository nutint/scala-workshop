package com.nat.scalaworkshop.basic.exercise

import org.scalatest.{FreeSpec, Matchers}

/**
  *
  */
class TodoListExerciseSpec extends FreeSpec with Matchers {

  "adding item" - {
    "should have new element" in pending
    "should have new log item" in pending
  }

  "removing item" - {
    "when the item is not exists" - {
      "should do nothing in todo list" in pending
      "should log error" in pending
    }

    "when item is exists" - {
      "should remove the specified item" in pending
      "should log remove success" in pending
    }
  }

  "editing item" - {
    "when the item is not exists" - {
      "should do nothing in todo list" in pending
      "should log error" in pending
    }

    "when item is exists" - {
      "should update the specified item" in pending
      "should log update success" in pending
    }
  }

}
