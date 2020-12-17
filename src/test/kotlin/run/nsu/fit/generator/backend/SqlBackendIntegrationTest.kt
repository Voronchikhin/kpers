package run.nsu.fit.generator.backend

import com.mysql.cj.jdbc.Driver
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import run.nsu.fit.core.Condition
import run.nsu.fit.core.Row
import run.nsu.fit.core.Table


internal class SqlBackendIntegrationTest {

    private lateinit var sqlBackend: SqlBackend
    @BeforeEach
    fun init(){
        val driver = Driver()
        sqlBackend = SqlBackend(driver, "jdbc:mysql://localhost:3306/test", "root", "password")
    }

    @AfterEach
    fun shutDown(){
        sqlBackend.close()
    }
    @Test
    fun createTable() {
        sqlBackend.createTable("Table100"){
            column("id", ColumnType.INT)
            column("name", ColumnType.VARCHAR(255))
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
    fun delete() {
        val condition = Condition.Const(Table100.id, 1)
        sqlBackend.delete(Table100, condition)
    }
}
object Table100: Table(){
    val name = varchar("name", 255)
}