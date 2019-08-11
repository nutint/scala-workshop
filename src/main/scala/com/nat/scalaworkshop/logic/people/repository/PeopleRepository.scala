package com.nat.scalaworkshop.logic.people.repository

import com.nat.scalaworkshop.logic.people.model.Person
import com.nat.scalaworkshop.repository.RepositoryHelper.RepoResultF

trait PeopleRepository {
  def getAllPeople: RepoResultF[List[Person]]
  def getPersonById(id: String): RepoResultF[Person]
  def createPerson(person: Person): RepoResultF[String]
  def updatePerson(id: String, person: Person): RepoResultF[Person]
  def deletePerson(id: String): RepoResultF[Person]
}

object PeopleRepository {
  def apply(): PeopleRepository = new PeopleRepository {
    override def getAllPeople: RepoResultF[List[Person]] = ???

    override def getPersonById(id: String): RepoResultF[Person] = ???

    override def createPerson(person: Person): RepoResultF[String] = ???

    override def updatePerson(id: String, person: Person): RepoResultF[Person] = ???

    override def deletePerson(id: String): RepoResultF[Person] = ???
  }
}