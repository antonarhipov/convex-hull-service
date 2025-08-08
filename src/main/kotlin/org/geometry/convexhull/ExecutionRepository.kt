package org.geometry.convexhull

import com.fasterxml.jackson.databind.ObjectMapper
import org.geometry.math.D
import org.springframework.context.annotation.Profile
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

interface ExecutionRepository {
    fun save(algorithm: String, input: List<D>, result: List<D>, durationMs: Long): ExecutionRecord
    fun findAll(): List<ExecutionSummary>
    fun findById(id: String): ExecutionRecord?
}

@Profile("inmem")
@Component
class InMemoryExecutionRepository : ExecutionRepository {

    private val store = ConcurrentHashMap<String, ExecutionRecord>()

    override fun save(algorithm: String, input: List<D>, result: List<D>, durationMs: Long): ExecutionRecord {
        val id = UUID.randomUUID().toString()
        val record = ExecutionRecord(
            id = id,
            timestamp = Instant.now(),
            algorithm = algorithm,
            input = input.map { D(it.x, it.y) },
            result = result.map { D(it.x, it.y) },
            durationMs = durationMs
        )
        store[id] = record
        return record
    }

    override fun findAll(): List<ExecutionSummary> = store.values
        .sortedByDescending { it.timestamp }
        .map { r ->
            ExecutionSummary(
                id = r.id,
                timestamp = r.timestamp,
                algorithm = r.algorithm,
                inputCount = r.input.size,
                resultCount = r.result.size,
                durationMs = r.durationMs
            )
        }

    override fun findById(id: String): ExecutionRecord? = store[id]
}

@Table("executions")
data class ExecutionEntity(
    @Id val id: UUID? = null,
    @Column("created_at") val timestamp: Instant,
    val algorithm: String,
    val input: String,
    val result: String,
    @Column("duration_ms") val durationMs: Long
)

@Profile("!inmem") // "jdbc"??
@Repository
interface SpringDataExecutionCrud : CrudRepository<ExecutionEntity, UUID> {
    @Query("select id, created_at, algorithm, input, result, duration_ms from executions order by created_at desc")
    fun findAllOrderByCreatedAtDesc(): List<ExecutionEntity>
}

@Profile("!inmem")
@Repository
class JdbcExecutionRepository(
    private val crud: SpringDataExecutionCrud,
    private val objectMapper: ObjectMapper
) : ExecutionRepository {

    override fun save(algorithm: String, input: List<D>, result: List<D>, durationMs: Long): ExecutionRecord {
        val now = Instant.now()
        val entity = ExecutionEntity(
            id = null,
            timestamp = now,
            algorithm = algorithm,
            input = objectMapper.writeValueAsString(input),
            result = objectMapper.writeValueAsString(result),
            durationMs = durationMs,
        )
        val saved = crud.save(entity)
        return ExecutionRecord(
            id = saved.id!!.toString(),
            timestamp = now,
            algorithm = algorithm,
            input = input.map { D(it.x, it.y) },
            result = result.map { D(it.x, it.y) },
            durationMs = durationMs
        )
    }

    override fun findAll(): List<ExecutionSummary> =
        crud.findAllOrderByCreatedAtDesc().map { e ->
            val inputList = readDList(e.input)
            val resultList = readDList(e.result)
            ExecutionSummary(
                id = e.id!!.toString(),
                timestamp = e.timestamp,
                algorithm = e.algorithm,
                inputCount = inputList.size,
                resultCount = resultList.size,
                durationMs = e.durationMs
            )
        }

    override fun findById(id: String): ExecutionRecord? =
        runCatching { UUID.fromString(id) }.getOrNull()
            ?.let { uuid -> crud.findById(uuid).orElse(null) }
            ?.let { e ->
                ExecutionRecord(
                    id = e.id!!.toString(),
                    timestamp = e.timestamp,
                    algorithm = e.algorithm,
                    input = readDList(e.input),
                    result = readDList(e.result),
                    durationMs = e.durationMs
                )
            }

    private data class P(val x: Int = 0, val y: Int = 0)

    private fun readDList(json: String): List<D> {
        val type = objectMapper.typeFactory.constructCollectionType(List::class.java, P::class.java)
        val pts: List<P> = objectMapper.readValue(json, type)
        return pts.map { D(it.x, it.y) }
    }
}
