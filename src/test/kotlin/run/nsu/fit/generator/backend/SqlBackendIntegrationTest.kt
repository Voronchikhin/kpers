package run.nsu.fit.generator.backend

import com.mysql.cj.jdbc.Driver
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import run.nsu.fit.core.Row
import run.nsu.fit.core.Table


internal class SqlBackendIntegrationTest {

    private lateinit var sqlBackend: SqlBackend
    @BeforeEach
    fun init(){
        val driver = Driver()
        sqlBackend = SqlBackend(driver, "jdbc:mysql://localhost:3306/test", "root", "password")
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
        val table100 = Table100()
        val row = Row()
        row[table100.id] = 1
        row[table100.name] = "name"
        sqlBackend.insert(table100, row)
    }

    @Test
    fun select() {
    }
}
class Table100: Table(){
    val name = varchar("name", 255)
}