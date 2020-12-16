package run.nsu.fit.generator.backend

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
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
}