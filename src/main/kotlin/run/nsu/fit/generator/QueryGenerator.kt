package run.nsu.fit.generator

import run.nsu.fit.core.*
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

    fun updateRow(row: Row, table: Table) {
        backend.update(table, row)
    }

    fun join(tableOne: Table, tableTwo: Table): WrappedTable {
        return backend.createWrappedTable(tableOne, tableTwo)
    }

    fun dropTable(table: Table) {
        backend.drop(table)
    }

    fun deleteRows(table: Table, condition: Condition) {
        backend.delete(table, condition)
    }

    fun insertRow(row: Row, table: Table) {
        backend.insertRow(row, table)
    }


}