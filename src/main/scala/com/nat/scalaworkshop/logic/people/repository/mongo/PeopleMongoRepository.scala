package com.nat.scalaworkshop.logic.people.repository.mongo

import com.nat.scalaworkshop.config.MongoConfig
import com.nat.scalaworkshop.logic.people.model.Person
import com.nat.scalaworkshop.logic.people.repository.PeopleRepository
import com.nat.scalaworkshop.repository.MongoRepository
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.MongoClient

import scala.concurrent.ExecutionContext

class PeopleMongoRepository(
  collectionName: String
)(
  implicit executionContext: ExecutionContext,
  client: MongoClient,
  config: MongoConfig
) extends MongoRepository[PersonMongoDoc, Person](collectionName)
  with PeopleRepository {

  import com.nat.scalaworkshop.repository.RepositoryHelper._

  override def envelop(data: Person): PersonMongoDoc = {
    val objId = newObjectId(data.id)
    PersonMongoDoc(
      _id = objId,
      data = data.withId(objId.toString),
      createDate = currentDate,
      updateDate = None,
      deleteDate = None,
      deleted = false
    )
  }

  override def markDeleted(doc: PersonMongoDoc): PersonMongoDoc =
    if(doc.deleted) doc
    else doc.copy(deleted = true, deleteDate = Some(currentDate))

  override def markUpdate(doc: PersonMongoDoc): PersonMongoDoc =
    doc.copy(updateDate = Some(currentDate))

  override def searchAbleFields: List[String] =
    List("id", "firstName", "lastName")

  override def codecRegistry: CodecRegistry =
    fromRegistries(
      fromProviders(
        classOf[Person],
        classOf[PersonMongoDoc]
      )
    )

  override def getAllPeople: RepoResultF[List[Person]] =
    getAll()

  override def getPersonById(id: String): RepoResultF[Person] =
    getById(id)

  override def createPerson(jobInfo: Person): RepoResultF[String] =
    insert(jobInfo)
      .map(ji => Right(ji.id.get))

  override def updatePerson(id: String, jobInfo: Person): RepoResultF[Person] =
    update(id, jobInfo)

  override def deletePerson(id: String): RepoResultF[Person] =
    delete(id)

}
