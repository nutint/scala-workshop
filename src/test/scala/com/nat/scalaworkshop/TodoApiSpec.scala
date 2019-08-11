package com.nat.scalaworkshop

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.Future

class TodoApiSpec extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar {

  "get all" should {
    "success when repository return list" in {
      val mockedTodoRepository = mock[TodoRepository]
      when(mockedTodoRepository.getAllTodos)
        .thenReturn(Future.failed(new Exception("something went wrong")))

      val todoApi = new TodoApi(mockedTodoRepository)
      Get("/todos") ~> todoApi.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "exception occurred something went wrong"
      }
    }
  }

  "insert one" should {

  }
}
