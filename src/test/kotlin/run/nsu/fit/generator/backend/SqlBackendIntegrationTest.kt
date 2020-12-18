package run.nsu.fit.generator.backend

import com.mysql.cj.jdbc.Driver
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import run.nsu.fit.Classroom
import run.nsu.fit.Classrooms
import run.nsu.fit.Persons
import run.nsu.fit.core.*


internal class SqlBackendIntegrationTest {

    private lateinit var sqlBackend: SqlBackend

    @BeforeEach
    fun init() {
        val driver = Driver()
        sqlBackend = SqlBackend(driver, "jdbc:mysql://localhost:3306/test", "root", "password")
    }

    @AfterEach
    fun shutDown() {
        sqlBackend.close()
    }

    @Test
    fun createTable() {
        sqlBackend.createTable("Table100") {
            column("id", ColumnType.INT)
            column("name", ColumnType.VARCHAR(255))
        }

        sqlBackend.createTable("Table200") {
            column("id", ColumnType.INT)
            column("name200", ColumnType.VARCHAR(255))
        }
    }

    @Test
    fun insert() {

        val row = Row()
        row[Table100.id] = 3
        row[Table100.name] = "name3"
        sqlBackend.insert(Table100, row)
    }

    @Test
    fun select() {
        val toList1 = sqlBackend.select(Table100, Condition.Any).toList()
        val toList = toList1[0]
        assertEquals("name", toList[Table100.name])
        assertEquals(1, toList[Table100.id])
    }

    @Test
    fun selectToObject() {
//        sqlBackend.createTable("Table100"){
//            column("id", ColumnType.INT)
//            column("name", ColumnType.VARCHAR(255))
//        }
//
//        sqlBackend.createTable("Table200"){
//            column("id", ColumnType.INT)
//            column("name200", ColumnType.VARCHAR(255))
//        }

        val row = Row()
        row[Table100.id] = 3
        row[Table100.name] = "name100"
        sqlBackend.insert(Table100, row)

        val row200 = Row()
        row200[Table200.id] = 3
        row200[Table200.name200] = "name200"
        sqlBackend.insert(Table200, row)

        val a =
            Classrooms
                .join(Persons)
                .join(Table100)
                .select { Condition.Any }
                .wrap(Table300Entity::class.java)
                .first()
        assertEquals("Anna", a.name)

    }

    @Test
    fun delete() {
        val condition = Condition.Const(Table100.id, 1)
        sqlBackend.delete(Table100, condition)
    }

    @Test
    fun transitiveUpdate(){
        val classroom = Classrooms.select { Condition.Any }.wrap(Classroom::class.java).first()
        val first = classroom.persons.first()
        first.name = "yyyyyyyyyyyyyyyyyyyyyyyyy"
        Classroom.flush(classroom)
    }
}


object Table100 : Table() {
    val name = varchar("name", 255)
}

object Table200 : Table() {
    val name200 = varchar("name200", 255)
}


class Table300Entity(id: Int) : Entity(id) {
    var name by Table100.name
    var name200 by Table200.name200
}


