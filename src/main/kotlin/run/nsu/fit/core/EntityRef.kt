package run.nsu.fit.core

import kotlin.reflect.KProperty

class EntityRef<T : Entity>(val target: EntityClass<T>, val rule: Pair<Column<Int>, Column<Int>>) {


}