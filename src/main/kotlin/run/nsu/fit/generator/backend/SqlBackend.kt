package run.nsu.fit.generator.backend

import run.nsu.fit.core.Column
import run.nsu.fit.core.Condition
import run.nsu.fit.core.Row
import run.nsu.fit.core.Table
import java.lang.StringBuilder
import java.sql.*


class SqlBackend(driver: Driver, url: String, user: String, password: String) : Backend {
    private val connection: Connection = DriverManager.getConnection(url, user, password)


    override fun createTable(tableName: String, block: CreateTableDSL.() -> Unit) {
        val createTableDSL = CreateTableDSL()
        createTableDSL.block()
        val statement = connection.createStatement()
        statement.execute(SqlGenerator.createTable(tableName, createTableDSL))
        statement.close()
    }

    data class ItemData(
        val columnName: String,
        val value: String
    )

    override fun insert(table: Table, row: Row) {

        connection.createStatement().execute("")
    }

    override fun delete(table: Table, condition: Condition) {
        TODO("Not yet implemented")
    }

    override fun select(table: Table, condition: Condition): Sequence<Row> {
        TODO("Not yet implemented")
    }

    companion object SqlGenerator {
        fun createTable(tableName: String, block: CreateTableDSL): String {
            val columnString = StringBuilder()
            block.columnsAcc.forEach {
                columnString.append("${it.columnName} ${it.columnType},")
            }
            columnString.deleteAt(columnString.length - 1)

            return "CREATE TABLE $tableName(${columnString});"
        }

        fun insert(table: Table, row: Row): String {
            val list = mutableListOf<ItemData>()
            table.getColumns().forEach{
                when(it){
                    is Column.Integer -> list.add(ItemData(it.name, row[it].toString()))
                    is Column.Varchar -> list.add(ItemData(it.name, "'${row[it]}'"))
                }
                list.add(ItemData(it.name, row[it].toString()))
            }
            return "INSERT INTO ${table.getName()} (${list.joinToString { it.columnName }}) VALUES ( ${list.joinToString { it.value }} );"
        }

        fun delete(table: Table, condition: Condition): String {
            TODO("Not yet implemented")
        }

        fun select(table: Table, condition: Condition): String {
            TODO("Not yet implemented")
        }

    }

}