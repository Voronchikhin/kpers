package run.nsu.fit

open class Table {
    fun varchar(name: String, length: Int): Column{return  Column()
    }
    fun integer(name: String): Column{ return Column()}

    fun select(query: QueryDsl.()-> Unit){

    }
}
