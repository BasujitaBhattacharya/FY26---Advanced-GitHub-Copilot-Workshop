# Module 05 — App Modernization

[![Module](https://img.shields.io/badge/Module-05_App_Modernization-0078d4?logo=github&logoColor=white)](.)
[![Estimated Time](https://img.shields.io/badge/Time-45_min-informational)](.)

> **What you'll learn:** How to use the **.NET Upgrade Assistant** and **Java Modernization Assistant** with GitHub Copilot to incrementally migrate legacy applications. Includes a real legacy .NET Framework 4.7 Web API sample designed to trigger modernization suggestions.

---

## Tooling Comparison

| | .NET Upgrade Assistant | Java Modernization Assistant |
|-|----------------------|------------------------------|
| **Primary IDE** | VS Code, Visual Studio 2022 | VS Code, IntelliJ IDEA |
| **Migration path** | .NET Framework → .NET 8 | Java 8/11 → Java 17/21 |
| **Approach** | AST analysis + incremental project upgrades | AST analysis + code pattern rewrites |
| **Copilot integration** | Suggests code fixes for flagged issues | Suggests modern Java idioms |
| **Output** | Modified project files + solution | Modified source files |
| **VS Code extension** | `ms-dotnettools.vscode-dotnet-pack` | `redhat.java` + Copilot |

---

## .NET Upgrade Assistant Workflow

```mermaid
flowchart TD
    START["Legacy .NET Framework Project"]
    SCAN["Upgrade Assistant scans project\nIdentifies: deprecated APIs, incompatible packages,\nsystem.web references, sync patterns"]
    REPORT["Generates upgrade report\nLists issues by severity"]
    STEP1["Step 1: Upgrade project file\n(.csproj format → SDK-style)"]
    STEP2["Step 2: Replace incompatible packages\n(e.g. System.Web → Microsoft.AspNetCore)"]
    STEP3["Step 3: Fix deprecated APIs\n(ConfigurationManager → IConfiguration)"]
    STEP4["Step 4: Fix async patterns\n(sync → async/await)"]
    COPILOT["GitHub Copilot assists:\nexplains each change,\ngenerates replacement code,\nfixes compilation errors"]
    BUILD["dotnet build — verify no errors"]
    TEST["dotnet test — verify behaviour unchanged"]
    DONE["✅ Running on .NET 8"]

    START --> SCAN --> REPORT
    REPORT --> STEP1 --> STEP2 --> STEP3 --> STEP4
    STEP1 & STEP2 & STEP3 & STEP4 --> COPILOT
    COPILOT --> BUILD --> TEST --> DONE
```

---

## .NET Migration Timeline

```mermaid
timeline
    title .NET Framework → .NET 8 Migration Path
    .NET Framework 4.7.2 : Legacy start point
        : System.Web, ConfigurationManager
        : Sync HTTP, XML config
    .NET Core 3.1 : First cross-platform step
        : ASP.NET Core
        : appsettings.json
    .NET 5 / 6 : Unified platform
        : Minimal APIs
        : ILogger<T>
    .NET 8 LTS : Target
        : Record types, C# 12
        : IHttpClientFactory
        : Built-in DI
```

---

## Contents

| Doc / Folder | What it covers |
|-------------|---------------|
| [docs/dotnet-modernization.md](docs/dotnet-modernization.md) | .NET Upgrade Assistant: install, usage, incremental flags |
| [docs/java-modernization.md](docs/java-modernization.md) | Java Modernization Assistant: install, usage, reference |
| [dotnet-sample/](dotnet-sample/) | **Legacy .NET Framework 4.7 Web API** — open this in VS Code and run the Upgrade Assistant |
| [java-sample/](java-sample/) | Legacy Java 8 Servlet app — reference comparison |

---

## How Copilot + Upgrade Assistant Collaborate

```mermaid
sequenceDiagram
    participant Dev as Developer
    participant UA as .NET Upgrade Assistant
    participant Copilot as GitHub Copilot

    Dev->>UA: Run upgrade on dotnet-sample/
    UA->>Dev: Flags: ConfigurationManager, sync HttpClient, System.Web
    Dev->>Copilot: "Explain why ConfigurationManager is flagged"
    Copilot-->>Dev: "ConfigurationManager is .NET Framework only —\nuse IConfiguration in .NET 8"
    Dev->>Copilot: "Show me how to replace it in PermitRepository.cs"
    Copilot-->>Dev: Generates IConfiguration injection pattern
    Dev->>UA: Accept suggestion, move to next issue
    UA->>Dev: Flags: synchronous HttpClient usage
    Dev->>Copilot: [uses modernize-dotnet.prompt.md on selected code]
    Copilot-->>Dev: Step-by-step migration + code diff
```

---

## Quick Start

```bash
# .NET sample — open in VS Code and run Upgrade Assistant
cd 05-app-modernization/dotnet-sample

# Install .NET Upgrade Assistant (if not already installed)
dotnet tool install --global upgrade-assistant

# Run upgrade analysis (read-only report, no changes applied)
upgrade-assistant analyze PermitApi.csproj

# Then build to confirm starting state
dotnet build
```

For the Java sample, open `java-sample/` in VS Code with the **Java Extension Pack** (`vscjava.vscode-java-pack`) installed — Copilot will automatically offer modernization suggestions as you browse the code.

---

## Related Modules

- [Module 01 — Customization](../01-customization/README.md) — prompt files for modernization tasks
- [Module 06 — QA & Testing](../06-qa-testing/README.md) — verify migrated code with generated tests
- [Module 10 — Lab Exercise 04](../10-hands-on-lab/exercises/exercise-04-modernization.md)
