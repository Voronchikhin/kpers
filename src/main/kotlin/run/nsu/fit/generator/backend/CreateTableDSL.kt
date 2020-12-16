package run.nsu.fit.generator.backend

class CreateTableDSL {
    val columnsAcc = mutableListOf<ColumnData>()
    fun column(columnName: String, type: ColumnType) {
        columnsAcc.add(ColumnData(columnName, type))
    }

    data class ColumnData(
        val columnName: String,
        val columnType: ColumnType
    )
}
//
//createTable("User"){
//      column("name", Varchar)
//} ---- >>>>> CREATE TABLE User (id : INT, name : VARCHAR(256) );
//
//
//
//