package org.geometry.convexhull.algo

import org.springframework.stereotype.Service

data class AlgorithmInfo(
    val name: String,
    val displayName: String,
    val complexity: String
)

@Service
class AlgorithmRegistry(strategies: List<AlgorithmStrategy>) {
    private val byName: Map<String, AlgorithmStrategy> = strategies.associateBy { it.name }
    private val list: List<AlgorithmStrategy> = strategies.sortedBy { it.displayName }

    val infos: List<AlgorithmInfo> = list.map { AlgorithmInfo(it.name, it.displayName, it.complexity) }

    fun getOrNull(name: String): AlgorithmStrategy? = byName[name]

    fun default(): AlgorithmStrategy = byName["legacy-jarvis"] ?: list.first()
}
