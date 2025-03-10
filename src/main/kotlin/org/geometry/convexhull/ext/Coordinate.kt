package org.geometry.convexhull.ext


data class Coordinate(val x: Int, val y: Int)

enum class Orientation {
    COLLINEAR, CLOCKWISE, COUNTERCLOCKWISE;

    companion object {
        fun getOrientation(p: Coordinate, q: Coordinate, r: Coordinate): Orientation {
            val p: Int = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y)
            if (p == 0) return COLLINEAR
            return if (p > 0) CLOCKWISE else COUNTERCLOCKWISE
        }
    }
}