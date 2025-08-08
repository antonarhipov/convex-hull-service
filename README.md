# Convex Hull Service

A Spring Boot service that provides various algorithms for computing the convex hull of a set of 2D points.

## Overview

This service implements multiple algorithms for finding the convex hull of a set of points in a 2D plane. The convex hull is the smallest convex polygon that contains all the points in a given set. It's like stretching a rubber band around the points and letting it snap into place.

## Technologies Used

- Kotlin 1.9.25
- Java 17
- Spring Boot 3.4.3
- Spring Web (REST API)
- Spring Data JDBC
- PostgreSQL (runtime)
- Testcontainers (tests)
- JUnit 5 for testing
- Gradle for build management

## Available Algorithms

The service implements the following convex hull algorithms:

1. **Andrew's Monotone Chain** - O(n log n) time complexity
   - Sorts points lexicographically and builds the hull in two passes (lower and upper)

2. **Graham Scan** - O(n log n) time complexity
   - Sorts points by polar angle with respect to the lowest point and builds the hull using a stack

3. **Jarvis March (Gift Wrapping)** - O(n*h) time complexity
   - Starts with the leftmost point and wraps around the points counterclockwise
   - Where n is the number of points and h is the number of points on the hull

4. **Quickhull** - O(n log n) average case, O(n²) worst case
   - Divides the problem into subproblems using a divide-and-conquer approach
   - Similar to the Quicksort algorithm

5. **Legacy Algorithm** - A Java implementation of the Jarvis March algorithm

## API Endpoints

### POST /ds

Computes the convex hull of a set of points using the legacy algorithm.

**Request Body:**
```json
[
  {"x": 0, "y": 0},
  {"x": 1, "y": 1},
  {"x": 2, "y": 2},
  {"x": 0, "y": 2},
  {"x": 2, "y": 0}
]
```

**Response:**
```json
[
  {"x": 0, "y": 0},
  {"x": 2, "y": 0},
  {"x": 2, "y": 2},
  {"x": 0, "y": 2}
]
```

## Building and Running

### Prerequisites

- JDK 17 or higher
- Gradle 7.6 or higher (or use the included Gradle wrapper)

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

The service will start on port 8080 by default.

## Web UI

A simple UI is available at http://localhost:8080/ (served from `src/main/resources/static/index.html`).

- Paste an array of points in JSON format into the textarea. Example:

```json
[
  {"x": 0, "y": 0},
  {"x": 1, "y": 1},
  {"x": 2, "y": 0},
  {"x": 0, "y": 2}
]
```

- Click "Compute hull (POST /ds)" to send the request to `/ds` and visualize the result.
- Use "Load example" or the "Random" button (with adjustable count) to generate inputs quickly.

## Testing

Run the tests with:

```bash
./gradlew test
```

## Project Structure

- `src/main/kotlin/org/geometry/convexhull/`
  - `Application.kt` - Spring Boot application entry point
  - `AlgorithmController.kt` - REST controller for the API endpoints
  - `ext/` - Contains the convex hull algorithm implementations
    - `Algorithm.kt` - Interface for all convex hull algorithms
    - `Coordinate.kt` - Data class for representing 2D points
    - `Orientation.kt` - Enum and utility for determining point orientations
    - `AndrewsMonotoneChain.kt` - Implementation of Andrew's Monotone Chain algorithm
    - `GrahamScan.kt` - Implementation of Graham Scan algorithm
    - `JarvisMarch.kt` - Implementation of Jarvis March algorithm
    - `Quickhull.kt` - Implementation of Quickhull algorithm
- `src/main/java/org/geometry/math/`
  - `D.java` - Java class for representing 2D points (legacy)
  - `TheAlgorithm.java` - Legacy implementation of convex hull algorithm

## Examples

### Using cURL

```bash
curl -X POST http://localhost:8080/ds \
  -H "Content-Type: application/json" \
  -d '[{"x":0,"y":0},{"x":1,"y":1},{"x":2,"y":2},{"x":0,"y":2},{"x":2,"y":0}]'
```

## License

[Add your license information here]

## Contributing

[Add contribution guidelines here]