package org.geometry.convexhull.algo

import org.geometry.convexhull.ext.Coordinate
import org.geometry.convexhull.ext.JarvisMarch
import org.geometry.math.D
import org.springframework.stereotype.Component

@Component
class JarvisAlgorithm : AlgorithmStrategy {
    override val name: String = "jarvis"
    override val displayName: String = "Jarvis March (Kotlin)"
    override val complexity: String = "O(nh)"

    private val impl = JarvisMarch()

    override fun run(points: List<D>): List<D> {
        if (points.size < 3) return emptyList()
        val input = points.map { Coordinate(it.x, it.y) }
        val out = impl.run(input)
        return out.map { D(it.x, it.y) }
    }
}
