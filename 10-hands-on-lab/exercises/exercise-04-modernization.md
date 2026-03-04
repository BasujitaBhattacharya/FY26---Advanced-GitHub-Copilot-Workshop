# Exercise 04 — App Modernization: .NET Upgrade Assistant

⏱ **Estimated time:** 15 minutes
📚 **References:** [Module 05 — .NET Modernization](../../05-app-modernization/docs/dotnet-modernization.md)

---

## Learning Objectives

By the end of this exercise you will:

1. Install and run the .NET Upgrade Assistant VS Code extension
2. Interpret the upgrade analysis report on a legacy Web API 2 project
3. Accept targeted Copilot suggestions to modernise specific code patterns

---

## Part A — Install .NET Upgrade Assistant (3 min)

### Step 1 — Install the extension

1. Open the **Extensions** panel (`Ctrl+Shift+X`)
2. Search for **".NET Upgrade Assistant"**
3. Install the extension published by **Microsoft**
4. Reload VS Code when prompted

### Step 2 — Verify the extension is active

Open the Command Palette (`Ctrl+Shift+P`) and type:

```
.NET Upgrade Assistant
```

You should see entries for **"Upgrade .NET Project"** and **"Show Upgrade Summary"**.

---

## Part B — Analyse the Legacy Sample (5 min)

### Step 3 — Open the legacy project

In the Explorer panel, navigate to:

```
05-app-modernization/dotnet-sample/
```

This is a simulated Web API 2 (.NET Framework) project. Key files to review before the upgrade:

| File | Legacy Pattern |
|---|---|
| `App_Start/WebApiConfig.cs` | `HttpConfiguration` (Web API 2) |
| `Controllers/PermitController.cs` | `ApiController` base class |
| `Repositories/PermitRepository.cs` | `SqlConnection` (no async) |
| `Web.config` | XML config instead of `appsettings.json` |

### Step 4 — Run the upgrade analysis

1. Right-click `05-app-modernization/dotnet-sample/` in Explorer
2. Select **.NET Upgrade Assistant → Upgrade Project**
3. Choose **"Analyse"** (not "Upgrade") when prompted for mode
4. Wait for the report to generate (10–30 seconds)

### Step 5 — Review the report

The Upgrade Assistant will open a panel listing:

- **Breaking changes** — APIs removed in .NET 8
- **Compatibility issues** — NuGet packages with no .NET 8 equivalent
- **Recommended actions** — automatic migrations available

Record the number of each category:

| Category | Count in your report |
|---|---|
| Breaking changes | |
| Compatibility warnings | |
| Auto-fixable items | |

---

## Part C — Accept Two Copilot Suggestions (5 min)

### Step 6 — Fix `HttpConfiguration` → `WebApplication`

In the upgrade report, find the flagged `App_Start/WebApiConfig.cs`.

Open the file and use Copilot Chat in **Edit mode** (`Ctrl+I` on the file):

```
This is a .NET Framework Web API 2 startup class using HttpConfiguration.
Rewrite it as a .NET 8 minimal API startup using WebApplication.CreateBuilder
and keep the same route prefix /api.
```

Review the suggested diff. Accept it.

### Step 7 — Fix synchronous repository pattern

Open `Repositories/PermitRepository.cs` and ask Copilot:

```
This repository uses synchronous SqlConnection and SqlCommand.
Refactor all database calls to use async/await with SqlConnectionAsync,
OpenAsync, ExecuteReaderAsync patterns. Keep the same public method signatures
but suffix them with Async.
```

Review and accept the suggestion.

### Step 8 — Verify the project still builds

Open a terminal and run:

```powershell
cd "05-app-modernization/dotnet-sample"
dotnet build
```

> **Expected output:** `Build succeeded.`
> If there are errors, read the first error message and ask Copilot Chat to explain and fix it.

---

## Part D — Reflection (2 min)

Answer the following in the chat or a comment in the code:

1. Which of the two changes (startup config or async repositories) would you consider higher-risk in a real migration? Why?
2. What pattern from `copilot-instructions.md` did the refactored repository code follow? (Hint: look at async method naming.)
3. At what point in a real project should you run Upgrade Assistant — before writing new features or after?

---

## Troubleshooting

| Issue | Solution |
|---|---|
| "Upgrade Assistant" not found in Command Palette | Ensure the extension is installed and VS Code is reloaded |
| `dotnet build` fails with `TargetFramework` error | The sample targets `net48` — when prompted by the UA, change to `net8.0` in the `.csproj` |
| Copilot suggestion doesn't compile | Paste the error back into chat: "Fix this build error: [paste error]" |
| Extension shows "no supported project found" | Ensure you're running the command with a `.cs` or `.csproj` file selected |

---

## ✅ Exercise Complete

You have:
- Installed and run the .NET Upgrade Assistant on a legacy Web API 2 project
- Interpreted an upgrade analysis report
- Applied two targeted Copilot-assisted modernisation changes
- Verified the updated project builds successfully

**Proceed to:** [Exercise 05 → AI-Assisted Testing](exercise-05-testing.md)
