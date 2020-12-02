package run.nsu.fit

open class Table {
    fun varchar(name: String, length: Int): Column{return  Column()
    }
    fun integer(name: String): Column{ return Column()}

    fun select(query: QueryDsl.()-> Unit){

    }
    fun insert(block: Row.()->Unit){

    }
}
open class Row{
    operator fun get(column: Column): Any {
        return ""
    }
    operator fun set(column: Column, value: Any){
        return
    }
}