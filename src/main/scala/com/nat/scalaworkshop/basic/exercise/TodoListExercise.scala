package com.nat.scalaworkshop.basic.exercise

/**
  * Requirement
  *  - Don't mute the data (no var)
  *  - add/delete/update operation
  *  - All operation need to be logged
  * Hint
  *  - Use ADT
  */
class TodoListExercise {

}

object TodoListExercise {

  /**
    * You can modify this type
    * @param items
    * @param logs
    */
  case class TodoList(items: List[Any], logs: List[Any])
}
