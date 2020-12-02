package run.nsu.fit

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TableTest{

    @Test
    fun `test simple table description`(){
        val users = object : Table(){
            val id= integer("id")
            val name = integer("name")
        }
    }
}