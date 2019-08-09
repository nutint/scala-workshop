package com.nat.scalaworkshop.basic.exercise

class BaccaratExercise {
  import BaccaratExercise._

  /**
    * Rules (https://starvegasgame.com/วิธีเล่นไพ่ป๊อกเด้ง/)
    * @param hand1
    * @param hand2
    * @return
    */
  def isHand1Win(hand1: Hand, hand2: Hand): CompareResult = hand1 vs hand2

}

/**
  * Companion Object Purposes
  *  - alternative ways to create class's instance
  *  - Related types
  */
object BaccaratExercise {
  trait Card {
    def rank: Rank = ???
  }
  case class Diamond(override val rank: Rank) extends Card
  case class Clover(override val rank: Rank) extends Card
  case class Heart(override val rank: Rank) extends Card
  case class Spade(override val rank: Rank) extends Card
  object Card {
    def apply(s: String, r: Rank): Card = s.toLowerCase match {
      case "d" => Diamond(r)
      case "c" => Clover(r)
      case "h" => Heart(r)
      case "s" => Spade(r)
    }
  }

  trait Rank
  case object Ace extends Rank
  case class Numeric(value: Int) extends Rank
  case object Jack extends Rank
  case object Queen extends Rank
  case object King extends Rank
  object Rank {
    implicit def stringToRank(s: String): Rank = s.toLowerCase match {
      case "a" => Ace
      case "1" => Ace
      case "2" => Numeric(2)
      case "3" => Numeric(3)
      case "4" => Numeric(4)
      case "5" => Numeric(5)
      case "6" => Numeric(6)
      case "7" => Numeric(7)
      case "8" => Numeric(8)
      case "9" => Numeric(9)
      case "10" => Numeric(10)
      case "j" => Jack
      case "q" => Queen
      case "k" => King
    }
  }

  case class Hand(card1: Card, card2: Card, card3: Option[Card] = None) {
    def vs(anotherHand: Hand): CompareResult = ???
    def calculateMultiplication(): Int = ???
  }

  trait CompareResult
  case object Win extends CompareResult
  case object Lose extends CompareResult
}