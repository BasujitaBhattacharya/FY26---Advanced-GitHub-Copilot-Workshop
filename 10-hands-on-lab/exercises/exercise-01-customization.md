# Exercise 01 — Customization: Repo Instructions + Prompt File

⏱ **Estimated time:** 15 minutes  
📚 **References:** [Module 01 — Customization](../../01-customization/README.md)

---

## Learning Objectives

By the end of this exercise you will:

1. Understand how `.github/copilot-instructions.md` fires on every Copilot request in this repo
2. Observe how file-based instructions (`.instructions.md`) narrow context to specific file types
3. Create a new prompt file (`.prompt.md`) and invoke it from Copilot Chat
4. Verify that Copilot follows your instructions when generating C# code

---

## Part A — Observe the Always-On Instructions (5 min)

### Step 1 — Open the instructions file

Open [.github/copilot-instructions.md](../../.github/copilot-instructions.md).

Spend 2 minutes reading it. Note the rules that cover:
- Naming conventions (private fields, async methods)
- `ArgumentNullException.ThrowIfNull()` guard clauses
- `IHttpClientFactory` over `new HttpClient()`

### Step 2 — Test that instructions fire

Open Copilot Chat (`Ctrl+Alt+I`), then ask:

```
Write a C# service class called RegionService with a constructor that accepts
IRegionRepository and ILogger<RegionService>. Add a method GetActiveRegionsAsync
that returns IReadOnlyList<Region>.
```

**Expected:** Copilot should:
- Use `_camelCase` for private fields
- Call `ArgumentNullException.ThrowIfNull()` in the constructor (not null checks)
- Name the async method with `Async` suffix
- Use XML doc comments on the public class and method

**If Copilot doesn't follow the rules:** Check that `github.copilot.chat.codeGeneration.useInstructionFiles` is `true` in `.vscode/settings.json`.

---

## Part B — File-Based Instructions (5 min)

### Step 3 — Open the C# standards instruction file

Open [.github/instructions/csharp-standards.instructions.md](../../.github/instructions/csharp-standards.instructions.md).

Note the `applyTo: **/*.cs` frontmatter — this file only fires when you're working in a `.cs` file.

### Step 4 — Verify scoping

1. Open a non-C# file (e.g., `README.md`)
2. Ask Copilot: *"What are the naming rules for this project?"*
3. Copilot will answer generically (no project-specific rules)

4. Now open [06-qa-testing/csharp-unit-tests/PermitService.cs](../../06-qa-testing/csharp-unit-tests/PermitService.cs)
5. Ask the same question
6. Copilot should now reference the project's `_camelCase` and `PascalCase` conventions

**Reflection:** Why is scoping instructions to file types useful for a mixed-language codebase?

---

## Part C — Create a New Prompt File (5 min)

### Step 5 — Create a new prompt for generating API controllers

Create a new file at `.github/prompts/generate-controller.prompt.md`:

```markdown
---
mode: agent
description: Generate an ASP.NET Core controller for a given service interface
---

# Generate ASP.NET Core Controller

Generate a complete ASP.NET Core 8 minimal-style `[ApiController]` class for the
service interface in `${selection}`.

Requirements:
- Constructor injection of the service interface
- Constructor injection of `ILogger<T>`
- All service methods exposed as REST endpoints with appropriate HTTP methods:
  - `Get*` → `[HttpGet]`
  - `Submit*` or `Create*` → `[HttpPost]` returning `CreatedAtAction`
  - `Cancel*` or `Delete*` → `[HttpDelete]`
- `ActionResult<T>` return types with correct status codes
- `ArgumentNullException.ThrowIfNull()` guard in constructor
- XML doc comments on the class and each action
- Route: `[Route("api/[controller]")]`

Do not include business logic in the controller — delegate everything to the service.
```

### Step 6 — Use the prompt file

1. Open [06-qa-testing/csharp-unit-tests/PermitService.cs](../../06-qa-testing/csharp-unit-tests/PermitService.cs)
2. Select the entire file or just the interface definition
3. Open Copilot Chat → type `/generate-controller`
4. Observe the generated controller

**Check:** Does the controller follow all the rules from `copilot-instructions.md`?

---

## ✅ Exercise Complete

You have:
- Observed always-on instructions firing in Copilot responses
- Verified file-scoped instructions with `applyTo`
- Created and used a custom prompt file

**Proceed to:** [Exercise 02 → Background Agent](exercise-02-background-agent.md)
