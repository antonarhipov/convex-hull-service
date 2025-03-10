package org.geometry.convexhull.ext

import org.geometry.convexhull.ext.Algorithm.Companion.MINIMUM_POINTS_FOR_HULL

/**
 * Implementation of the Jarvis march (Gift wrapping) algorithm for finding the convex hull.
 * Time complexity: O(n*h) where n is the number of points and h is the number of points on the hull.
 */
class JarvisMarch : Algorithm {
    override fun run(coordinates: List<Coordinate>): List<Coordinate> {
        // Need at least 3 points to form a convex hull
        if (coordinates.size < MINIMUM_POINTS_FOR_HULL) {
            return emptyList()
        }

        val result = mutableListOf<Coordinate>()
        
        // Find the leftmost point
        var leftmostPointIndex = 0
        for (i in 1 until coordinates.size) {
            if (coordinates[i].x < coordinates[leftmostPointIndex].x) {
                leftmostPointIndex = i
            }
        }
        
        // Start from leftmost point and keep wrapping counterclockwise
        var currentPointIndex = leftmostPointIndex
        do {
            // Add current point to result
            result.add(coordinates[currentPointIndex])
            
            // Find the next point
            var nextPointIndex = (currentPointIndex + 1) % coordinates.size
            
            for (i in coordinates.indices) {
                // If point i is more counterclockwise than current next point, update next point
                val orientation = Orientation.getOrientation(
                    coordinates[currentPointIndex], 
                    coordinates[i], 
                    coordinates[nextPointIndex]
                )
                
                if (orientation == Orientation.COUNTERCLOCKWISE) {
                    nextPointIndex = i
                }
            }
            
            currentPointIndex = nextPointIndex
            
        } while (currentPointIndex != leftmostPointIndex) // Continue until we reach the start point
        
        return result
    }
}