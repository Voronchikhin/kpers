package run.nsu.fit.core

data class EntityRef<T : Entity>(val target: EntityClass<T>, val rule: Pair<Column<Int>, Column<Int>>)
data class EntityRefs<T : Entity>(val target: EntityClass<T>, val rule: Pair<Column<Int>, Column<Int>>)

