package com.nat.scalaworkshop.config

case class AppConfig(
  buildConfig: BuildConfig,
  serverConfig: HttpServerConfig,
  mongoConfig: MongoConfig
)
