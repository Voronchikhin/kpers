package run.nsu.fit.generator

import run.nsu.fit.core.Column
import run.nsu.fit.core.Table
import run.nsu.fit.generator.backend.ColumnType
import run.nsu.fit.generator.backend.Backend

class QueryGenerator(private val backend: Backend) {
    fun createTable(table: Table){
        backend.createTable(table.getName()){
            column("id", ColumnType.INT)
            table.getColumns().forEach { column ->
                when(column){
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
}