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
import java.lang.Exception

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
        }catch (e: Exception){} finally {
            val person = Row()
            person[Persons.id] = 1
            person[Persons.name] = "Maxim"
            person[Persons.classroomId] = 1

            val person1 = Row()
            person1[Persons.id] = 1
            person1[Persons.name] = "Anya"
            person1[Persons.classroomId] = 1

            val room = Row()
            room[Classrooms.id] = 1
            room[Classrooms.location] = "Novosibirsk"
            backend.insert(Persons, person)
            backend.insert(Persons, person1)
            backend.insert(Classrooms, room)
        }
    }

    @Test
    fun gettingClassrooms() {
        val classRooms = Classroom.allLazy().toList()
        assertTrue(classRooms[0].location == "Novosibirsk")
    }

    fun a() {
        val find: Sequence<Person> = Person.findLazy(Condition.Equal(Persons.classroomId, Classrooms.id))
    }

    @Test
    fun gettingUser() {
        val person = Person.allLazy().first()
        assertEquals("Maxim", person.name)
        assertEquals("Novosibirsk", person.classroom.location)
        person.name = "Anna"
        Person.flush(person)
    }

    @Test
    fun getUsersUyClassroom(){

        val classroom = Classroom.allLazy().first()
        assertEquals(2, classroom.persons.toList().size)

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
    val persons by Person.referrersOn(Classrooms.id, Persons.classroomId)

}
