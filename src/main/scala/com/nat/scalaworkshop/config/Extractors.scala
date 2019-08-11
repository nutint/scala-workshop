package com.nat.scalaworkshop.config

import com.nat.scalaworkshop.config.utils.ConfigExtractor
import com.typesafe.config.Config

import scala.util.{Success, Try}

object Extractors {

  import ConfigExtractor._

  implicit object BuildConfigExtractor extends ConfigExtractor[BuildConfig] {
    override def extract(config: Config): BuildConfig = {
      Try(config.getString("build").toLowerCase) match {
        case Success("dev") => BuildConfigDevelopment
        case Success("prod") => BuildConfigProduction
        case Success(v) => throw new IllegalArgumentException(s"Invalid build configuration value: $v")
        case _ => throw new IllegalArgumentException("Unable to load config: build")
      }
    }
  }

  implicit object MongoConfigExtractor extends ConfigExtractor[MongoConfig] {
    override def extract(config: Config): MongoConfig =
      (Try(config.getString("uri")), Try(config.getString("database"))) match {
        case (Success(uri), Success(db)) => MongoConfig(uri, db)
        case _ => throw new IllegalArgumentException("Unable to load config: mongodb")
      }
  }

  implicit object HttpServerConfigExtractor extends ConfigExtractor[HttpServerConfig] {
    override def extract(config: Config): HttpServerConfig =
      (Try(config.getString("host")), Try(config.getInt("port"))) match {
        case (Success(host), Success(port)) => HttpServerConfig(host, port)
        case _ => throw new IllegalArgumentException("Unable to load config: http-server")
      }
  }

  implicit object AppConfigExtractor extends ConfigExtractor[AppConfig] {
    override def extract(config: Config): AppConfig = AppConfig(
      extractConfig[BuildConfig](config),
      extractConfig[HttpServerConfig](config, "http-server"),
      extractConfig[MongoConfig](config, "mongodb")
    )
  }

}
