package com.nat.scalaworkshop.logic.people.service

import com.nat.scalaworkshop.logic.people.model.Person
import com.softwaremill.macwire._
import com.nat.scalaworkshop.logic.people.repository.PeopleRepository
import com.nat.scalaworkshop.service.ServiceHelper

import cats.data.EitherT
import scala.concurrent.ExecutionContext

trait PeopleService
  extends ServiceHelper
{

  lazy val peopleRepository: PeopleRepository = wire[PeopleRepository]
  implicit def ec: ExecutionContext

  def getPeople: ServiceRes[List[Person]] =
    EitherT(peopleRepository.getAllPeople)

  def getPersonById(id: String): ServiceRes[Person] =
    EitherT(peopleRepository.getPersonById(id))

  def addPerson(firstName: String, lastName: String): ServiceRes[String] =
    EitherT(peopleRepository.createPerson(Person(None, firstName, lastName)))

  def deletePerson(id: String): ServiceRes[Person] =
    EitherT(peopleRepository.deletePerson(id))

  def updatePerson(id: String, firstName: String, lastName: String): ServiceRes[Person] =
    EitherT(peopleRepository.updatePerson(id, Person(None, firstName, lastName)))

}
