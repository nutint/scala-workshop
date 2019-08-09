package com.nat.scalaworkshop.basic

import org.scalatest.{FreeSpec, Matchers}

/**
  * Documentation - https://www.scala-lang.org/api/2.12.3/scala/collection/immutable/List.html
  *  - Focus on basic usage of collections especially on list
  *  - How to use the documentation
  */
class ImmutableCollectionsSpec extends FreeSpec with Matchers{

  val ic = new ImmutableCollections

  "List" - {
    val fruits = List("apple", "mango", "banana", "melon")

    "different types of list initiation should be the same" in {
      val fruits: List[String] = List("apple", "mango", "durian", "mangosteen")
      val fruits2 = List("apple", "mango", "durian", "mangosteen")
      val fruits3 = "apple" :: "mango" :: "durian" :: "mangosteen" :: Nil

      fruits == fruits2 && fruits2 == fruits3 shouldBe true
    }

    "list concat" in {
      val fruit1 = "apple" :: Nil
      val fruit2 = "mango" :: Nil
      val totalFruits = fruit1 ::: fruit2

      totalFruits shouldBe "apple" :: "mango" :: Nil
    }

    "List element transformation" in {
      fruits.map(_.length) shouldBe List(5, 5, 6, 5)
      fruits.map(_.toUpperCase) shouldBe List("APPLE", "MANGO", "BANANA", "MELON")

      // More Detailed in the function declaration section
      val getStringLength: String => Int = (strInput: String) => strInput.length
      val getStringLength2 = (strInput: String) => strInput.length
      fruits.map(getStringLength) shouldBe List(5, 5, 6, 5)
    }

    "List reduce, fold, sum" in {
      fruits
        .map(_.length)
        .reduce(_ + _) shouldBe 21

      fruits
        .map(_.length)
        .sum shouldBe 21

      fruits
        .map(_.charAt(0))
        .foldLeft("")((a, b) => s"$a$b") shouldBe "ambm"

      fruits
        .map(_.charAt(0))
        .foldRight("")((a, b) => s"$b$a") shouldBe "mbma"
    }

    "filter and find" in {
      fruits
        .filter(_.charAt(0) == 'a') shouldBe List("apple")

      fruits
        .find(_.charAt(0) == 'm') shouldBe Some("mango")
    }

    "group by" in {
      fruits
        .groupBy(_.charAt(0)) shouldBe
          Map(
            'a' -> List("apple"),
            'm' -> List("mango", "melon"),
            'b' -> List("banana"))
    }

    "with pattern matching" in {
      val getSecondElement = (aList: List[String]) => aList match {
        case _ :: b :: _ => Some(b)
        case _ => None
      }

      getSecondElement("a" :: "b" :: Nil) shouldBe Some("b")
      getSecondElement("a" :: Nil) shouldBe None
      getSecondElement(Nil) shouldBe None
    }
  }
}
