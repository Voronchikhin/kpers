package run.nsu.fit.core

class ConditionDsl {
    infix fun<R> Column<R>.eq(column: Column<R>): Condition {
        return Condition.Equal(this, column)
    }infix fun<R> Column<R>.eq(columnValue: R): Condition {
        return Condition.Const(this, columnValue)
    }

    infix fun Condition.and(condition: Condition): Condition{
        return Condition.And(this, condition)
    }
}