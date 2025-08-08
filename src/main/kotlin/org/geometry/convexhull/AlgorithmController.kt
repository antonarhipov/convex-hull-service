package org.geometry.convexhull

import org.geometry.math.D
import org.geometry.math.TheAlgorithm
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AlgorithmController(private val repo: ExecutionRepository) {
    @PostMapping("/ds")
    fun createDList(@RequestBody ds: List<D>): ResponseEntity<List<D>> {
        val start = System.nanoTime()
        val input = ds.toTypedArray()
        val result = TheAlgorithm.run(input) // may be null if < 3 points
        val durationMs = (System.nanoTime() - start) / 1_000_000
        val resultList = result?.toList() ?: emptyList()

        val record = repo.save(
            algorithm = "legacy-jarvis",
            input = ds,
            result = resultList,
            durationMs = durationMs
        )
        println("Computed hull: ${resultList.size} points in ${durationMs}ms (execId=${record.id})")

        val headers = HttpHeaders()
        headers.add("X-Execution-Id", record.id)
        return ResponseEntity.ok().headers(headers).body(resultList)
    }

    @GetMapping("/executions")
    fun listExecutions(): ResponseEntity<List<ExecutionSummary>> =
        ResponseEntity.ok(repo.findAll())

    @GetMapping("/executions/{id}")
    fun getExecution(@PathVariable id: String): ResponseEntity<ExecutionRecord> {
        val record = repo.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(record)
    }
}
