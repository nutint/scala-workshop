package com.nat.scalaworkshop.config.utils

import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.{AsyncFreeSpec, FreeSpec, Matchers}
import org.scalatest.mockito.MockitoSugar

import scala.util.{Success, Try}

object MyConfigExtractorImplicit {

  implicit object MyAwesomeConfigExtractor extends ConfigExtractor[MyAwesomeConfig] {
    override def extract(config: Config): MyAwesomeConfig =
      (Try(config.getString("name")), Try(config.getInt("score"))) match {
        case (Success(name), Success(score)) => MyAwesomeConfig(name, score)
        case _ => throw new IllegalArgumentException("Unable to load Awesome config (MyAwesomeConfig)")
      }
  }
}

case class MyAwesomeConfig(name: String, score: Int)


class ConfigExtractorSpect extends FreeSpec with Matchers with MockitoSugar {

  "Extracting config" - {

    "should extract correctly in root config" in {

      val beingExtractedConfig: Config = ConfigFactory.parseString(
        """
          |name=johnSmith
          |score=100
        """.stripMargin)

      import ConfigExtractor._

      implicit object MyAwesomeConfigExtractor extends ConfigExtractor[MyAwesomeConfig] {
        override def extract(config: Config): MyAwesomeConfig =
          (Try(config.getString("name")), Try(config.getInt("score"))) match {
            case (Success(name), Success(score)) => MyAwesomeConfig(name, score)
            case _ => throw new IllegalArgumentException("Unable to load Awesome config (MyAwesomeConfig)")
          }
      }

      val res = extractConfig[MyAwesomeConfig](beingExtractedConfig)
      assert(res == MyAwesomeConfig("johnSmith", 100))
    }

    "should extract correctly when specify path" in {

      val beingExtractedConfig: Config = ConfigFactory.parseString(
        """
          |root {
          |  name=johnSmith
          |  score=100
          |}
        """.stripMargin)

      import ConfigExtractor._

      implicit object MyAwesomeConfigExtractor extends ConfigExtractor[MyAwesomeConfig] {
        override def extract(config: Config): MyAwesomeConfig =
          (Try(config.getString("name")), Try(config.getInt("score"))) match {
            case (Success(name), Success(score)) => MyAwesomeConfig(name, score)
            case _ => throw new IllegalArgumentException("Unable to load Awesome config (MyAwesomeConfig)")
          }
      }

      val res = extractConfig[MyAwesomeConfig](beingExtractedConfig, "root")
      assert(res == MyAwesomeConfig("johnSmith", 100))

    }

  }

}
