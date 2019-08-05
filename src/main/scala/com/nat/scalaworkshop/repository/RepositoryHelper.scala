package com.nat.scalaworkshop.repository

import com.nat.scalaworkshop.repository.RepositoryError.{InternalError, NotFoundError}

import scala.concurrent.{ExecutionContext, Future}

object RepositoryHelper {

  type RepoResult[A] = Either[RepositoryError, A]
  type RepoResultF[A] = Future[RepoResult[A]]

  def recoverFailed[A]: PartialFunction[Throwable, Future[Either[RepositoryError, A]]] = {
    case ex: Throwable => Future.successful(Left(InternalError(ex.getMessage)))
  }

  def optionToEither[A](v: Option[A]): RepoResult[A] =
    Either.cond(v.isDefined, v.get, NotFoundError())

  implicit def futureOptionToRepoResult[A](res: Future[Option[A]])(implicit ec: ExecutionContext): RepoResultF[A] =
    res.map(optionToEither).recoverWith(recoverFailed)

  implicit def futureToRepoResult[A](res: Future[A])(implicit ec: ExecutionContext): RepoResultF[A] =
    res.map(Right(_)).recoverWith(recoverFailed)
}
