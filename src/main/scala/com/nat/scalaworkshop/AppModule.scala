package com.nat.scalaworkshop

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.nat.scalaworkshop.basic.FutureExample
import com.nat.scalaworkshop.config._
import com.nat.scalaworkshop.utils.DateFactory
import org.mongodb.scala.MongoClient

import scala.io.StdIn

class AppModule(
  appConfig: AppConfig
) (
  implicit system: ActorSystem,
  materializer: ActorMaterializer
) extends ApiModule {

  implicit val mongoConfig = appConfig.mongoConfig
  override implicit val ec = system.dispatcher

  implicit val mongoClient = MongoClient(mongoConfig.uri)
  implicit val dateFactory = new DateFactory

  def startService(): Unit = {
    val bindingFuture = Http().bindAndHandle(api, appConfig.serverConfig.host, appConfig.serverConfig.port)
    appConfig.buildConfig match {
      case BuildConfigDevelopment =>
        println(s"Running in development mode @port ${appConfig.serverConfig.port}")
        StdIn.readLine()
        bindingFuture
          .flatMap(_.unbind())
          .recover {
            case ex =>
              println("Unable to start service")
              ex.printStackTrace()
          }
          .onComplete(_ => system.terminate())
      case BuildConfigProduction =>
        println(s"Running in production mode @port ${appConfig.serverConfig.port}")
    }
  }

}