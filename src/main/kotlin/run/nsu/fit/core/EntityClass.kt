package run.nsu.fit.core

open class EntityClass<T>(val table: Table, entityType: Class<T>) {
    fun findById(id: Int): T? {
        return null
    }

    fun find(condition: Condition): Sequence<T> {
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