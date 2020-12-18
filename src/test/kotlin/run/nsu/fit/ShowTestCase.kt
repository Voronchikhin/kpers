package run.nsu.fit

import run.nsu.fit.core.Entity
import run.nsu.fit.core.EntityClass
import run.nsu.fit.core.Table
import run.nsu.fit.entity.Classroom
import run.nsu.fit.entity.Classrooms
import run.nsu.fit.entity.Person
import run.nsu.fit.entity.Persons

class ShowTestCase {
}

object Persons : Table() {
    val name = varchar("name", 125)
    val classroomId = integer("classId")
}


object Classrooms : Table() {
    val location = varchar("location", 155)
}

class Person(id: Int) : Entity(id) {
    companion object : EntityClass<Person>(Persons, Person::class.java)

    var name: String by Persons.name
    val classroom: Classroom by Classroom.referencedBy(Persons.classroomId)
}

class Classroom(id: Int) : Entity(id) {
    companion object : EntityClass<Classroom>(Classrooms, Classroom::class.java)

    var location by Classrooms.location
    val persons by Person.pluralRefer(Classrooms.id, Persons.classroomId)

}

