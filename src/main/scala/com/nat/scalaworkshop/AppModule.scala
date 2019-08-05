package com.nat.scalaworkshop

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.nat.scalaworkshop.config._

class AppModule(
  appConfig: AppConfig
) (
  implicit system: ActorSystem,
  materializer: ActorMaterializer
) {

  def startService(): Unit = {
    appConfig.buildConfig match {
      case BuildConfigDevelopment =>
        println(s"Running in development mode")
      case BuildConfigProduction =>
        println(s"Running in production mode")
    }
  }

}