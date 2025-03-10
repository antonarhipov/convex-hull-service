package org.geometry.convexhull.ext

import org.geometry.convexhull.ext.Algorithm.Companion.MINIMUM_POINTS_FOR_HULL
import java.util.*

/**
 * Implementation of the Andrew's Monotone Chain algorithm for finding the convex hull.
 * Time complexity: O(n log n) where n is the number of points.
 */
class AndrewsMonotoneChain : Algorithm {
    override fun run(coordinates: List<Coordinate>): List<Coordinate> {
        // Need at least 3 points to form a convex hull
        if (coordinates.size < MINIMUM_POINTS_FOR_HULL) {
            return emptyList()
        }

        // Sort points lexicographically (first by x, then by y)
        val sortedPoints = coordinates.sortedWith(compareBy({ it.x }, { it.y }))
        
        val lowerHull = ArrayList<Coordinate>()
        val upperHull = ArrayList<Coordinate>()
        
        // Build lower hull (from left to right)
        for (point in sortedPoints) {
            while (lowerHull.size >= 2 && 
                   Orientation.getOrientation(
                       lowerHull[lowerHull.size - 2], 
                       lowerHull[lowerHull.size - 1], 
                       point
                   ) != Orientation.COUNTERCLOCKWISE) {
                lowerHull.removeAt(lowerHull.size - 1)
            }
            lowerHull.add(point)
        }
        
        // Build upper hull (from right to left)
        for (i in sortedPoints.size - 1 downTo 0) {
            val point = sortedPoints[i]
            while (upperHull.size >= 2 && 
                   Orientation.getOrientation(
                       upperHull[upperHull.size - 2], 
                       upperHull[upperHull.size - 1], 
                       point
                   ) != Orientation.COUNTERCLOCKWISE) {
                upperHull.removeAt(upperHull.size - 1)
            }
            upperHull.add(point)
        }
        
        // Remove the last point of each hull (as it's the same as the first point of the other hull)
        if (lowerHull.isNotEmpty()) lowerHull.removeAt(lowerHull.size - 1)
        if (upperHull.isNotEmpty()) upperHull.removeAt(upperHull.size - 1)
        
        // Combine the lower and upper hulls to form the complete convex hull
        val convexHull = ArrayList<Coordinate>()
        convexHull.addAll(lowerHull)
        convexHull.addAll(upperHull)
        
        return convexHull
    }
}