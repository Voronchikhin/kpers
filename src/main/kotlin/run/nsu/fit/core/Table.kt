package run.nsu.fit.core

open class Table {
    private val columns = mutableListOf<Column<*>>()
    fun varchar(name: String, length: Int): Column<String> {
        val column = Column.Varchar(this, name , length)
        columns.add(column)
        return column
    }

    fun integer(name: String): Column<Int> {
        val column = Column.Integer(this, name)
        columns.add(column)
        return column
    }

    fun select(condition: ConditionDsl.() -> Condition) {

    }

    fun delete(condition: ConditionDsl.() -> Condition) {

    }

    fun insert(block: Row.() -> Unit) {

    }

    fun innerJoin(otherTable: Table, condition: ConditionDsl.() -> Condition): Table {
        return TODO()
    }
    fun getColumns(): List<Column<*>>{
        return columns
    }
    fun getName(): String{
        return this.javaClass.name
    }
}

open class Row {
    operator fun <R> get(column: Column<R>): R {
        return TODO()
    }

    operator fun <R> set(column: Column<R>, value: R) {
        return
    }
}