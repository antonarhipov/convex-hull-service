package org.geometry.convexhull.algo

import org.geometry.math.D
import org.geometry.math.TheAlgorithm
import org.springframework.stereotype.Component

@Component
class LegacyJarvisAlgorithm : AlgorithmStrategy {
    override val name: String = "legacy-jarvis"
    override val displayName: String = "Jarvis March (legacy)"
    override val complexity: String = "O(nh)"

    override fun run(points: List<D>): List<D> {
        if (points.size < 3) return emptyList()
        val result = TheAlgorithm.run(points.toTypedArray())
        return result?.toList() ?: emptyList()
    }
}
