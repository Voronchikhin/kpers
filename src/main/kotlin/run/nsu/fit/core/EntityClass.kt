package run.nsu.fit.core

import com.mysql.cj.jdbc.Driver
import run.nsu.fit.generator.QueryGenerator
import run.nsu.fit.generator.backend.SqlBackend

open class EntityClass<T : Entity>(val table: Table, entityType: Class<T>) {
    private val driver = Driver()
    private val queryGenerator =
        QueryGenerator(SqlBackend(driver, "jdbc:mysql://localhost:3306/test", "root", "password"))
    private val constructor = entityType.getConstructor(Int::class.java)

    fun findById(id: Int): T? {
        val condition = Condition.Const(table.id, id)
        return queryGenerator.getRows(table, condition).map {
            val instance = constructor.newInstance(it[table.id])
            instance.row = it
            instance
        }.firstOrNull()
    }

    fun find(condition: Condition): Sequence<T> {
        return queryGenerator.getRows(table, condition).map {
            val instance = constructor.newInstance(it[table.id])
            instance.row = it
            instance
        }
    }

    fun all(): Sequence<T> {
        return queryGenerator.getRows(table, Condition.Any).map {
            val instance = constructor.newInstance(it[table.id])
            instance.row = it
            instance
        }
    }

    fun new(id: Int, init: T.() -> Unit) {
        val instance = constructor.newInstance(id)
        queryGenerator.updateRow(instance.row, table)
    }


    infix fun referencedBy(otherTableColumn: Column<Int>): EntityRef<T> {
        return EntityRef(this, Pair(otherTableColumn, table.id))
    }

    fun referencedBy(otherTableColumn: Column<Int>, thisTableColumn: Column<Int>): EntityRef<T> {
        return EntityRef(this, Pair(otherTableColumn, thisTableColumn))
    }



    fun flush(any: T) {
        queryGenerator.updateRow(any.row, table)
    }

}