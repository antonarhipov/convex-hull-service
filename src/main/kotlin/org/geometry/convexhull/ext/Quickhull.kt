package org.geometry.convexhull.ext

/**
 * Implementation of the Quickhull algorithm for finding the convex hull.
 * Time complexity: O(n log n) on average, O(n²) in the worst case.
 */
class QuickhullAlgorithm : Algorithm {
    override fun run(coordinates: List<Coordinate>): List<Coordinate> {
        // Need at least 3 points to form a convex hull
        if (coordinates.size < MINIMUM_POINTS_FOR_HULL) {
            return emptyList()
        }

        // Find the points with minimum and maximum x-coordinates
        var minPoint = coordinates[0]
        var maxPoint = coordinates[0]
        
        for (point in coordinates) {
            if (point.x < minPoint.x) {
                minPoint = point
            }
            if (point.x > maxPoint.x) {
                maxPoint = point
            }
        }
        
        // Initialize the result with the min and max points
        val convexHull = mutableSetOf(minPoint, maxPoint)
        
        // Divide the points into two subsets: points above and below the line
        val pointsAboveLine = mutableListOf<Coordinate>()
        val pointsBelowLine = mutableListOf<Coordinate>()
        
        for (point in coordinates) {
            if (point == minPoint || point == maxPoint) continue
            
            val orientation = Orientation.getOrientation(minPoint, maxPoint, point)
            
            when (orientation) {
                Orientation.COUNTERCLOCKWISE -> pointsAboveLine.add(point)
                Orientation.CLOCKWISE -> pointsBelowLine.add(point)
                Orientation.COLLINEAR -> {
                    // For collinear points, we can skip them as they won't be part of the convex hull
                    // unless they are the extreme points (which we've already added)
                }
            }
        }
        
        // Recursively find the convex hull on each side
        findHull(pointsAboveLine, minPoint, maxPoint, convexHull)
        findHull(pointsBelowLine, maxPoint, minPoint, convexHull)
        
        return convexHull.toList()
    }
    
    /**
     * Recursive function to find the convex hull.
     * 
     * @param points The set of points to process
     * @param p1 The first point of the line segment
     * @param p2 The second point of the line segment
     * @param convexHull The set to store the convex hull points
     */
    private fun findHull(
        points: List<Coordinate>,
        p1: Coordinate,
        p2: Coordinate,
        convexHull: MutableSet<Coordinate>
    ) {
        if (points.isEmpty()) return
        
        // Find the point with the maximum distance from the line segment p1-p2
        var maxDistance = 0
        var farthestPoint: Coordinate? = null
        
        for (point in points) {
            val distance = distanceFromLine(point, p1, p2)
            if (distance > maxDistance) {
                maxDistance = distance
                farthestPoint = point
            }
        }
        
        // If no point was found, return
        if (farthestPoint == null) return
        
        // Add the farthest point to the convex hull
        convexHull.add(farthestPoint)
        
        // Divide the remaining points into two subsets
        val pointsOutsideTriangle1 = mutableListOf<Coordinate>()
        val pointsOutsideTriangle2 = mutableListOf<Coordinate>()
        
        for (point in points) {
            if (point == farthestPoint) continue
            
            val orientation1 = Orientation.getOrientation(p1, farthestPoint, point)
            val orientation2 = Orientation.getOrientation(farthestPoint, p2, point)
            
            // If the point is outside the triangle formed by p1, farthestPoint, and p2
            if (orientation1 == Orientation.COUNTERCLOCKWISE) {
                pointsOutsideTriangle1.add(point)
            } else if (orientation2 == Orientation.COUNTERCLOCKWISE) {
                pointsOutsideTriangle2.add(point)
            }
        }
        
        // Recursively process the two subsets
        findHull(pointsOutsideTriangle1, p1, farthestPoint, convexHull)
        findHull(pointsOutsideTriangle2, farthestPoint, p2, convexHull)
    }
    
    /**
     * Calculate the perpendicular distance from a point to a line segment.
     * 
     * @param point The point
     * @param lineStart The start point of the line segment
     * @param lineEnd The end point of the line segment
     * @return The perpendicular distance
     */
    private fun distanceFromLine(point: Coordinate, lineStart: Coordinate, lineEnd: Coordinate): Int {
        return Math.abs(
            (lineEnd.y - lineStart.y) * point.x - 
            (lineEnd.x - lineStart.x) * point.y + 
            lineEnd.x * lineStart.y - 
            lineEnd.y * lineStart.x
        )
    }
}