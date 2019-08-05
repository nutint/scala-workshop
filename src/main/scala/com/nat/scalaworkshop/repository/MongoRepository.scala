package com.nat.scalaworkshop.repository


import java.util.Date

import com.nat.scalaworkshop.config.MongoConfig
import org.bson.codecs.configuration.CodecRegistry
import org.bson.types.ObjectId
import org.mongodb.scala.{MongoClient, MongoCollection}
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonRegularExpression}
import org.mongodb.scala.model.Filters.equal
import org.bson.codecs.configuration.CodecRegistries.{fromCodecs, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag
import scala.util.Try

/**
  * This class is new base class for Scala Mongo DB Driver
  * @param ct
  * @param executionContext
  * @tparam A
  * @tparam B
  */
abstract class MongoRepository[A<:MongoDoc[B], B](
  collectionName: String
) (
  implicit ct: ClassTag[A],
  client: MongoClient,
  config: MongoConfig,
  executionContext: ExecutionContext
) {

  /**
    * Override this to specified needed collection
    * @return
    */
  lazy val collection: MongoCollection[A] =
    client
      .getDatabase(config.database)
      .withCodecRegistry(summarizedCodedRegistry)
      .getCollection[A](collectionName)

  /**
    * This method is for encapsulate data in to versioned mongo document
    * @param data
    * @return
    */
  def envelop(data: B): A

  /**
    * This method is helper for creating deleted version of document
    * This is because MongoDoc is a trait, not case class
    * @param doc
    * @return
    */
  def markDeleted(doc: A): A

  /**
    * This method is helper for creating updated version of document
    * This is because MongoDoc is a trait, not case class
    * @param doc
    * @return
    */
  def markUpdate(doc: A): A

  /**
    * To be able to search, this method ust specified searchable fields
    * @return
    */
  def searchAbleFields: List[String] = Nil

  /**
    * This for encapsulation searching in MongoDoc
    */
  lazy val calculatedSearchAbleFields = searchAbleFields.map(field => s"data.$field")

  /**
    * Codec registry for encode and decode document to Mongo Database
    * @return
    */
  def codecRegistry: CodecRegistry

  def summarizedCodedRegistry: CodecRegistry =
    fromRegistries(
      fromCodecs(
        new codec.BigDecimalScalaCodec(),
        new codec.JavaDateCodec()
      ),
      codecRegistry,
      DEFAULT_CODEC_REGISTRY
    )

  def toObjId(id: String): Try[ObjectId] =
    Try(new ObjectId(id))

  def getById(id: String): Future[Option[B]] =
    toObjId(id)
      .map { objId =>
        collection
          .find(equal("_id", objId))
          .toFuture()
          .map(_.headOption.map(_.data))
      }
      .getOrElse(Future.successful(None))

  def insert(data: B): Future[B] = {
    val envelopedData = envelop(data)
    collection
      .insertOne(envelopedData)
      .toFuture()
      .map(_=>envelopedData.data)
  }


  def update(id: String, data: B): Future[Option[B]] =
    toObjId(id)
      .map { objId =>
        collection
          .findOneAndReplace(equal("_id", objId), markUpdate(envelop(data)))
          .toFuture()
          .map { res =>
            if (res == null) None
            else Some(data)
          }
      }
      .getOrElse(Future.successful(None))

  def delete(id: String, softDelete: Boolean = true): Future[Option[B]] =
    toObjId(id)
      .map(safeId => safeDelete(safeId, softDelete))
      .getOrElse(Future.successful(None))

  def safeDelete(id: ObjectId, softDelete: Boolean = true): Future[Option[B]] = {
    val query = equal("_id", id)
    if (!softDelete) {
      collection.findOneAndDelete(query)
        .toFuture()
        .map {
          case null => None
          case deletingItem: A => Some(deletingItem.data)
        }
    } else {
      collection
        .find(query)
        .flatMap { matchedItem: A =>
          collection
            .findOneAndReplace(query, markDeleted(matchedItem))
        }
        .toFuture()
        .map(_.headOption.map(_.data))
    }
  }

  def getAll(offset: Int = 0, limit: Int = 20, deleted: Boolean = false): Future[List[B]] =
    search("", offset, limit, deleted)

  def search(queryString: String = "", offset: Int = 0, limit: Int = 20, deleted: Boolean = false): Future[List[B]] = {
    val queryList: List[BsonDocument] = queryString.trim match {
      case "" =>
        List(
          BsonDocument("_id" -> BsonDocument("$exists" -> true))
        )
      case qs => {
        val bsonRegexData = BsonRegularExpression(qs, "i")
        calculatedSearchAbleFields
          .map( f => BsonDocument(f -> bsonRegexData))
      }
    }

    val q = BsonDocument(
      "$or" -> BsonArray(queryList),
      "$and" -> BsonArray(BsonDocument("deleted" -> false))
    )
    collection
      .find(q)
      .skip(offset)
      .limit(limit)
      .toFuture()
      .map(_.toList.map(_.data))
  }

  def getLast: Future[Option[B]] =
    collection
      .find()
      .sort(BsonDocument("$natural" -> -1))
      .limit(1)
      .toFuture()
      .map(_.toList.headOption.map(_.data))

  def findByField[FieldType](field: String, value: FieldType): Future[List[B]] = {
    collection
      .find(equal(s"data.$field", value))
      .toFuture()
      .map(_.map(_.data).toList)
  }

  def newObjectId(id: Option[String] = None): ObjectId =
    id
      .map(s=>new ObjectId(s))
      .getOrElse(new ObjectId())

  def currentDate = new Date()
}