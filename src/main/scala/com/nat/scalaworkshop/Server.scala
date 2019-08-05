package com.nat.scalaworkshop

// Standard Libraries
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.nat.scalaworkshop.config._
import com.typesafe.config.ConfigFactory

import scala.util.Try


class Server
  extends App
{
  import Extractors._
  import config.utils.ConfigExtractor._

  val conf = ConfigFactory.load()
  implicit val system = ActorSystem("actorSystem")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  Try(extractConfig[AppConfig](conf))
    .fold[Unit](
      ex => {
        println(s"Failed loading configuration ${ex.getMessage}")
        ex.printStackTrace()
        system.terminate()
      },
      appConfig => {
        val appModule = new AppModule(appConfig)
        appModule.startService()
    })
}


object ServerApp extends Server