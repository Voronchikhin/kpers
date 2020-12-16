package run.nsu.fit.generator.backend

import run.nsu.fit.core.Column
import run.nsu.fit.core.Condition
import run.nsu.fit.core.Row
import run.nsu.fit.core.Table
import java.lang.IllegalArgumentException
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

        connection.createStatement().execute(SqlGenerator.insert(table, row))
    }

    override fun delete(table: Table, condition: Condition) {
        TODO("Not yet implemented")
    }

    override fun select(table: Table, condition: Condition): Sequence<Row> {
        val sql = SqlGenerator.select(table, condition)
        val createStatement = connection.createStatement()
        val resultSet = createStatement.executeQuery(sql)
        return sequence {
            while(resultSet.next()){
                val row = Row()
                table.getColumns().forEach {
                    row[it] = it.extractValue(resultSet) as Any
                }
                yield(row)
            }
        }
    }
    private fun <R> Column<R>.extractValue(resultSet: ResultSet): R{
        return when(this){
            is Column.Integer -> resultSet.getInt(this.name) as R
            is Column.Varchar -> resultSet.getString(this.name) as R
        }
    }
    companion object SqlGenerator {
        fun createTable(tableName: String, block: CreateTableDSL): String {
            val columnString = StringBuilder()
            block.columnsAcc.forEach {
                columnString.append(" ${it.columnName} ${it.columnType}, ")
            }
            columnString.deleteAt(columnString.length - 2)
            println("CREATE TABLE $tableName (${columnString});")
            return "CREATE TABLE $tableName (${columnString});"
        }

        fun insert(table: Table, row: Row): String {
            val list = mutableListOf<ItemData>()
            table.getColumns().forEach{
                when(it){
                    is Column.Integer -> list.add(ItemData(it.name, row[it].toString()))
                    is Column.Varchar -> list.add(ItemData(it.name, "'${row[it]}'"))
                }
            }
            return "INSERT INTO ${table.getName()} (${list.joinToString { it.columnName }}) VALUES ( ${list.joinToString { it.value }} );"
        }

        fun delete(table: Table, condition: Condition): String {
            TODO("Not yet implemented")
        }

        fun select(table: Table, condition: Condition): String {
            TODO("Not yet implemented")
        }
        internal fun processCondition(condition: Condition): String{
            return "WHERE ( ${doPrecessingCondition(condition)} )"
        }
        private fun doPrecessingCondition(condition: Condition): String{
            return when(condition){
                is Condition.Equal<*> -> " ${condition.first.refName()} = ${condition.second.refName()} "
                is Condition.Const<*> -> {
                    if( condition.column is Column.Integer ) {
                        " ${condition.column.refName()} = ${condition.value} "
                    } else{
                        " ${condition.column.refName()} = '${condition.value}' "
                    }
                }

                is Condition.And -> " (${doPrecessingCondition(condition.first)}) AND (${doPrecessingCondition(condition.second)}) "
                else -> throw IllegalArgumentException("fuck this")
            }
        }
    }

}