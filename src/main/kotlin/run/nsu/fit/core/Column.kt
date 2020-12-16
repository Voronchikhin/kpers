package run.nsu.fit.core

sealed class Column<T>(val table: Table, val name: String) {
    class Integer(table: Table, name: String): Column<Int>(table, name) {

    }

    class Varchar(table: Table, name: String, val length: Int): Column<String>(table, name){

    }
    fun refName():String = "${table.getName()}.$name"
}
