package run.nsu.fit.generator.backend

import run.nsu.fit.core.*
import java.io.Closeable
import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.sql.*


class SqlBackend(driver: Driver, url: String, user: String, password: String) : Backend, Closeable {
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
        val insertSql = SqlGenerator.insert(table, row)
        println(insertSql)
        connection.createStatement().execute(insertSql)
    }

    override fun delete(table: Table, condition: Condition) {
        val sql = SqlGenerator.delete(table, condition)
        val createStatement = connection.createStatement()
        createStatement.executeUpdate(sql)
    }

    override fun select(table: Table, condition: Condition): Sequence<Row> {
        val sql = SqlGenerator.select(table, condition)
        val createStatement = connection.createStatement()
        println(sql)
        val resultSet = createStatement.executeQuery(sql)
        return sequence {
            while (resultSet.next()) {
                val row = Row()
                table.getColumns().forEach {
                    row[it] = it.extractValue(resultSet) as Any
                }
                yield(row)
            }
        }
    }

    override fun createWrappedTable(
        tableFrom1: Table,
        tableFrom2: Table
    ): WrappedTable {
        val wrappedTable = WrappedTable()
        wrappedTable.columns.addAll(tableFrom1.getColumns())
        wrappedTable.columns.addAll(tableFrom2.getColumns())
        wrappedTable.wrappedName = "${tableFrom1.getName()} join ${tableFrom2.getName()} "
        return wrappedTable
    }

    override fun update(table: Table, row: Row) {
        val sql = SqlGenerator.update(table, row)
        val createStatement = connection.createStatement()
        println(sql)
        createStatement.executeUpdate(sql)
    }

    override fun drop(table: Table) {
        connection.createStatement().execute("DROP TABLE ${table.getName()}")
    }

    override fun insertRow(row: Row, table: Table) {
        insert(table, row)
    }

    private fun <R> Column<R>.extractValue(resultSet: ResultSet): R {
        return when (this) {
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
            table.getColumns().forEach {
                if (row[it] != null) {
                    list.add(ItemData(it.name, changeValueToString(row[it])))
                }
            }
            return "INSERT INTO ${table.getName()} (${list.joinToString { it.columnName }}) VALUES ( ${list.joinToString { it.value }} );"
        }

        private fun changeValueToString(any: Any?): String {
            return when (any) {
                is Int -> any.toString()
                is String -> "'$any'"
                else -> throw Exception("Unknown type")
            }
        }

        fun delete(table: Table, condition: Condition): String {
            return "DELETE FROM ${table.getName()} ${processCondition(condition)}"
        }


        fun select(table: Table, condition: Condition): String {
            return when (condition) {
                is Condition.Join<*> -> "SELECT * FROM "
                else -> "SELECT * FROM ${table.getName()} ${processCondition(condition)}"
            }
        }

        fun selectFromTwoTable(tableFrom1: Table, tableFrom2: Table, condition: Condition): String {
            return "SELECT * FROM ${tableFrom1.getName()} join ${tableFrom2.getName()} ${processCondition(condition)}"

        }

        internal fun processCondition(condition: Condition): String {
            return "WHERE ( ${doPrecessingCondition(condition)} )"
        }

        private fun doPrecessingCondition(condition: Condition): String {
            return when (condition) {
                is Condition.Any -> "1 = 1"
                is Condition.Equal<*> -> " ${condition.first.refName()} = ${condition.second.refName()} "
                is Condition.Const<*> -> {
                    if (condition.column is Column.Integer) {
                        " ${condition.column.refName()} = ${condition.value} "
                    } else {
                        " ${condition.column.refName()} = '${condition.value}' "
                    }
                }

                is Condition.And -> " (${doPrecessingCondition(condition.first)}) AND (${doPrecessingCondition(condition.second)}) "
                else -> throw IllegalArgumentException("fuck this")
            }
        }

        fun update(table: Table, row: Row): String {
            val columns = table.getColumns()
            val joinToString = columns.joinToString(",") {
                "${it.refName()}=${changeValueToString(row[it])}"
            }
            return "UPDATE ${table.getName()} SET $joinToString WHERE id = ${row[columns.first()]};"
        }
    }

    override fun close() {
        connection.close()
    }

}