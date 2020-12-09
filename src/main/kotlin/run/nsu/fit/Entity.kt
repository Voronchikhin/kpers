package run.nsu.fit

import kotlin.reflect.KProperty

open class Entity(id: Int) {
    operator fun <T> Column<T>.getValue(o: Entity, desc: KProperty<*>): T{
        TODO()
    }
    operator fun <T> Column<T>.setValue(o: Entity, desc: KProperty<*>, value: T){
        TODO()
    }

    operator fun <T : Entity> T.getValue(o: Entity, desc: KProperty<*>): T{
        TODO()
    }

    operator fun <T : Entity> T.setValue(o: Entity, desc: KProperty<*>, value: T){
        TODO()
    }

}