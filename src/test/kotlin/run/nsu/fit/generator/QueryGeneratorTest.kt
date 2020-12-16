package run.nsu.fit.generator

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import run.nsu.fit.core.Table
import run.nsu.fit.generator.backend.ColumnType
import run.nsu.fit.generator.backend.CreateTableDSL
import run.nsu.fit.generator.backend.Backend

internal class QueryGeneratorTest{

    lateinit var backend: Backend
    lateinit var createDsl: CreateTableDSL
    lateinit var queryGenerator: QueryGenerator
    @BeforeEach
    fun  init(){
        backend = mockk()
        createDsl = mockk()
        val slot = slot<CreateTableDSL.()->Unit>()
        every {
            backend.createTable(any(), capture(slot))
        }answers{
            slot.captured.invoke(createDsl)
        }
        queryGenerator = QueryGenerator(backend)
    }

    @Test
    fun `create empty table with id`(){
        class SimpleTable(): Table()

        queryGenerator.createTable(SimpleTable())

        verify {
            createDsl.column("id", ColumnType.INT)
        }
    }
}