---
name: "C# / ASP.NET Core Standards"
description: "Coding conventions for all C# source files in this repo"
applyTo: "**/*.cs"
---

# C# / ASP.NET Core Standards

## Naming

- Classes, interfaces, methods, properties: `PascalCase`
- Local variables, parameters: `camelCase`
- Private fields: `_camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Async methods: suffix with `Async`

## Code Style

- Use `var` only when the type is obvious from the right-hand side
- Use `record` types for immutable DTOs
- Prefer pattern matching over `as` casts
- Always `async`/`await` — never `.Result` or `.Wait()`

## XML Documentation

All `public` and `protected` members need XML doc comments.

## Security

- No hard-coded connection strings or secrets
- Use `ArgumentNullException.ThrowIfNull()` for guard clauses
- Use `IHttpClientFactory` — never `new HttpClient()`
