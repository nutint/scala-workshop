package com.nat.scalaworkshop.logic.people.route

object PeopleRouteEntities {
  case class CreatePersonEntity(firstName: String, lastName: String)
  case class UpdatePersonEntity(firstName: String, lastName: String)

  import spray.json.DefaultJsonProtocol._
  import spray.json._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  implicit val createPersonEntity: JsonFormat[CreatePersonEntity] = jsonFormat2(CreatePersonEntity.apply)
  implicit val updatePersonEntity: JsonFormat[UpdatePersonEntity] = jsonFormat2(UpdatePersonEntity.apply)
}
