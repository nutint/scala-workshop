package com.nat.scalaworkshop.basic

import org.scalatest.{FreeSpec, Matchers}

class FunctionAndValueSpec extends FreeSpec with Matchers {

  "Initializing variable, functions, methods, and classes" - {

    "defining val" in {
      // Full notation
      val a: String = "a string"
      // With type inference
      val b = "a string"

      a.shouldBe(b) // Full
      a shouldBe b // Short (More human language)

      val c: Int = 1
      val d = 1
      c shouldBe d
    }

    "defining functions" in {
      // Function with single parameters
      val f1: Int => String = (a: Int) => a.toString
      val f2 = (a: Int) => a.toString // Use type inference
      val f3: Int => String = _.toString // single parameter can be shorthanded by using _
      // val function4 = _.toString // compile error because the compiler cannot infer type of the parameters

      f1(1).shouldBe(f2(1))
      f1(1) shouldBe f2(1)
      f1(1) shouldBe f3(1)

      // Function with multiple parameters
      val f11: (Int, Int) => String = (a: Int, b: Int) => (a+b).toString
      val f22 = (a: Int, b: Int) => (a+b).toString
      val f33 = (a: Int, b: Int) => (a+b) toString

      f11(99, 100) shouldBe f22(99, 100)
      f11(0, 190) shouldBe f22(0, 190)
      f11(99, 100) shouldBe f33(99, 100)
    }

    "defining methods in scala" in {

      def m1(a: Int): String = a.toString // Full
      def m2(a: Int) = a.toString // Use type inference
      def m3(a: Int) = a toString // Use type inference and omit dot(.)

      m1(1) shouldBe m2(1)
      m1(1) shouldBe m3(1)
    }

    "defining class in scala" in {
      class C1() { println("C1 created") }
      class C2(param: Int) { println(s"C2 created with parameter $param")}
      class C3[A](param: A) { println(s"C3 created with parameter $param")}
    }

    "defining class with different types of members" in {
      class MyClass[A](param1: String, param2: A) {
        def getParam2Type: String = param2 match {
          case _: String => "String"
          case _: Int => "Int"
          case _: Double => "Double"
          case _: BigDecimal => "BigDecimal"
          case _: Boolean => "Boolean"
//          case _: List[Int] => "List of Int"
//          case _: List[String] => "List of String"
          case _ => "Unknown type"
        }

        override def toString: String = s"param1($param1) with param2($param2, ${getParam2Type})"
      }

      val c1 = new MyClass("c1", "abcd")
      val c2 = new MyClass("c2", 12)
      val c3 = new MyClass("c3", true)
      val c4 = new MyClass("c4", 0.5)
      val c5 = new MyClass("c5", 'a')

      val c6 = new MyClass("c6", List(12, 13))
      val c7 = new MyClass("c7", List("123", "456"))

      c1.toString shouldBe "param1(c1) with param2(abcd, String)"
      c2.toString shouldBe "param1(c2) with param2(12, Int)"
      c3.toString shouldBe "param1(c3) with param2(true, Boolean)"
      c4.toString shouldBe "param1(c4) with param2(0.5, Double)"
      c5.toString shouldBe "param1(c5) with param2(a, Unknown type)"

      c6.toString shouldBe "param1(c6) with param2(List(12, 13), Unknown type)"
      c7.toString shouldBe "param1(c7) with param2(List(123, 456), Unknown type)"
    }
  }
}
