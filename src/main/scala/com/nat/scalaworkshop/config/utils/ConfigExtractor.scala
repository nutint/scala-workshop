package com.nat.scalaworkshop.config.utils

import com.typesafe.config.Config

trait ConfigExtractor[A] {
  def extract(config: Config): A
}

object ConfigExtractor {

  def extractConfig[A](config: Config)(implicit extractor: ConfigExtractor[A]): A = {
    extractor.extract(config)
  }

  def extractConfig[A](config: Config, path: String)(implicit extractor: ConfigExtractor[A]): A = {
    extractor.extract(config.getConfig(path))
  }
}