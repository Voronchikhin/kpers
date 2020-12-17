package run.nsu.fit.generator.backend

import run.nsu.fit.core.Condition
import run.nsu.fit.core.Row
import run.nsu.fit.core.Table


interface Backend {
    fun createTable(tableName: String, block: CreateTableDSL.()->Unit)
    fun insert(table: Table, row: Row)
    fun delete(table: Table, condition: Condition)
    fun select(table: Table, condition: Condition): Sequence<Row>
    fun update(table: Table, row: Row)

}