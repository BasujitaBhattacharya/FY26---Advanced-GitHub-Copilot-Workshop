---
name: "Code Review"
description: "Performs a structured code review on selected C# code following team conventions"
---

# Code Review

Please review the following C# code and provide structured feedback.

Evaluate against these criteria:

## 1. Correctness
- Are there any logic errors or edge cases not handled?
- Is null handled correctly (nullable reference types enabled)?
- Are async operations used correctly (no `.Result` or `.Wait()`)?

## 2. Code Style
- Does it follow PascalCase / camelCase / `_camelCase` naming conventions?
- Are expression-bodied members used where appropriate?
- Is `var` used only when the type is obvious?

## 3. Security
- Are there any hard-coded secrets, connection strings, or sensitive values?
- Is user input validated before use?
- Is `IHttpClientFactory` used instead of `new HttpClient()`?

## 4. Error Handling
- Are exceptions caught at the right level?
- Is logging present before re-throwing?
- Are guard clauses using `ArgumentNullException.ThrowIfNull()`?

## 5. Testability
- Are dependencies injected via constructor?
- Are concrete types used where interfaces should be?

## Code to Review

```csharp
${selection}
```

Format your response as:
- **Summary** (1-2 sentences overall verdict)
- **Issues** (numbered list, severity: Critical / Major / Minor)
- **Suggestions** (concrete code snippets for key fixes)
