package org.geometry.convexhull.ext


interface Algorithm {
    companion object {
        const val MINIMUM_POINTS_FOR_HULL = 3
    }

    fun run(coordinates: List<Coordinate>): List<Coordinate>
}