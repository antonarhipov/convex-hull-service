package org.geometry.convexhull

import org.geometry.math.D
import java.time.Instant

// Represents a stored execution with input, result and metadata
data class ExecutionRecord(
    val id: String,
    val timestamp: Instant,
    val algorithm: String,
    val input: List<D>,
    val result: List<D>,
    val durationMs: Long
)

// Lightweight DTO for listing executions
data class ExecutionSummary(
    val id: String,
    val timestamp: Instant,
    val algorithm: String,
    val inputCount: Int,
    val resultCount: Int,
    val durationMs: Long
)
