# Ask / Edit / Agent Mode

GitHub Copilot in VS Code offers three interaction modes. Choosing the right mode dramatically affects how useful Copilot is for a given task.

---

## Mode Comparison

```mermaid
flowchart TD
    TASK["What are you trying to do?"]

    TASK --> Q1{"Do you want to\nmake code changes?"}
    Q1 -->|"No — I want to\nlearn or explore"| ASK
    Q1 -->|Yes| Q2{"How many files\nare involved?"}
    Q2 -->|"1–2 files\nI know exactly what I want"| EDIT
    Q2 -->|"Multiple files\nor I want Copilot to plan it"| Q3{"Do you want\nCopilot to use tools\n(run terminal, search, etc.)?"}
    Q3 -->|No| EDIT2["Edit mode\n(multi-file)"]
    Q3 -->|Yes| AGENT

    ASK["💬 Ask Mode\nConversational\nNo edits made"]
    EDIT["✏️ Edit Mode\nDirect file edits\nYou review diffs"]
    AGENT["🤖 Agent Mode\nPlans + executes\nUses tools autonomously"]

    style ASK fill:#0078d4,color:#fff
    style EDIT fill:#107c10,color:#fff
    style EDIT2 fill:#107c10,color:#fff
    style AGENT fill:#881798,color:#fff
```

---

## Detailed Comparison

| Feature | Ask | Edit | Agent |
|---------|-----|------|-------|
| **Makes code changes** | ❌ | ✅ | ✅ |
| **Runs terminal commands** | ❌ | ❌ | ✅ |
| **Searches files** | ❌ | Limited | ✅ |
| **Multi-file edits** | ❌ | ✅ | ✅ |
| **Uses MCP tools** | ❌ | ❌ | ✅ |
| **Requires review/approval** | N/A | Each diff | Checkpoint prompts |
| **Best for** | Q&A, explain, plan | Targeted refactors | Complex multi-step tasks |
| **Instruction files applied** | ✅ | ✅ | ✅ |

---

## Ask Mode

The default conversational mode. Use it to:
- Ask questions about code, architecture, or APIs
- Understand what existing code does
- Get explanations of error messages
- Plan a refactoring approach before doing it
- Understand which Copilot customizations apply

**Example prompts:**
```
What does this PermitService class do?
What's the difference between IEnumerable and IReadOnlyList?
How should I structure a .NET 8 minimal API?
What coding conventions apply to this project?
```

---

## Edit Mode

Direct file editing mode. Copilot proposes changes as diffs that you accept or reject. Use it to:
- Add a method to an existing class
- Refactor a specific function
- Update a file to match new conventions
- Fix a known bug

**Example prompts:**
```
Add XML doc comments to all public methods in this file.
Refactor this method to use async/await.
Extract this logic into a separate class.
```

To open Edit mode: Chat panel → switch from "Ask" to "Edit" in the dropdown selector.

---

## Agent Mode

The most powerful mode. Copilot plans the task, then autonomously:
- Reads files across the workspace
- Makes changes to multiple files
- Runs terminal commands
- Calls MCP tools (when configured)
- Iterates until the task is done

Use it for:
- "Add logging to all controllers in this project"
- "Create a full CRUD API for the Permits entity with tests"
- "Migrate this controller from .NET Framework to .NET 8"
- Anything that would take you 10+ minutes of manual changes

**Agent mode respects all instruction files** — your `.github/copilot-instructions.md` is active throughout.

```mermaid
sequenceDiagram
    participant Dev as Developer
    participant Agent as Copilot Agent
    participant Files as Workspace Files
    participant Terminal as Terminal

    Dev->>Agent: "Add xUnit tests for all services in /Services"
    Agent->>Files: Reads existing service files
    Agent->>Agent: Plans: 3 services × test classes
    Agent->>Files: Creates PermitServiceTests.cs
    Agent->>Files: Creates RegionServiceTests.cs
    Agent->>Files: Creates ApplicantServiceTests.cs
    Agent->>Terminal: dotnet test
    Terminal-->>Agent: 12 tests passed
    Agent-->>Dev: "Created 3 test files, 12 tests, all passing"
```

---

## Switching Between Modes

In the Copilot Chat panel, use the **mode selector dropdown** (bottom of the chat input area) to switch between Ask, Edit, and Agent.

> **Tip:** Start in Ask mode to plan, switch to Agent mode to execute.
