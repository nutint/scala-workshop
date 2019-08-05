package com.nat.scalaworkshop.repository

import scala.concurrent.Future

trait RepositoryError
object RepositoryError {

  final case class UserInputError(msg: String) extends RepositoryError
  final case class InternalError(msg: String) extends RepositoryError
  final case class NotFoundError(msg: String = "Not found") extends RepositoryError
}
