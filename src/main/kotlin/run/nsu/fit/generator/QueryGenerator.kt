package run.nsu.fit.generator

import run.nsu.fit.core.Column
import run.nsu.fit.core.Condition
import run.nsu.fit.core.Row
import run.nsu.fit.core.Table
import run.nsu.fit.generator.backend.ColumnType
import run.nsu.fit.generator.backend.Backend

class QueryGenerator(private val backend: Backend) {
    fun createTable(table: Table) {
        backend.createTable(table.getName()) {
            table.getColumns().forEach { column ->
                when (column) {
                    is Column.Integer -> {
                        column(column.name, ColumnType.INT)
                    }
                    is Column.Varchar -> {
                        column(column.name, ColumnType.VARCHAR(column.length))
                    }
                }
            }
        }
    }

    fun getRows(table: Table, condition: Condition): Sequence<Row> {
        return backend.select(table, condition)
    }

}