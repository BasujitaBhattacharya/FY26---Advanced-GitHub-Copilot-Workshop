---
name: "Modernize .NET"
description: "Guides incremental migration from .NET Framework to .NET 8"
---

# .NET Modernization Guide

Analyse the selected code and guide me through an **incremental .NET Framework → .NET 8 migration**.

## Modernization Principles

- **Incremental over big-bang** — suggest the smallest safe change first
- **Preserve behaviour** — the refactored code must do the same thing
- **Flag breaking changes** — call out anything that changes the public API or serialization behaviour
- **One concern at a time** — separate: async, DI, config, HTTP, logging into distinct steps

## Selected Code

```csharp
${selection}
```

## What I Need

1. **Assessment**: What .NET Framework dependencies does this code have? Rate migration complexity: Low / Medium / High
2. **Step-by-step migration plan**: Ordered list of changes, from safest to most invasive
3. **Step 1 implementation**: Show me the first change with a before/after diff
4. **Copilot hints**: Suggest 2–3 follow-up prompts I can use to continue the migration

## Target Stack

- Runtime: **.NET 8**
- DI: `Microsoft.Extensions.DependencyInjection`
- Config: `Microsoft.Extensions.Configuration` + `appsettings.json`
- Logging: `Microsoft.Extensions.Logging` (`ILogger<T>`)
- HTTP: `IHttpClientFactory`
- Async: `async`/`await` throughout
