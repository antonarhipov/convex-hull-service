package org.geometry.convexhull

import org.assertj.core.api.Assertions.assertThat
import org.geometry.math.D
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@TestPropertySource(properties = [
    // Ensure JDBC autoconfiguration is NOT excluded (overrides src/test/resources/application.properties)
    "spring.autoconfigure.exclude=",
    // Force schema.sql execution using the same DataSource (container credentials)
    "spring.sql.init.mode=always"
])
@Testcontainers
class DatabaseIntegrationTests {

    companion object {
        @Container
        @JvmStatic
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:16-alpine").apply {
            withUsername("postgres")
            withPassword("postgres")
            withDatabaseName("convexhull")
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }
        }
    }

    @Autowired
    lateinit var repo: ExecutionRepository

    @LocalServerPort
    var port: Int = 0

    @Autowired
    lateinit var rest: TestRestTemplate

    @Test
    fun saveAndRetrieveViaRepository() {
        // Arrange
        val input = listOf(D(0, 0), D(10, 0), D(10, 10), D(0, 10))
        val hull = listOf(D(0, 0), D(10, 0), D(10, 10), D(0, 10))

        // Act
        val saved = repo.save("it-test", input, hull, 5L)

        // Assert
        assertThat(saved.id).isNotBlank()

        val byId = repo.findById(saved.id)
        assertThat(byId).isNotNull()
        assertThat(byId!!.algorithm).isEqualTo("it-test")
        assertThat(byId.input.map { it.x to it.y }).containsExactlyElementsOf(input.map { it.x to it.y })
        assertThat(byId.result.map { it.x to it.y }).containsExactlyElementsOf(hull.map { it.x to it.y })
        assertThat(byId.durationMs).isEqualTo(5L)

        val summaries = repo.findAll()
        assertThat(summaries.any { it.id == saved.id && it.inputCount == input.size && it.resultCount == hull.size }).isTrue()
    }

    @Test
    fun submitAndRetrieveViaHttpEndpoints() {
        // Arrange: POST /ds with a simple square (algorithm will compute hull and persist)
        val ds = listOf(D(0, 0), D(10, 0), D(10, 10), D(0, 10))

        val response = rest.postForEntity("http://localhost:$port/ds", ds, String::class.java)
        assertThat(response.statusCode.is2xxSuccessful).isTrue()

        val execId = response.headers.getFirst("X-Execution-Id")
        assertThat(execId).isNotNull()

        // GET /executions should contain the execution
        val listResponse = rest.getForEntity("http://localhost:$port/executions", Array<ExecutionSummary>::class.java)
        assertThat(listResponse.statusCode.is2xxSuccessful).isTrue()
        val summaries = listResponse.body!!.toList()
        assertThat(summaries.any { it.id == execId }).isTrue()

        // GET /executions/{id} should return the full record
        val recordResponse = rest.getForEntity("http://localhost:$port/executions/$execId", String::class.java)
        assertThat(recordResponse.statusCode.is2xxSuccessful).isTrue()
        val recordJson = recordResponse.body!!
        assertThat(recordJson).contains("\"id\":\"$execId\"")
    }
}

// Small helper to avoid adding new files: extension on ExecutionRecord to get counts
private fun ExecutionRecord.inputCountAndResultCount(): Pair<Int, Int> =
    this.input.size to this.result.size
