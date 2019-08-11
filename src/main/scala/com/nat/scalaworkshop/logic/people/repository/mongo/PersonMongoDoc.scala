package com.nat.scalaworkshop.logic.people.repository.mongo

import java.util.Date

import com.nat.scalaworkshop.logic.people.model.Person
import com.nat.scalaworkshop.repository.MongoDoc
import org.bson.types.ObjectId

case class PersonMongoDoc(
  override val _id: ObjectId,
  override val data: Person,
  override val deleted: Boolean,
  override val version: String = "2",
  override val createDate: Date,
  override val updateDate: Option[Date],
  override val deleteDate: Option[Date]
) extends MongoDoc[Person]{

  def markUpdated(date: Date): MongoDoc[Person] =
    copy(
      updateDate = Some(date)
    )

  def markDeleted(date: Date): MongoDoc[Person] =
    copy(
      deleted = true,
      deleteDate = Some(date)
    )
}
