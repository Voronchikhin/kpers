package run.nsu.fit

open class EntityClass<T>(val table: Table, entityType: Class<T>? = null) {
    fun findById(id: Int): T? {
        return null
    }

    fun find(op: Op<Boolean>): Sequence<T> {
        return sequenceOf()
    }

    fun all(): Sequence<T> {
        return sequenceOf()
    }

    fun new(id: Int, init: T.() -> Unit) {
    }

    infix fun referencedBy(column: Column<Int>): T {
        TODO()
    }

    infix fun refersOn(column: Column<Int>): T {
        TODO()
    }

}