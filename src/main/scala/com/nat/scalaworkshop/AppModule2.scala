package com.nat.scalaworkshop

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.nat.scalaworkshop.config.{AppConfig, BuildConfigDevelopment, BuildConfigProduction}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import scala.concurrent.ExecutionContext
import scala.io.StdIn

class AppModule2 (
  appConfig: AppConfig
) (
  implicit system: ActorSystem,
  materializer: ActorMaterializer
) extends  SprayJsonSupport with DefaultJsonProtocol {

  implicit val ec: ExecutionContext = system.dispatcher

  val todoService: TodoService = new TodoService

  implicit val todoItemFormat = jsonFormat3(Todo)

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
          complete(todoService.getAllTodos)
        } ~
        post {
          entity(as[String]) { itemName =>
            complete(todoService.addTodo(itemName))
          }
        }
      } ~
      pathPrefix(Segment) { id: String =>
        pathEnd {
          get {
            complete(todoService.getById(id))
          } ~
          put {
            entity(as[String]) { itemName =>
              complete(todoService.updateById(id, itemName))
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

class TodoService(repository: TodoRepository) {
  def addTodo(title: String): Todo = repository.addTodo(title)

  def getAllTodos: List[Todo] = repository.getAllTodos

  def getById(id: String): Option[Todo] = repository.getById(id)

  def deleteById(id: String): Unit = repository.deleteById(id)

  def updateById(id: String, title: String): Option[Todo] = repository.updateById(id, title)
}

case class Todo(id: String, title: String, done: Boolean)

trait TodoRepository {
  def addTodo(title: String): Todo = ???

  def getAllTodos: List[Todo] = ???

  def getById(id: String): Option[Todo] = ???

  def deleteById(id: String): Unit = ???

  def updateById(id: String, title: String): Option[Todo] = ???
}

class TodoInMemRepository extends TodoRepository {
  var todos: List[Todo] = Nil

  override def addTodo(title: String): Todo = {
    val newTodo = Todo(UUID.randomUUID().toString, title, false)
    todos = newTodo :: todos
    newTodo
  }

  override def getAllTodos: List[Todo] = todos

  override def getById(id: String): Option[Todo] = todos.find(_.id == id)

  override def deleteById(id: String): Unit = {
    todos = todos.filter(_.id != id)
  }

  override def updateById(id: String, title: String): Option[Todo] = {
    todos
      .find(_.id == id)
      .map { foundTodo =>
        val newTodo = foundTodo.copy(title = title)
        todos = newTodo :: todos.filter(_.id != id)
        newTodo
      }
  }
}
