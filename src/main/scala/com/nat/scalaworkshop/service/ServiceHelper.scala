package com.nat.scalaworkshop.service

import cats.data.EitherT
import com.nat.scalaworkshop.repository.RepositoryError
import com.nat.scalaworkshop.repository.RepositoryError.{InternalError, NotFoundError, UserInputError}
import com.nat.scalaworkshop.service.ServiceError.{DataNotFoundError, DependencyError}

import scala.concurrent.{ExecutionContext, Future}

trait ServiceHelper {

  import cats.implicits._
  implicit def ec: ExecutionContext

  type ServiceRes[A] = Future[Either[ServiceError, A]]

  implicit def buildServiceResult[A](res: EitherT[Future, RepositoryError, A]): ServiceRes[A] =
    res
      .leftMap{
        case NotFoundError(msg) => DataNotFoundError(msg)
        case InternalError(msg) => DependencyError(msg)
        case UserInputError(msg) => DependencyError(msg)
      }
      .value
}
