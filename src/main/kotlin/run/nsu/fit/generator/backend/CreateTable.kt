package run.nsu.fit.generator.backend

interface CreateTableDSL: SqlGenerator {
    var tableName: String
    fun column(columnName: String, columnDescription: ColumnDSL.()->Unit)

    interface ColumnDSL {
        var name: String
    }
}
//
//createTable("User"){
//      column("name", Varchar)
//} ---- >>>>> CREATE TABLE User (id : INT, name : VARCHAR(256) );
//
//
//
//
//
//
//
//
//
//
//