package run.nsu.fit.core

import kotlin.reflect.KProperty

open class Entity(id: Int) {

    operator fun <T> Column<T>.getValue(o: Entity, desc: KProperty<*>): T{
        return o.row[this]!!
    }
    operator fun <T> Column<T>.setValue(o: Entity, desc: KProperty<*>, value: T){
        row[this] = value as Any
    }

    operator fun <R, T : EntityRef<R>> T.getValue(o: Entity, desc: KProperty<*>): R{
        val column = this.rule.second
        val value = this@Entity.row[this.rule.first]!!
        val condition = Condition.Const(column, value)
        return this.target.find(condition).first()
    }

    operator fun <R, T : EntityRef<R>> T.setValue(o: Entity, desc: KProperty<*>, value: R){
        TODO()
    }

    var row = Row()
}