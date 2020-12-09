package run.nsu.fit.core

open class Table {
    fun varchar(name: String, length: Int): Column<String> {
        return Column()
    }

    fun integer(name: String): Column<Int> {
        return Column()
    }

    fun select(query: QueryDsl.() -> Condition) {

    }

    fun delete(query: QueryDsl.() -> Condition) {

    }

    fun insert(block: Row.() -> Unit) {

    }

    fun innerJoin(otherTable: Table, query: QueryDsl.() -> Condition): Table {
        return TODO()
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