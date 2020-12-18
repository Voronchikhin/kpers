package run.nsu.fit.generator.backend

import run.nsu.fit.core.*


interface Backend {
    fun createTable(tableName: String, block: CreateTableDSL.()->Unit)
    fun insert(table: Table, row: Row)
    fun delete(table: Table, condition: Condition)
    fun select(table: Table, condition: Condition): Sequence<Row>
    fun createWrappedTable(tableFrom1: Table, tableFrom2: Table): WrappedTable
    fun update(table: Table, row: Row)
    fun drop(table: Table)
    fun insertRow(row: Row, table: Table)

}