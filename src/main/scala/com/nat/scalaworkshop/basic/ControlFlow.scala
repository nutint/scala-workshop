package com.nat.scalaworkshop.basic


/**
  * This session demonstrate on the following topics
  *  - Control Flow (If-Else, Pattern Matching)
  *    - [If-Else](https://www.tutorialspoint.com/scala/scala_if_el)
  *    - [Pattern Matching](https://www.tutorialspoint.com/scala/scala_pattern_matching.htm)
  *  - Method Declaration (Basic)
  *    - Return statement can be omitted
  *    - Method with one parameter can omit parenthesis
  *  - Function Declaration
  *  - Class Declaration (Basic)
  *  -
  */
class ControlFlow {

  /**
    *
    * @param a
    * @return true or false if the input value is even
    */
  def isEven(a: Int): Boolean = if (a % 2 == 0) true else false

  /**
    * Transform from integer number to weekdays, if not valid input will return empty string
    * 1 as Monday
    * 2 as Tuesday
    *  ....
    * 7 as Sunday
    * others as ""
    * @param a
    * @return
    */
  def intToDate(a: Int): String = a match {
    case 1 => "Monday"
    case 2 => "Tuesday"
    case 3 => "Wednesday"
    case 4 => "Thursday"
    case 5 => "Friday"
    case 6 => "Saturday"
    case 7 => "Sunday"
    case _ => ""
  }


  /**
    * f(x, g) => g(x+1)
    * @param a
    * @param transformFn
    */
  def printPlus1TransformedInt(a: Int, transformFn: Int => String): String = transformFn(a+1)
}
