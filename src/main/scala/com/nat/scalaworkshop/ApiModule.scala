package com.nat.scalaworkshop

import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.nat.scalaworkshop.logic.healthcheck.HealthcheckRoute
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.nat.scalaworkshop.logic.people.route.PeopleRoute

import scala.concurrent.ExecutionContext

trait ApiModule
  extends HealthcheckRoute
  with PeopleRoute
{
  implicit def ec: ExecutionContext
  val api: Route =
    cors () {
      healthCheckRoute ~
      pathPrefix("api" / "v1" ) {
        peopleV1
      }
    }
}
