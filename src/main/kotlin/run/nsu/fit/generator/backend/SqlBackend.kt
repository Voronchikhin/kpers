package run.nsu.fit.generator.backend

import run.nsu.fit.generator.backend.CreateTable

interface SqlBackend {
    val createTable: CreateTable
}