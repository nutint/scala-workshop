package com.nat.scalaworkshop.route

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.nat.scalaworkshop.service.ServiceError
import com.nat.scalaworkshop.service.ServiceError.{DataNotFoundError, DependencyError, InputError}
import spray.json.JsonFormat

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait StandardRouter {
  sealed trait ServiceResponse {
    def wrap: ResponseWrapper
  }

  // Success 2xx
  sealed trait SuccessResponse extends ServiceResponse
  case class OkResponse[A](data: Option[A]) extends SuccessResponse {
    override def wrap: WrappedServiceSuccessResponse[A] = WrappedServiceSuccessResponse(data)
  }
  case class CreatedResponse[A](data: Option[A]) extends SuccessResponse {
    override def wrap: WrappedServiceSuccessResponse[A] = WrappedServiceSuccessResponse(data)
  }
  case class PaginationResponse[A](data: List[A], metaData: PaginationMetadata) extends SuccessResponse {
    override def wrap: WrappedServicePaginationResponse[A] = WrappedServicePaginationResponse[A](data, metaData)
  }
  case class PaginationMetadata(page: Int, perPage: Int, pageCount: Int)

  // 4xx or 5xx
  sealed trait ServiceErrorResponse extends ServiceResponse

  // User 4xx
  sealed trait UserErrorResponse extends ServiceErrorResponse
  case object UnauthorizedResponse extends UserErrorResponse {
    override def wrap: WrappedServiceErrorResponse = WrappedServiceErrorResponse("Unauthorized")
  }
  case object NotFoundResponse extends UserErrorResponse {
    override def wrap: WrappedServiceErrorResponse = WrappedServiceErrorResponse("Not found")
  }
  case class BadRequestResponse(msg: String) extends UserErrorResponse {
    override def wrap: WrappedServiceErrorResponse = WrappedServiceErrorResponse(msg)
  }

  // Server 5xx
  sealed trait ServerErrorResponse extends ServiceErrorResponse
  case class InternalErrorResponse(msg: String) extends ServerErrorResponse {
    override def wrap: WrappedServiceErrorResponse = WrappedServiceErrorResponse(msg)
  }

  sealed trait ResponseWrapper
  case class WrappedServiceSuccessResponse[A](data: Option[A]) extends ResponseWrapper
  case class WrappedServicePaginationResponse[A](data: List[A], metadata: PaginationMetadata) extends ResponseWrapper
  case class WrappedServiceErrorResponse(error: String) extends ResponseWrapper

  object JsonFormats {
    import spray.json.DefaultJsonProtocol._
//    import spray.json._
    implicit def wrappedServiceSuccessResponse[A: JsonFormat] = jsonFormat1(WrappedServiceSuccessResponse.apply[A])
    implicit def paginationMetadata = jsonFormat3(PaginationMetadata.apply)
    implicit def wrappedServicePaginationResponse[A: JsonFormat] = jsonFormat2(WrappedServicePaginationResponse.apply[A])
    implicit def wrappedServiceErrorResponse = jsonFormat1(WrappedServiceErrorResponse)
  }


  import JsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import spray.json.DefaultJsonProtocol._
  import spray.json._
  import akka.http.scaladsl.model._

  def handleCreateResponse[A<: ServiceError, B: JsonFormat](res: Future[Either[A, B]]): Route =
    onComplete(res) {
      case Success(Left(a)) => a match {
        case InputError(msg) => complete(StatusCodes.BadRequest, BadRequestResponse(msg).wrap)
        case DependencyError(msg) => complete(StatusCodes.InternalServerError, InternalErrorResponse(msg).wrap)
        case DataNotFoundError(msg) => complete(StatusCodes.BadRequest, BadRequestResponse(msg).wrap)
      }
      case Success(Right(b)) => complete(StatusCodes.Created, CreatedResponse[B](Some(b)).wrap)
      case Failure(ex) => {
        ex.printStackTrace()
        complete(StatusCodes.InternalServerError, InternalErrorResponse(s"Unhandled Error ${ex.getMessage}").wrap)
      }
    }

  def handleNormalResponse[A<: ServiceError, B: JsonFormat](res: Future[Either[A, B]]): Route = {
    onComplete(res) {
      case Success(Left(a)) => a match {
        case InputError(msg) => complete(StatusCodes.BadRequest, BadRequestResponse(msg).wrap)
        case DependencyError(msg) => complete(StatusCodes.InternalServerError, InternalErrorResponse(msg).wrap)
        case DataNotFoundError(msg) => complete(StatusCodes.NotFound, NotFoundResponse.wrap)
      }
      case Success(Right(b)) => complete(StatusCodes.OK, OkResponse[B](Some(b)).wrap)
      case Failure(ex) => {
        ex.printStackTrace()
        complete(StatusCodes.InternalServerError, InternalErrorResponse(s"Unhandled Error ${ex.getMessage}").wrap)
      }
    }
  }

  def handleListResponse[A<: ServiceError, B: JsonFormat](res: Future[Either[A, List[B]]]): Route = {
    onComplete(res) {
      case Success(Left(a)) => a match {
        case InputError(msg) => complete(StatusCodes.BadRequest, BadRequestResponse(msg).wrap)
        case DependencyError(msg) => complete(StatusCodes.InternalServerError, InternalErrorResponse(msg).wrap)
      }
      case Success(Right(b)) => complete(StatusCodes.OK, PaginationResponse(b, PaginationMetadata(0, 0, 0)).wrap)
      case Failure(ex) => complete(StatusCodes.InternalServerError, InternalErrorResponse(ex.getMessage).wrap)
    }
  }
}
