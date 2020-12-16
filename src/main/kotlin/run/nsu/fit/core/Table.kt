package run.nsu.fit.core

open class Table {
    private val columns = mutableListOf<Column<*>>()
    val id = integer("id")
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
        return this.javaClass.simpleName
    }
}

open class Row {
    val map  = mutableMapOf<Column<*>, Any?>()
    operator fun <R> get(column: Column<R>): R? {
        return map.getOrDefault(column, null) as? R?
    }

    operator fun <R> set(column: Column<R>, value: Any) {
        map[column] = value
    }
}