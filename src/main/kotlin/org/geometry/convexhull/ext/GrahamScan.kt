package org.geometry.convexhull.ext

import org.geometry.convexhull.ext.Algorithm.Companion.MINIMUM_POINTS_FOR_HULL
import java.util.*

/**
 * Implementation of the Graham scan algorithm for finding the convex hull.
 * Time complexity: O(n log n) where n is the number of points.
 */
class GrahamScan : Algorithm {
    override fun run(coordinates: List<Coordinate>): List<Coordinate> {
        // Need at least 3 points to form a convex hull
        if (coordinates.size < MINIMUM_POINTS_FOR_HULL) {
            return emptyList()
        }

        // Create a copy of the input list to avoid modifying the original
        val points = coordinates.toMutableList()
        
        // Find the point with the lowest y-coordinate (and leftmost if tied)
        var lowestPoint = points[0]
        for (i in 1 until points.size) {
            val point = points[i]
            if (point.y < lowestPoint.y || (point.y == lowestPoint.y && point.x < lowestPoint.x)) {
                lowestPoint = point
            }
        }
        
        // Move the lowest point to the beginning of the list
        points.remove(lowestPoint)
        points.add(0, lowestPoint)
        
        // Sort the remaining points by polar angle with respect to the lowest point
        val pivot = points[0]
        points.subList(1, points.size).sortWith(Comparator { a, b ->
            val orientationValue = Orientation.getOrientation(pivot, a, b)
            when (orientationValue) {
                Orientation.COLLINEAR -> {
                    // If collinear, sort by distance from pivot
                    val distA = squaredDistance(pivot, a)
                    val distB = squaredDistance(pivot, b)
                    distA.compareTo(distB)
                }
                Orientation.COUNTERCLOCKWISE -> -1
                Orientation.CLOCKWISE -> 1
            }
        })
        
        // Remove collinear points with the same angle (keep only the farthest)
        var i = 1
        while (i < points.size - 1) {
            val orientation = Orientation.getOrientation(pivot, points[i], points[i + 1])
            if (orientation == Orientation.COLLINEAR) {
                // Remove the closer point
                if (squaredDistance(pivot, points[i]) < squaredDistance(pivot, points[i + 1])) {
                    points.removeAt(i)
                } else {
                    points.removeAt(i + 1)
                }
            } else {
                i++
            }
        }
        
        // If we have fewer than 3 points after removing collinear points, return all points
        if (points.size < 3) {
            return points
        }
        
        // Build the convex hull using a stack
        val hull = Stack<Coordinate>()
        hull.push(points[0])
        hull.push(points[1])
        
        for (j in 2 until points.size) {
            // Remove points that make a non-left turn
            while (hull.size > 1 && 
                   Orientation.getOrientation(hull[hull.size - 2], hull[hull.size - 1], points[j]) != Orientation.COUNTERCLOCKWISE) {
                hull.pop()
            }
            hull.push(points[j])
        }
        
        return hull.toList()
    }
    
    // Helper function to calculate squared distance between two points
    private fun squaredDistance(a: Coordinate, b: Coordinate): Int {
        val dx = b.x - a.x
        val dy = b.y - a.y
        return dx * dx + dy * dy
    }
}