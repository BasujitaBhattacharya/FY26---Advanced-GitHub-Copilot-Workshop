---
name: "Explain Legacy Code"
description: "Explains legacy .NET Framework code and suggests .NET 8 equivalents"
---

# Explain Legacy Code

Analyse the following legacy .NET Framework code and provide:

## 1. Plain-English Explanation
Explain what this code does in 2–4 sentences, as if explaining to a developer who didn't write it.

## 2. Legacy Patterns Identified
List any deprecated, obsolete, or .NET-Framework-specific patterns. For each, note:
- **Pattern**: what it is
- **Why it's legacy**: brief reason
- **Modern equivalent**: .NET 8 replacement

Common patterns to look for:
- `HttpClient` instantiated directly → `IHttpClientFactory`
- `ConfigurationManager.AppSettings` → `IConfiguration`
- `System.Web.*` imports → ASP.NET Core equivalents
- Synchronous I/O (`.Result`, `.Wait()`) → `async`/`await`
- `WebClient` → `HttpClient` via factory
- `Thread.Sleep` → `Task.Delay`
- XML-based config (`Web.config`) → `appsettings.json`

## 3. Modernised Version
Provide a rewritten version using .NET 8 / C# 12 idioms, with inline `// MODERNIZED:` comments on each changed line.

## Code to Analyse

```csharp
${selection}
```
