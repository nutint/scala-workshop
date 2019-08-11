package com.nat.scalaworkshop.logic.people.route

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object PeopleRouteEntities extends DefaultJsonProtocol with SprayJsonSupport{
  case class PersonEntity(firstName: String, lastName: String)

//  import spray.json.DefaultJsonProtocol._
//  import spray.json._
//  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  implicit def createPersonEntity= jsonFormat2(PersonEntity)
}
