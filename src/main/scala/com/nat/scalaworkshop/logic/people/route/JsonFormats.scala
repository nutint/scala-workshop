package com.nat.scalaworkshop.logic.people.route

import com.nat.scalaworkshop.logic.people.model.Person

object JsonFormats {

  import spray.json.DefaultJsonProtocol._
  import spray.json._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  implicit val personFormat: JsonFormat[Person] = jsonFormat3(Person)
}
