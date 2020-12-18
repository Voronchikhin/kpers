package run.nsu.fit.core

import kotlin.reflect.KProperty

open class Entity(id: Int) {
    val changedCash = mutableListOf<Pair<Entity, Table>>()

    operator fun <T> Column<T>.getValue(o: Entity, desc: KProperty<*>): T{
        return o.row[this]!!
    }

    operator fun <T> Column<T>.setValue(o: Entity, desc: KProperty<*>, value: T){
        row[this] = value as Any
    }

    operator fun <R : Entity, T : EntityRef<R>> T.getValue(o: Entity, desc: KProperty<*>): R{
        val column = this.rule.second
        val value = this@Entity.row[this.rule.first]!!
        val condition = Condition.Const(column, value)
        return this.target.findLazy(condition).first().also {
            changedCash.add(it to this.target.table)
        }
    }

    operator fun <R: Entity, T : EntityLazyRefs<R>> T.getValue(o: Entity, desc: KProperty<*>): Sequence<R> {
        val column = this.rule.second
        val value = this@Entity.row[this.rule.first]!!
        val condition = Condition.Const(column, value)
        return this.target.findLazy(condition).map {
            changedCash.add(it to target.table)
            it
        }
    }

    operator fun <R: Entity, T : EntityEagerRefs<R>> T.getValue(o: Entity, desc: KProperty<*>): List<R> {
        val column = this.rule.second
        val value = this@Entity.row[this.rule.first]!!
        val condition = Condition.Const(column, value)
        return this.target.findLazy(condition).map {
            changedCash.add(it to target.table)
            it
        }.toList()
    }

    var row = Row()
}