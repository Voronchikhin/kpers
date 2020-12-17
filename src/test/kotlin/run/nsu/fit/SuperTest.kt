package run.nsu.fit

import com.mysql.cj.jdbc.Driver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import run.nsu.fit.core.*
import run.nsu.fit.generator.QueryGenerator
import run.nsu.fit.generator.backend.SqlBackend

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SuperTest {
    companion object {
        private val driver = Driver()
        val backend = SqlBackend(driver, "jdbc:mysql://localhost:3306/test", "root", "password")
        private val queryGenerator =
            QueryGenerator(backend)
    }

    @BeforeAll
    fun init() {
        try {
            Persons.createTable()
            Classrooms.createTable()
        } finally {
            val person = Row()
            person[Persons.id] = 1
            person[Persons.name] = "Maxim"
            person[Persons.classroomId] = 1

            val room = Row()
            room[Classrooms.id] = 1
            room[Classrooms.location] = "Novosibirsk"
            backend.insert(Persons, person)
            backend.insert(Classrooms, room)
        }
    }

    @Test
    fun gettingClassrooms() {
        val classRooms = Classroom.all().toList()
        assertTrue(classRooms[0].location == "Novosibirsk")
    }

    fun a() {
        val find: Sequence<Person> = Person.find(Condition.Equal(Persons.classroomId, Classrooms.id))
    }

    @Test
    fun gettingUser() {
        val person = Person.all().first()
        assertEquals("Maxim", person.name)
        assertEquals("Novosibirsk", person.classroom.location)
        Person.flush(person)
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
    var classroom: Classroom by Classroom.referencedBy(Persons.classroomId)
}

class Classroom(id: Int) : Entity(id) {
    companion object : EntityClass<Classroom>(Classrooms, Classroom::class.java)

    var location by Classrooms.location
}