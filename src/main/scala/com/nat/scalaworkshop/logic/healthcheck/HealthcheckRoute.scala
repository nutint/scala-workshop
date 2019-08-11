package com.nat.scalaworkshop.logic.healthcheck

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

trait HealthcheckRoute
{

  val healthCheckRoute =
    path ("ping") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Pong</h1>"))
      }
    } ~
      path ("healthcheck") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Healthcheck</h1>"))
        }
      }
}