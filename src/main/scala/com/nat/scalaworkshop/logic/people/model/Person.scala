package com.nat.scalaworkshop.logic.people.model

case class Person(id: Option[String], firstName: String, lastName: String) {
  def withId(newId: String) = copy(id = Some(newId))
}
