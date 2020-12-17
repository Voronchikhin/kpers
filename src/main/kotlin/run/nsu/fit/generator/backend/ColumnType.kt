package run.nsu.fit.generator.backend

sealed class ColumnType {
    object INT : ColumnType(){
        override fun toString(): String {
            return "INT"
        }
    }
    class VARCHAR(private val length: Int) : ColumnType(){
        override fun toString(): String {
            return "VARCHAR($length)"
        }
    }
}
