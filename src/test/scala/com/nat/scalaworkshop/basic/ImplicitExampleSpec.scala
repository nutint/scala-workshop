package com.nat.scalaworkshop.basic

import org.scalatest.{FreeSpec, Matchers}

class ImplicitExampleSpec extends FreeSpec with Matchers {

  "method with implicit parameter" in {
    implicit val a: Int = 1
    def m1(param1: Int)(implicit param2: Int): Int = param1 + param2

    m1(1) shouldBe 2
    m1(1)(7) shouldBe 8
  }

  "implicit conversion" in {
    implicit def stringToInt(a: String) = a.toInt
    def add(a: Int, b: Int) = a + b
    add(1, "2") shouldBe 3 // compiler will look implicit methods to converse string to int
  }

  /**
    * Implicit class is used when you want to add new feature to the specific type without modification
    * or subclassing the original class
    */
  "implicit class" in {
    case class Person(name: String) {
      def greet: String = s"hello $name"
    }

    implicit class SayGoodbye(person: Person) {
      def sayGoodbye: String = s"goodbye ${person.name}"
    }

    val mrJohn = Person("John")
    mrJohn.sayGoodbye shouldBe "goodbye John"
  }

  /**
    * This feature can be done when you want to keep the code clean from any other functionalities
    */
  "implicit typed class" in {

    // Original types
    case class Person(firstName: String, lastName: String)
    case class Dog(name: String)
    case class Cat(name: String)

    // Adding features
    trait CanGreet[A] {
      def greet(a: A): String
    }
    implicit def greet[A](a: A)(implicit ai: CanGreet[A]): String = ai.greet(a)

    // Implement behavior for those type
    implicit val personCanGreet = new CanGreet[Person] {
      override def greet(a: Person): String = s"Hello my name is ${a.firstName + " " + a.lastName}"
    }

    // Usages
    greet(Person("John", "Smith")) shouldBe "Hello my name is John Smith"
//    greet(Dog("JaiDee")) shouldBe "Woof Woof!!"
//    greet(Cat("Kitty")) shouldBe "Meaw Meaw"

    val greetedPerson: String = Person("John", "Smith")
    greetedPerson shouldBe "Hello my name is John Smith"

  }



}
