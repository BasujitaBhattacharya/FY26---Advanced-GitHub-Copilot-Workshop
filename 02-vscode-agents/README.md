# Module 02 — VS Code Agents

[![Module](https://img.shields.io/badge/Module-02_VS_Code_Agents-0078d4?logo=github&logoColor=white)](.)
[![Estimated Time](https://img.shields.io/badge/Time-40_min-informational)](.)

> **What you'll learn:** The different execution environments for GitHub Copilot agents in VS Code — from the conversational modes in the IDE, to Background Agent running asynchronous long-horizon tasks, to Cloud-based execution on GitHub.com. Includes a decision guide to help you pick the right agent for any task.

---

## Agent Architecture Overview

```mermaid
flowchart TB
    subgraph LOCAL["🖥️ Local — VS Code (your machine)"]
        direction LR
        ASK["💬 Ask Mode\nConversational Q&A\nNo code changes"]
        PLAN["✏️ Plan Mode\nGenerate Plan\nYou review/edit steps \nbefore implementation"]
        AGENT["🤖 Agent Mode\nAutonomous, multi-step\nUses tools + terminal"]
    end

    subgraph BG["⚡ Background Agent (VS Code + GitHub)"]
        BGAGENT["Runs asynchronously\nwhile you keep working\nCreates a GitHub PR with results"]
    end

    subgraph CLOUD["☁️ Cloud — GitHub.com"]
        CLOUDAGENT["Copilot Coding Agent\nFully cloud-hosted\nAssigned via GitHub Issues"]
    end

    DEV["👩‍💻 Developer"] --> LOCAL
    DEV --> BG
    DEV --> CLOUD

    style LOCAL fill:#f0f6ff,stroke:#0078d4
    style BG fill:#fff4e0,stroke:#d97706
    style CLOUD fill:#f0fff4,stroke:#107c10
```

---

## Which Agent Should I Use?

```mermaid
flowchart TD
    Q1{"How long will\nthe task take?"}
    Q2{"Do I need to\nstay in control\nstep-by-step?"}
    Q3{"Is the task\nwell-defined\nenough to delegate?"}
    Q4{"Do I want to keep\nworking while it runs?"}
    Q5{"Is this tied to\na GitHub Issue?"}

    A1["💬 Ask Mode\nFor questions,\nexplanations"]
    A2["✏️ Plan Mode\nFor targeted,\nstructured plans"]
    A3["🤖 Agent Mode (local)\nFor complex multi-file\ntasks with oversight"]
    A4["⚡ Background Agent\nFor long tasks, runs\nasync, returns a PR"]
    A5["☁️ Copilot Coding Agent\nFully delegated,\ncloud-hosted"]

    Q1 -->|"< 2 min"| Q2
    Q1 -->|"2–10 min"| Q3
    Q1 -->|"> 10 min"| Q4
    Q2 -->|Yes| A2
    A2 -->|Handoff implementation| A3
    Q2 -->|No, just explore| A1
    Q3 -->|No, I want to guide it| A3
    Q3 -->|Yes, well-defined| Q4
    Q4 -->|No, I'll watch| A3
    Q4 -->|Yes, let it run| Q5
    Q5 -->|Yes| A5
    Q5 -->|No| A4
```

---

## Contents

| Doc | What it covers |
|-----|---------------|
| [docs/background-agent.md](docs/background-agent.md) | What Background Agent is, how to use it, GitHub Actions integration |
| [docs/cloud-agents.md](docs/cloud-agents.md) | Copilot Cloud Agent (GitHub.com), cloud sandbox execution |
| [docs/sub-agents.md](docs/sub-agents.md) | Delegation patterns, chaining, practical .NET scenario |
| [docs/agent-selection-guide.md](docs/agent-selection-guide.md) | Detailed selection guide with worked examples |

> **Agent modes (Ask / Plan / Agent)** are covered in [Module 01 → docs/agent-modes.md](../01-customization/docs/agent-modes.md).
> **Copilot Coding Agent on GitHub.com** is covered in [Module 09](../09-copilot-on-github/README.md).

---

## Background Agent Lifecycle

```mermaid
sequenceDiagram
    participant Dev as 👩‍💻 Developer
    participant BGA as ⚡ Background Agent
    participant GH as GitHub

    Dev->>BGA: Describe task in VS Code<br/>("Refactor all controllers to use ILogger<T>")
    BGA->>BGA: Plans changes autonomously
    Note over BGA: Runs asynchronously<br/>Developer continues working
    BGA->>BGA: Edits files, runs build
    BGA->>GH: Opens a Pull Request with changes
    GH-->>Dev: Notification: PR ready for review
    Dev->>GH: Reviews diff, requests changes or merges
```

---

## Background Agent States

```mermaid
stateDiagram-v2
    [*] --> Queued : Task submitted
    Queued --> Running : Agent picks up task
    Running --> AwaitingInput : Agent needs clarification
    AwaitingInput --> Running : Developer responds
    Running --> BuildFailed : Build/test failure detected
    BuildFailed --> Running : Agent retries with fix
    Running --> PullRequestOpened : Task complete
    PullRequestOpened --> [*] : Developer reviews and merges
    Running --> Cancelled : Developer cancels
    Cancelled --> [*]
```
