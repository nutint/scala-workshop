package com.nat.scalaworkshop.basic

import java.util.Date

import org.scalatest.{FreeSpec, Matchers}

class ADTsAndTraitsSpecs extends FreeSpec with Matchers {

  case class Person(name: String)

  trait RelationshipStatus

  case object Single extends RelationshipStatus {
    def marryWith(person: Person) = Married(person)
    def hasRelationshipWith(person: Person) = InRelationship(person)
  }
  case class InRelationship(partner: Person) extends RelationshipStatus {
    def breakUp = Single
    def marryWith(partner: Person) = Married(partner)
  }
  case class Married(partner: Person) extends RelationshipStatus {
    def divorce = Divorced
    def partnerDead = Widowed
  }
  case object Widowed extends RelationshipStatus {
    def marryWith(partner: Person) = Married(partner)
  }
  case object Divorced extends RelationshipStatus {
    def marryWith(partner: Person) = Married(partner)
  }

  "It's very complicated relationship" in {

    Single
      .hasRelationshipWith(Person("Natalie"))
      .breakUp
      .hasRelationshipWith(Person("Natalie"))
      .marryWith(Person("Natalie"))
      .divorce
      .marryWith(Person("Jane")) shouldBe  Married(Person("Jane"))
  }
}
