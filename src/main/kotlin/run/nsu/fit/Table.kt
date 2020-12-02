package run.nsu.fit

open class Table {
    fun varchar(name: String, length: Int): Column<String> {
        return Column()
    }

    fun integer(name: String): Column<Int> {
        return Column()
    }

    fun select(query: QueryDsl.() -> Unit) {

    }

    fun insert(block: Row.() -> Unit) {

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