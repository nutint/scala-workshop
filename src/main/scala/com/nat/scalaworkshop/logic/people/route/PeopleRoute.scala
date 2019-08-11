package com.nat.scalaworkshop.logic.people.route

// Standard Libraries
import scala.concurrent.ExecutionContext
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.nat.scalaworkshop.logic.people.service.PeopleService
import com.nat.scalaworkshop.route.StandardRouter

trait PeopleRoute
  extends StandardRouter
  with PeopleService
{
  import spray.json.DefaultJsonProtocol._
  import com.nat.scalaworkshop.logic.people.route.PeopleRouteEntities._
  import com.nat.scalaworkshop.logic.people.route.JsonFormats._
  implicit def ec: ExecutionContext

  val peopleV1: Route = pathPrefix("people") {
    pathEnd {
      get {
        handleNormalResponse(getPeople)
      } ~
      post {
        entity(as[CreatePersonEntity]) { createPersonEntity =>
          val CreatePersonEntity(firstName, lastName) = createPersonEntity
          handleCreateResponse(addPerson(firstName, lastName))
        }
      }
    } ~ pathPrefix( Segment ) { personId =>
      pathEnd {
        get {
          handleNormalResponse(getPersonById(personId))
        } ~
          put {
            entity(as[UpdatePersonEntity]) { updatePersonEntity =>
              val UpdatePersonEntity(first, last) = updatePersonEntity
              handleNormalResponse(updatePerson(personId, first, last))
            }
          } ~
          delete {
            handleNormalResponse(deletePerson(personId))
          }
      }
    }
  }
}
