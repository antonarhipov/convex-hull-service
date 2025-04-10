# Convex Hull Service Improvement Plan

This improvement plan is generated based on the requirements listed in requirements.md file.

## Overview
This document outlines the plan for improving the Convex Hull Service based on the requirements specified in `docs/requirements.md`. The plan addresses three main requirements:

1. Replace the legacy Java implementation with a Kotlin implementation
2. Add persistence support for input points and results
3. Create a UI to visualize the algorithm

## Current State Analysis
The current implementation has several issues:

- Uses a legacy Java implementation of the convex hull algorithm with poor naming and documentation
- Uses mutable data structures (the `D` class)
- Lacks proper error handling
- Has no persistence layer
- Has no UI for visualization
- Has a discrepancy between the actual implementation and the test expectations

## Detailed Improvement Plan

### 1. Replace Legacy Java Implementation with Kotlin

#### 1.1 Create Point Data Class
- Create an immutable `Point` data class in Kotlin to replace the mutable `D` class
- Implement proper `equals()`, `hashCode()`, and `toString()` methods via Kotlin's data class

#### 1.2 Implement Convex Hull Algorithm in Kotlin
- Create a `ConvexHullAlgorithm` interface with a `compute()` method
- Implement the Jarvis March algorithm (currently in `TheAlgorithm.java`) as a Kotlin implementation of this interface
- Add proper documentation explaining the algorithm
- Implement proper error handling (return empty list instead of null for invalid inputs)
- Follow Kotlin style guidelines:
  - Use immutable data structures
  - Use functional programming where appropriate
  - Use extension functions for better readability

#### 1.3 Create Algorithm Service
- Implement `ConvexHullService` as expected by the tests
- Support multiple algorithm implementations via the `AlgorithmType` enum
- Implement the `getDefaultAlgorithmType()` method

#### 1.4 Update Controller
- Update `AlgorithmController` to use the new service
- Implement proper request/response DTOs as shown in the tests
- Add proper error handling and validation
- Replace direct println statements with proper logging

### 2. Add Persistence Support

#### 2.1 Create Domain Model
- Create entities for storing computation requests and results
- Design a schema that captures:
  - Input points
  - Result points (convex hull)
  - Timestamp
  - Algorithm used
  - Computation time

#### 2.2 Implement Repository Layer
- Create Spring Data repositories for the entities
- Implement methods for saving and retrieving computation data

#### 2.3 Update Service Layer
- Modify `ConvexHullService` to persist computation requests and results
- Add methods for retrieving historical computations

#### 2.4 Extend API
- Add endpoints for retrieving historical computations
- Add filtering and pagination support for the history endpoints

### 3. Create UI for Visualization

#### 3.1 Design UI Components
- Create a simple web UI using HTML, CSS, and JavaScript
- Design components for:
  - Input form for points
  - Visualization of points and convex hull
  - History of computations

#### 3.2 Implement Point Input
- Allow manual input of points
- Support random point generation
- Support uploading points from a file

#### 3.3 Implement Visualization
- Use a JavaScript library (e.g., D3.js or Chart.js) for visualization
- Display input points and convex hull
- Add animation to show the algorithm steps

#### 3.4 Implement History View
- Display a list of previous computations
- Allow selecting a computation to visualize
- Add filtering and sorting options

#### 3.5 Connect UI to Backend
- Implement API calls to the backend services
- Handle error cases and loading states

## Implementation Approach

### Phase 1: Kotlin Implementation
- Replace Java implementation with Kotlin
- Update controller and service layer
- Ensure all tests pass

### Phase 2: Persistence
- Implement domain model and repositories
- Update service to persist data
- Add history endpoints
- Add tests for new functionality

### Phase 3: UI
- Implement basic UI structure
- Add visualization components
- Connect to backend API
- Add history view
- Test end-to-end functionality

## Technical Considerations

### Code Quality
- Follow Kotlin style guidelines
- Use immutable data structures
- Write comprehensive tests
- Add proper documentation

### Performance
- Optimize algorithm implementation for large datasets
- Use appropriate indexing for database queries
- Implement pagination for large result sets

### Security
- Validate all input data
- Implement proper error handling
- Protect against common web vulnerabilities

## Timeline Estimate
- Phase 1 (Kotlin Implementation): 1 week
- Phase 2 (Persistence): 1 week
- Phase 3 (UI): 2 weeks
- Testing and refinement: 1 week

Total estimated time: 5 weeks