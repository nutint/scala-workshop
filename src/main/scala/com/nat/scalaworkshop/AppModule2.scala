package com.nat.scalaworkshop

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.nat.scalaworkshop.config.{AppConfig, BuildConfigDevelopment, BuildConfigProduction}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext
import scala.io.StdIn

class AppModule2 (
  appConfig: AppConfig
) (
  implicit system: ActorSystem,
  materializer: ActorMaterializer
) {

  implicit val ec: ExecutionContext = system.dispatcher

  /**
    * Features
    *  - Add new item        [Post]   /todos
    *  - Get all             [GET]    /todos
    *    - pending/done      [PUT]    /todos/{id}/status
    *  - Edit current item   [PUT]    /todos/{id}
    *  - remove              [DELETE] /todos/{id}
    *  - Get by ID           [GET]    /todos/{id}
    */
  val route: Route =
    pathPrefix("todos") {
      pathEnd {
        get {
          complete("all tutorials")
        }
      }
    }


  def startService(): Unit = {
    val bindingFuture = Http().bindAndHandle(route, appConfig.serverConfig.host, appConfig.serverConfig.port)
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
