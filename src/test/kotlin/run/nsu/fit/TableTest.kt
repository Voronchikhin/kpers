package run.nsu.fit

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TableTest {

    @Test
    fun `test simple table description`() {
        val users = object : Table() {
            val id = integer("id")
            val name = integer("name")
        }
    }

    @Test
    fun `test simple select query`() {
        val users = object : Table() {
            val id = integer("id")
            val name = integer("name")
        }
        users.select {
            users.id eq users.name
        }
    }

    @Test
    fun `create simple insert query`() {
        val users = object : Table() {
            val id = integer("id")
            val name = varchar("name", 15)
        }
        users.insert {
            this[users.id] = 25
            this[users.name] = "Maxim"
        }
    }
}