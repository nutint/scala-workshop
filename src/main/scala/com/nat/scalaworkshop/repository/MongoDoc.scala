package com.nat.scalaworkshop.repository

import java.util.Date
import org.bson.types.ObjectId

trait MongoDoc[A] {
  def _id: ObjectId
  def data: A
  def deleted: Boolean
  def version: String
  def createDate: Date
  def updateDate: Option[Date]
  def deleteDate: Option[Date]

  def markUpdated(date: Date): MongoDoc[A]
  def markDeleted(date: Date): MongoDoc[A]
}
