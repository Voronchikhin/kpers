package run.nsu.fit

import org.junit.jupiter.api.Test
import run.nsu.fit.core.Entity
import run.nsu.fit.core.EntityClass
import run.nsu.fit.core.Table

internal class EntityClassTest{
    @Test
    fun `orm`(){
        val user = User.findById(25)
    }
}
object Users: Table(){
    val id = integer("id")
    val name = varchar("name", 32)
    val classId = integer("classId")
}
object Rooms: Table() {
    val id = integer("id")
    val name = varchar("name", 6)
}
class Room(id: Int) : Entity(id){
    companion object : EntityClass<Room>( Rooms, Room::class.java)
    var name by Rooms.name
}
class User(id: Int) : Entity(id){
    companion object : EntityClass<User>( Users, User::class.java)
    var name by Users.name
    var room by Room.referencedBy(Users.classId)
}

