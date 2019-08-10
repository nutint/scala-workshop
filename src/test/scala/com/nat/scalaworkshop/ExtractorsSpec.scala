package com.nat.scalaworkshop.config

import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.prop.TableDrivenPropertyChecks._

import scala.util.{Failure, Success, Try}


class ExtractorsSpec extends FreeSpec with Matchers with MockitoSugar {

  import utils.ConfigExtractor._
  import Extractors._

  "AppConfig Extractors" - {

    "should extract correctly" in {

      val beingExtractedConfig: Config = ConfigFactory.parseString(
        """
          |build=dev
          |
          |http-server {
          |  host = "0.0.0.0"
          |  port = 8000
          |}
          |
          |mongodb {
          |  uri = "mongodb://root:password@localhost:27017/admin"
          |  database = "mydb"
          |}
          |
        """.stripMargin)


      assert(
        extractConfig[AppConfig](beingExtractedConfig) ==
        AppConfig(
          BuildConfigDevelopment,
          MongoConfig(
            "mongodb://root:password@localhost:27017/admin",
            "mydb"
          )
        )
      )

    }

  }

  "Build Config Extractor" - {

    "should success when specify valid build value" in {

      val testTable =
        Table(
          ("build value", "parsed"),
          ("dev", BuildConfigDevelopment),
          ("prod", BuildConfigProduction)
        )

      forAll(testTable) { (buildValue: String, parsed: BuildConfig) =>
        val cfg: Config = ConfigFactory.parseString(s"build=$buildValue")
        assert(extractConfig[BuildConfig](cfg) == parsed)
      }
    }

    "should fail when specify invalid value in build" in {

      val testTable =
        Table(
          "build value",
          "development",
          "develop",
          "produ",
          "production"
        )

      forAll(testTable) { buildValue: String =>
        val cfg: Config = ConfigFactory.parseString(s"build=$buildValue")
        Try(extractConfig[BuildConfig](cfg)) match {
          case Failure(ex) => assert(ex.getMessage == s"Invalid build configuration value: $buildValue")
          case _ => assert(false)
        }
      }
    }

    "should fail when unable to file build key" in {

      val testTable =
        Table(
          ("build key", "build value"),
          ("bb", "dev"),
          ("cc", "dev"),
          ("dd", "dev")
        )

      forAll(testTable) { (buildKey: String, buildValue: String) =>
        val cfg: Config = ConfigFactory.parseString(s"$buildKey=$buildValue")
        Try(extractConfig[BuildConfig](cfg)) match {
          case Failure(ex) => assert(ex.getMessage == "Unable to load config: build")
          case _ => assert(false)
        }
      }
    }
  }

  "MongoConfigExtractor" - {

    "should be able to load valid mongo config" in {
      forAll(
        Table(
          ("uri", "database"),
          ("anyUri", "anyDatabase"),
          ("xxx", "yyy")
        )
      ) {
        (uri: String, database: String) =>
          val cfg: Config = ConfigFactory.parseString(
            s"""
               |uri: $uri
               |database: $database
             """.stripMargin
          )

          assert(extractConfig[MongoConfig](cfg) == MongoConfig(uri, database))
      }
    }

    "should fail when provide invalid key for mongo uri" in {
      forAll(
        Table(
          ("uriKey", "databaseKey"),
          ("urixx", "databasexx"),
          ("u", "database"),
          ("uri", "db")
        )
      ) {
        (uriKey: String, databaseKey: String) =>
          val cfg: Config = ConfigFactory.parseString(
            s"""
               |$uriKey: validuri
               |$databaseKey: dbval
             """.stripMargin
          )

          Try(extractConfig[MongoConfig](cfg)) match {
            case Failure(ex) => assert(ex.getMessage == "Unable to load config: mongodb")
            case _ => assert(false)
          }
      }
    }
  }

}
