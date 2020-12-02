package run.nsu.fit

class QueryDsl {
    infix fun<R> Column<R>.eq( column: Column<R>){}
}