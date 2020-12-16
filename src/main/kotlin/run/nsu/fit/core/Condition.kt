package run.nsu.fit.core

sealed class Condition(
) {
    class Or(val first: Condition, val second: Condition) : Condition()
    class And(val first: Condition, val second: Condition) : Condition()
    class Equal<T>(val first: Column<T>, val second: Column<T>) : Condition()
    class Const<T>(val column: Column<T>, val value: T): Condition(){
    }
}
