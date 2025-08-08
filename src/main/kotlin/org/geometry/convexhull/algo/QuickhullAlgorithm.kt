package org.geometry.convexhull.algo

import org.geometry.convexhull.ext.Coordinate
import org.geometry.convexhull.ext.Quickhull
import org.geometry.math.D
import org.springframework.stereotype.Component

@Component
class QuickhullAlgorithm : AlgorithmStrategy {
    override val name: String = "quickhull"
    override val displayName: String = "Quickhull"
    override val complexity: String = "O(n log n) avg"

    private val impl = Quickhull()

    override fun run(points: List<D>): List<D> {
        if (points.size < 3) return emptyList()
        val input = points.map { Coordinate(it.x, it.y) }
        val out = impl.run(input)
        return out.map { D(it.x, it.y) }
    }
}
