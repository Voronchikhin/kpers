package run.nsu.fit

import com.mysql.cj.jdbc.Driver
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import run.nsu.fit.core.*
import run.nsu.fit.generator.QueryGenerator
import run.nsu.fit.generator.backend.SqlBackend

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class EntityClassTest{

    @BeforeAll
    fun init(){
        val driver = Driver()
        val sqlBackend = SqlBackend(driver, "jdbc:mysql://localhost:3306/test", "root", "password")
        val queryGenerator = QueryGenerator(sqlBackend)
        queryGenerator.createTable(Rooms)
        queryGenerator.createTable(Users)
    }
    @AfterAll
    fun shutdown(){
        val driver = Driver()
        val sqlBackend = SqlBackend(driver, "jdbc:mysql://localhost:3306/test", "root", "password")
        val queryGenerator = QueryGenerator(sqlBackend)
        //queryGenerator.dropTable(Rooms)
    }
    @Test
    fun orm(){
        val driver = Driver()
        val sqlBackend = SqlBackend(driver, "jdbc:mysql://localhost:3306/test", "root", "password")
        val row = Row()
        row[Rooms.id] = 1
        row[Rooms.name] = "name"
        sqlBackend.insert(Rooms, row)
        val user = Room.all().first()
        assertEquals(user.name, "name")
        val user1 = Room.findById(1)
        assertEquals(user1!!.name, "name")
        val condition = Condition.Const(Rooms.name, "name")
        val user2 = Room.find(condition).first()
        assertEquals(user2.name, "name")
    }

    @Test
    fun ormComposite(){
        val driver = Driver()
        val sqlBackend = SqlBackend(driver, "jdbc:mysql://localhost:3306/test", "root", "password")

        val room = Row()
        room[Rooms.id] = 10
        room[Rooms.name] = "room"

        val user = Row()
        user[Users.id] = 10
        user[Users.classId] = 10
        sqlBackend.insert(Rooms, room)
        sqlBackend.insert(Users, user)
        val toList = User.all().toList()
        assertTrue(toList.any{
             it.room.name == "room"
        })
    }
}
object Users: Table(){
    val name = varchar("name", 32)
    val classId = integer("classId")
}
object Rooms: Table() {
    val name = varchar("name", 6)
}
class Room(id: Int) : Entity(id){
    companion object : EntityClass<Room>( Rooms, Room::class.java)
    var name by Rooms.name
}
class User(id: Int) : Entity(id){
    companion object : EntityClass<User>( Users, User::class.java)
    var name by Users.name
    var room by Room referencedBy Users.classId
}

