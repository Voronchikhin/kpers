package run.nsu.fit.core

class QueryDsl {
    infix fun<R> Column<R>.eq(column: Column<R>): Condition {
        return TODO()
    }
}