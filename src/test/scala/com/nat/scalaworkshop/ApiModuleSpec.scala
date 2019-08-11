package com.nat.scalaworkshop

import akka.http.scaladsl.model.HttpEntity
import com.nat.scalaworkshop.logic.people.repository.PeopleRepository
import com.nat.scalaworkshop.repository.RepositoryError.{InternalError, NotFoundError}
import com.nat.scalaworkshop.repository.RepositoryHelper.RepoResultF
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.prop.TableDrivenPropertyChecks._
import akka.http.scaladsl.model.ContentTypes._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
class ApiModuleSpec
  extends WordSpec
    with Matchers
    with ScalatestRouteTest
    with MockitoSugar
{
  import ApiModuleSpec._

  "Listing all people" should {
    "failed when repository connect failed" in {
      val mockedPeopleRepo = mock[PeopleRepository]
      when(mockedPeopleRepo.getAllPeople)
        .thenReturn(repoInternalError("connection failed"))
      val testingPeopleRoute = mockApiModule(
        peopleRepo = mockedPeopleRepo
      )

      Get("/api/v1/people") ~> testingPeopleRoute.api ~> check {
        status shouldBe StatusCodes.InternalServerError
      }
    }

    "success when repository returns list of people" in {
      val mockedPeopleRepo = mock[PeopleRepository]
      when(mockedPeopleRepo.getAllPeople)
        .thenReturn(Future.successful(Right(Nil)))
      val testingRoute = mockApiModule(peopleRepo = mockedPeopleRepo)
      Get("/api/v1/people") ~> testingRoute.api ~> check {
        status shouldBe StatusCodes.OK
      }
    }

  }
}

object ApiModuleSpec {

  def mockApiModule(
    peopleRepo: PeopleRepository
  ): ApiModule = new ApiModule {
    override implicit def ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
    override lazy val peopleRepository = peopleRepo
  }

  def repoInternalError[A](msg: String="any internal error"): RepoResultF[A] = Future.successful(Left(InternalError(msg)))
  def repoNotFoundError[A](msg: String="not found"): RepoResultF[A] = Future.successful(Left(NotFoundError(msg)))
  def repoSuccessResult[A](data: A): RepoResultF[A] = Future.successful(Right(data))
}