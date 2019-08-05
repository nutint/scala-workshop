package com.nat.scalaworkshop.service

trait ServiceError
object ServiceError {
  case class InputError(msg: String) extends ServiceError
  case class DependencyError(msg: String) extends ServiceError
  case class DataNotFoundError(msg: String) extends ServiceError
}
