package run.nsu.fit.generator.backend


interface SqlBackend {
    val createTable: CreateTableDSL
}