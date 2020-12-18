package run.nsu.fit.core

import com.mysql.cj.jdbc.Driver
import run.nsu.fit.generator.QueryGenerator
import run.nsu.fit.generator.backend.SqlBackend

open class Table {
    internal val columns = mutableListOf<Column<*>>()
    val id = integer("id")
    fun varchar(name: String, length: Int): Column<String> {
        val column = Column.Varchar(this, name, length)
        columns.add(column)
        return column
    }

    fun integer(name: String): Column<Int> {
        val column = Column.Integer(this, name)
        columns.add(column)
        return column
    }

    fun select(block: ConditionDsl.() -> Condition): Sequence<Row> {
        val condition = ConditionDsl().block()
        return queryGenerator.getRows(this, condition)
    }

    fun delete(block: ConditionDsl.() -> Condition) {
        val condition = ConditionDsl().block()
        queryGenerator.deleteRows(this, condition)
    }

    fun insert(block: Row.() -> Unit) {
        val row = Row().apply { block() }
        queryGenerator.updateRow(row, this)
    }

    fun getColumns(): List<Column<*>> {
        return columns
    }

    open fun getName(): String {
        return this.javaClass.simpleName
    }

    fun createTable() {
        queryGenerator.createTable(this)
    }

    fun join(table: Table): WrappedTable {
        return queryGenerator.join(this, table)
    }

    companion object {
        private val driver = Driver()
        private val queryGenerator =
            QueryGenerator(SqlBackend(driver, "jdbc:mysql://localhost:3306/test", "root", "password"))
    }

}

open class Row {
    val map = mutableMapOf<Column<*>, Any?>()
    operator fun <R> get(column: Column<R>): R? {
        return map.getOrDefault(column, null) as? R?
    }

    operator fun <R> set(column: Column<R>, value: Any) {
        map[column] = value
    }
}

fun <T: Entity> Sequence<Row>.wrap(entityClass: Class<T>): Sequence<T> {
    return map{ row ->
        entityClass.getConstructor(Int::class.java).newInstance(1).also {
            it.row = row
        }
    }
}