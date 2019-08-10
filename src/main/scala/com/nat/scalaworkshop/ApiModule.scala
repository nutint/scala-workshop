package com.nat.scalaworkshop

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, get, pathPrefix}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.nat.scalaworkshop.logic.healthcheck.HealthcheckRoute

import scala.concurrent.ExecutionContext

trait ApiModule
  extends HealthcheckRoute
{
  implicit def ec: ExecutionContext
  val api =
    cors () {
      healthCheckRoute ~
        pathPrefix("api" / "v1" ) {

        }
    }
}
