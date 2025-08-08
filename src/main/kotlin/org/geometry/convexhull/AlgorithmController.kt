package org.geometry.convexhull

import org.geometry.convexhull.algo.AlgorithmInfo
import org.geometry.convexhull.algo.AlgorithmRegistry
import org.geometry.math.D
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AlgorithmController(
    private val repo: ExecutionRepository,
    private val registry: AlgorithmRegistry
) {
    @GetMapping("/algorithms")
    fun listAlgorithms(): ResponseEntity<List<AlgorithmInfo>> =
        ResponseEntity.ok(registry.infos)

    @PostMapping("/ds")
    fun createDList(
        @RequestBody ds: List<D>,
        @RequestParam(name = "algo", required = false, defaultValue = "legacy-jarvis") algo: String
    ): ResponseEntity<List<D>> {
        val strategy = registry.getOrNull(algo)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emptyList())

        val start = System.nanoTime()
        val resultList = strategy.run(ds)
        val durationMs = (System.nanoTime() - start) / 1_000_000

        val record = repo.save(
            algorithm = strategy.name,
            input = ds,
            result = resultList,
            durationMs = durationMs
        )
        println("Computed hull via '${strategy.name}': ${resultList.size} points in ${durationMs}ms (execId=${record.id})")

        val headers = HttpHeaders().apply { add("X-Execution-Id", record.id) }
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
