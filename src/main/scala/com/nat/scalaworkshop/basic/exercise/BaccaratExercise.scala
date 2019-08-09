package com.nat.scalaworkshop.basic.exercise

class BaccaratExercise {
  import BaccaratExercise._

}

/**
  * Companion Object Purposes
  *  - alternative ways to create class's instance
  *  - Related types
  */
object BaccaratExercise {

  case class Card(suit: Suit, rank: Rank)
  object Card {
    def apply(s: String, r: String): Card = Card(Suit(s), Rank(r))
  }

  trait Suit
  case object Diamond extends Suit
  case object Clover extends Suit
  case object Heart extends Suit
  case object Spade extends Suit
  object Suit {
    def apply(s: String): Suit = s.toLowerCase match {
      case "d" => Diamond
      case "c" => Clover
      case "h" => Heart
      case "s" => Spade
    }
  }

  trait Rank
  case object Ace extends Rank
  case class Numeric(value: Int) extends Rank
  case object Jack extends Rank
  case object Queen extends Rank
  case object King extends Rank
  object Rank {
    def apply(s: String): Rank = s.toLowerCase match {
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

  /**
    * Rules (https://starvegasgame.com/วิธีเล่นไพ่ป๊อกเด้ง/)
    */
  case class Hand(card1: Card, card2: Card, card3: Option[Card] = None) {
    def vs(anotherHand: Hand): CompareResult = ???

    def handPattern(): HandPattern = ???
  }

  trait CompareResult
  case object Win extends CompareResult
  case object Lose extends CompareResult
  case object Draw extends CompareResult

  trait HandPattern
  case class RankDominated(score: Int) extends HandPattern
  case class TwoOfAKindDominated(score: Int) extends HandPattern
  case class ThreeOfAKindDominated(score: Int) extends HandPattern
  case class StraightDominated(rank: Rank) extends HandPattern
  case object RoyalCardDominated extends HandPattern
  case object StraightFlushDominated extends HandPattern
  case object RoyalStraightFlushDominated extends HandPattern
}