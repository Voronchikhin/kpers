package run.nsu.fit.generator.backend

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import run.nsu.fit.core.Column
import run.nsu.fit.core.Condition
import run.nsu.fit.core.Table

internal class SqlBackendTest {

    @Test
    fun createTable() {
        val block = CreateTableDSL()
        block.column("id", ColumnType.INT)
        block.column("countryId", ColumnType.INT)
        block.column("name", ColumnType.VARCHAR(150))
        val createTable = SqlBackend.createTable("City", block)
        assertEquals("CREATE TABLE City(id INT,countryId INT,name VARCHAR(150));", createTable)
    }

    @Test
    fun insert() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun select() {
    }
    @Test
    fun testSimpleCondition(){
        val condition = Condition.Equal(SimpleTable.id, OtherTable.id)
        assertEquals("WHERE (  SimpleTable.id = OtherTable.id  )", SqlBackend.processCondition(condition))
    }

    @Test
    fun testSimpleConstInt(){
        val condition = Condition.Const(SimpleTable.id, 1)
        assertEquals("WHERE (  SimpleTable.id = 1  )", SqlBackend.processCondition(condition))
    }
    @Test
    fun testSimpleConstVarchar(){
        val condition = Condition.Const(SimpleTable.name, "name")
        assertEquals("WHERE (  SimpleTable.name = 'name'  )", SqlBackend.processCondition(condition))
    }

    @Test
    fun testNestedCondition(){
        val condition = Condition.And (Condition.Equal(SimpleTable.id, OtherTable.id), Condition.Equal(SimpleTable.name, OtherTable.data) )
        assertEquals("WHERE (  ( SimpleTable.id = OtherTable.id ) AND ( SimpleTable.name = OtherTable.data )  )", SqlBackend.processCondition(condition))
    }

}
object SimpleTable: Table(){
    val name = varchar("name", 25)
}

object OtherTable: Table(){
    val data = varchar("data", 25)
}