package com.nat.scalaworkshop

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.nat.scalaworkshop.basic.FutureExample
import com.nat.scalaworkshop.config._

class AppModule(
  appConfig: AppConfig
) (
  implicit system: ActorSystem,
  materializer: ActorMaterializer
) {

  def startService(): Unit = {

    val futureExample = new FutureExample()
    futureExample.demo

    appConfig.buildConfig match {
      case BuildConfigDevelopment =>
        println(s"Running in development mode")
      case BuildConfigProduction =>
        println(s"Running in production mode")
    }
  }

}