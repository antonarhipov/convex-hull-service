package org.geometry.convexhull.algo

import org.geometry.math.D

interface AlgorithmStrategy {
    val name: String
    val displayName: String
    val complexity: String

    fun run(points: List<D>): List<D>
}
