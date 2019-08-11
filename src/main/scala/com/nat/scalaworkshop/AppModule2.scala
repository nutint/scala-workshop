package com.nat.scalaworkshop

import java.util.UUID

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

  val todoService: TodoService = new TodoService

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
          complete(todoService.getAllTodos.toString)
        } ~
        post {
          entity(as[String]) { itemName =>
            complete(todoService.addTodo(itemName).toString)
          }
        }
      } ~
      pathPrefix(Segment) { id: String =>
        pathEnd {
          get {
            complete(todoService.getById(id).toString)
          } ~
          put {
            entity(as[String]) { itemName =>
              complete(todoService.updateById(id, itemName).toString)
            }
          } ~
          delete {
            complete(todoService.deleteById(id).toString)
          }
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

class TodoService {
  var todos: List[Todo] = Nil

  def addTodo(title: String): Todo = {
    val newTodo = Todo(UUID.randomUUID().toString, title, false)
    todos = newTodo :: todos
    newTodo
  }

  def getAllTodos: List[Todo] = todos

  def getById(id: String): Option[Todo] = todos.find(_.id == id)

  def deleteById(id: String): Unit = {
    todos = todos.filter(_.id != id)
  }

  def updateById(id: String, title: String): Option[Todo] = {
    todos
      .find(_.id == id)
      .map { foundTodo =>
        val newTodo = foundTodo.copy(title = title)
        todos = newTodo :: todos.filter(_.id != id)
        newTodo
      }
  }
}

case class Todo(id: String, title: String, done: Boolean)
