package com.themakerheros.loki

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

/**
  * This route spect is credited by https://www.codemunity.io/tutorials/akka-http-routing-primer/
  * Thanks for the author for helping us understand how to write routes using akka-http
  */
class RouteSpec extends WordSpec with Matchers with ScalatestRouteTest {

  "A Router" should {
    "list all tutorial" in {

      Get("/tutorials") ~> Router.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "all tutorials"
      }
    }

    "return a single tutorial by id" in {
      Get("/tutorials/hello-world") ~> Router.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "tutorial hello-world"
      }
    }

    "list all comments of a given tutorial" in {
      Get("/tutorials/hello-world/comments") ~> Router.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "comments for the hello-world tutorial"
      }
    }

    "add comment to a tutorial" in {
      Post("/tutorials/hello-world/comments", "new comment") ~> Router.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "added the comment 'new comment' to the hello-world tutorial"
      }
    }
  }

  "A Router - Contacts" should {

    "list all contacts" in {
      Get("/contacts") ~> Router.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "all contacts"
      }
    }

    "return a single contact by id" in {
      Get("/contacts/asdfasdf") ~> Router.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "contact asdfasdf"
      }
    }
  }

  "Version 1 Router" should {

    "be able to get all tutorials" in {
      Get("/api/v1/tutorials") ~> Router.v1Route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "all tutorials"
      }
    }

    "be able to get all contacts" in {
      Get("/api/v1/contacts") ~> Router.v1Route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "all contacts"
      }
    }
  }

}

object Router {
  def route: Route = pathPrefix("tutorials") {
    pathEnd {
      get {
        complete("all tutorials")
      }
    } ~ pathPrefix(Segment) { id =>
      pathEnd {
        get {
          complete(s"tutorial $id")
        }
      } ~
      path("comments") {
        get {
          complete(s"comments for the $id tutorial")
        } ~
        post {
          entity(as[String]) { comment =>
            complete(s"added the comment '$comment' to the $id tutorial")
          }
        }
      }
    }
  } ~
  pathPrefix("contacts") {
    pathEnd {
      get {
        complete("all contacts")
      }
    } ~ pathPrefix(Segment) { id =>
      pathEnd {
        get {
          complete(s"contact $id")
        }
      }
    }
  }

  def v1Route: Route = pathPrefix("api"/"v1" ) {
    route
  }
}
