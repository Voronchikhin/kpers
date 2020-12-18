package run.nsu.fit

import org.junit.jupiter.api.Test
import run.nsu.fit.core.Table

internal class TableTest {

    @Test
    fun `test simple table description`() {
        val users = object : Table() {
            val name = integer("name")
        }
    }

    @Test
    fun `test simple select query`() {
        val users = object : Table() {
            val name = integer("name")
        }
        users.select {
            users.id eq users.name
        }
    }

    @Test
    fun `create simple insert query`() {
        val users = object : Table() {
            val name = varchar("name", 15)
        }
        users.insert {
            this[users.id] = 25
            this[users.name] = "Maxim"
        }
    }

    @Test
    fun `create simple join`(){
        val users = object : Table() {
            val name = varchar("name", 15)
        }
        val badUsers = object : Table() {
            val name = varchar("name", 15)
        }

    }
}
