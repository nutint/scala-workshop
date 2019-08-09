package com.nat.scalaworkshop.basic

import org.scalatest.{FreeSpec, Matchers}

class FunctionSpec extends FreeSpec with Matchers {

  "function composition" - {

    "same type function composition" in {
      val fn1 = (x: Int) => x * x
      val fn2 = (x: Int) => x - 1
      val fn3 = (x: Int) => x + 7

      val composed1 = fn1 compose fn2 compose fn3 // composed1 = fn1(fn2(fn3(x)))
      val composed2 = fn1 andThen fn2 andThen fn3

      composed1(2) shouldBe 64
      composed2(2) shouldBe 10
    }

    "different type function composition" in {

      val intToString = (x: Int) => x.toString
      val stringToInt = (x: String) => x.toInt

      val intToInt = intToString andThen stringToInt
      val stringToString = stringToInt andThen intToString

//      val xx = stringToInt andThen stringToInt

      intToInt(1) shouldBe 1
      stringToString("1") shouldBe "1"
    }

  }

  "higher order function" in {
    case class MySet[A](fn: A=>Boolean) {
      def isMember(a: A): Boolean = fn(a)
      def union(mySet: MySet[A]) = MySet((a: A) => fn(a) || mySet.fn(a))
      def intersect(mySet: MySet[A]) = MySet((a: A) => fn(a) && mySet.fn(a))
    }

    val countNumberSet = MySet((x: Int) => x > 0)
    val evenNumberSet = MySet((x: Int) => x % 2 == 0)
    val oddNumberSet = MySet((x: Int) => x % 2 == 1)

    countNumberSet.isMember(-1) shouldBe false
    countNumberSet.isMember(0) shouldBe false
    countNumberSet.isMember(1) shouldBe true

    val countEvenNumberSet = countNumberSet intersect evenNumberSet
    countEvenNumberSet.isMember(0) shouldBe false
    countEvenNumberSet.isMember(1) shouldBe false
    countEvenNumberSet.isMember(2) shouldBe true

    val countOddNumberSet = countNumberSet intersect oddNumberSet
    countOddNumberSet.isMember(0) shouldBe false
    countOddNumberSet.isMember(1) shouldBe true
    countOddNumberSet.isMember(2) shouldBe false
  }

  "curry function/method (Partially apply parameter function/method" in {

    // methods
    def myCurryMethod(a: Int)(b: Int)(c: Int) = a + b + c
    val myCurryFunction = (a: Int) => (b: Int) => (c: Int) => a + b + c

    val plus1Method = myCurryMethod(1) _ // convert method to function
    val plus1Function = myCurryFunction(1)

    plus1Method(3)(3) shouldBe plus1Function(4)(2)
  }
}
