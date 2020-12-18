package run.nsu.fit

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import run.nsu.fit.core.Condition
import run.nsu.fit.core.Entity
import run.nsu.fit.core.EntityClass
import run.nsu.fit.core.Table

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ShowTestCase {
    @Test
    @Order(1)
    fun `show simple table creation`() {
        Persons.createTable()
        Classrooms.createTable()
    }

    @Order(2)
    @Test
    fun `show inserting data`() {
        Classrooms.insert {
            this[Classrooms.id] = 1
            this[Classrooms.location] = "Novosibirsk"
        }
        Persons.insert {
            this[Persons.id] = 1
            this[Persons.name] = "Maxim"
            this[Persons.classroomId] = 1
        }
        Persons.insert {
            this[Persons.id] = 2
            this[Persons.name] = "Anna"
            this[Persons.classroomId] = 1
        }
    }

    @Test
    @Order(3)
    fun `show selecting data`() {
        val selectRow = Persons.select {
            (Persons.classroomId eq 1) and (Persons.name eq "Maxim")
        }.first()
        println("Selected row id: ${selectRow[Persons.id]}] name: ${selectRow[Persons.name]}")
    }

    @Test
    @Order(4)
    fun `show deleting`() {
        Classrooms.insert {
            this[Classrooms.id] = 2
            this[Classrooms.location] = "Munich"
        }
        Classrooms.delete {
            Classrooms.location eq "Munich"
        }
        Assertions.assertFalse(Classrooms.select { Condition.Any }.any { it[Classrooms.location] == "Munich" })
    }


    @Test
    @Order(5)
    fun `show entity selecting`() {
        val maxim = Person.findById(1) ?: error("Maxim is lost")
        assertEquals("Maxim", maxim.name)
        assertEquals("Novosibirsk", maxim.classroom.location)
    }

    @Test
    @Order(6)
    fun `show 1 2 many`() {
        val nskClassroom = Classroom.find {
            Classrooms.location eq "Novosibirsk"
        }.first()
        val personsFromNsk = nskClassroom.persons.toList()
        assertEquals(2, personsFromNsk.size)
        assertTrue(personsFromNsk.any { it.name == "Maxim" })
        assertTrue(personsFromNsk.any { it.name == "Anna" })
    }

    @Test
    @Order(7)
    fun `show update nested entities`() {
        val classroom = Classroom.all().first()
        val maxim = classroom.persons.find { it.name == "Maxim" } ?: error("Maxim is lost")
        maxim.name = "Maksim"
        //update classroom
        Classroom.flush(classroom)
        assertEquals("Maksim", Person.findById(maxim.id)?.name)
    }

    @Test
    @Order(8)
    fun `show drop tables`() {
        Persons.dropTable()
        Classrooms.dropTable()
    }
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

    //Lazy seq of Persons
    val persons by Person.pluralRefplerLazy(Classrooms.id, Persons.classroomId)
    //Non-lazy list
    //val personsEager by Person.pluralRefer(Classrooms.id, Persons.classroomId)

}

