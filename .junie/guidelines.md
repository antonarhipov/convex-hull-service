# Kotlin Style Guidelines

This document outlines the preferred coding style for Kotlin projects.

## General Principles

- **Prefer functional style** over imperative approaches when appropriate
- **Use immutable data structures** (`val`, immutable collections) by default
- **Use extension functions liberally** to enhance existing classes without inheritance
- Follow Kotlin's official [coding conventions](https://kotlinlang.org/docs/coding-conventions.html)

## Task Management

- Use the task list in `docs/tasks.md` to track project progress
- Tasks are organized hierarchically and numbered for easy reference (e.g., 1.1.1, 1.1.2)
- Mark tasks as complete by changing `[ ]` to `[x]` when the task is done
- Follow the phased implementation approach outlined in the improvement plan
- Regularly update the task list to reflect current progress
- Add new tasks as needed, following the established numbering convention
- When implementing a task, reference its number in commit messages

## Specific Guidelines

### Adding new libraries

*** VERY IMPORTANT*** Use MCP tools
Check the up-to-date documentation for the library you are adding to the project.

### Functional Style

- Use higher-order functions (`map`, `filter`, `fold`, etc.) instead of loops when appropriate
- Prefer expressions to statements
- Use lambda expressions for short, simple operations
- Consider using the sequence API for large collections to improve performance

### Immutability

- Use `val` instead of `var` whenever possible
- Use immutable collection types (`listOf`, `setOf`, `mapOf`) by default
- Use `copy()` with data classes to create modified instances
- Consider using sealed classes for representing state

### Extension Functions

- Create extension functions to add functionality to existing classes
- Use extension functions to improve readability and create domain-specific language
- Group related extension functions in separate files by functionality
- Consider creating extension properties when appropriate

### Code Organization

- Keep functions small and focused on a single responsibility
- Use meaningful names that reflect purpose
- Organize code by feature rather than by type
- Limit line length to 100-120 characters

### Error Handling

- Use nullable types and safe calls (`?.`) instead of null checks
- Prefer exceptions for exceptional conditions, not for control flow
- Consider using `Result` type for operations that can fail
