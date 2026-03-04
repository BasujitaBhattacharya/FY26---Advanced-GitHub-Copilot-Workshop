# Background Agent

Background Agent lets you assign a long-horizon task to Copilot and walk away. It runs **asynchronously in the background** — you keep working — and when finished, it opens a pull request with the results.

---

## What It Is

Background Agent is a **cloud-assisted, long-running agent** that:
- Accepts a natural-language task description
- Plans and executes changes across your codebase autonomously
- Runs on an isolated branch
- Creates a PR when done (or asks for clarification if stuck)

It's designed for tasks that would take 10–30+ minutes manually: large refactors, adding logging or observability across a whole project, migrating from one library to another, generating full test suites, etc.

---

## How to Trigger It

### Method 1: From VS Code Chat

1. Open Copilot Chat (`Ctrl+Alt+I`)
2. Switch to **Agent mode** using the mode selector
3. Click **Start Background Task** (or use the `#background` keyword in your message)
4. Describe your task clearly
5. Copilot confirms the task, spins off the background work, and you're free to continue

### Method 2: From a GitHub Issue

In Agent mode, reference a GitHub issue:
```
Work on #123 — the issue describes adding pagination to all list endpoints
```

Copilot will read the issue description and use it as the task specification.

---

## What Makes a Good Background Task

```mermaid
flowchart LR
    subgraph GOOD["✅ Good Background Tasks"]
        G1["Well-scoped with clear success criteria"]
        G2["Repetitive changes across many files\ne.g. add XML docs to all public methods"]
        G3["Known migration patterns\ne.g. swap HttpClient for IHttpClientFactory everywhere"]
        G4["Generating boilerplate\ne.g. CRUD API + tests for all entities"]
    end
    subgraph BAD["❌ Poor Background Tasks"]
        B1["Vague: 'make the code better'"]
        B2["Requires business decisions mid-task"]
        B3["Depends on external services or secrets"]
        B4["Exploratory work where you need to guide each step"]
    end
```

---

## Reviewing the Results

When Background Agent completes:
1. A **GitHub Pull Request** is opened on a new branch
2. You receive a VS Code notification (and a GitHub notification)
3. Review the diff in the PR — accept, request changes, or close
4. Copilot can iterate: add a comment to the PR with follow-up instructions and it will continue

---

## Limitations

| Limitation | Detail |
|-----------|--------|
| Requires GitHub repo | Must be connected to a GitHub repository |
| No access to running services | Can't hit live APIs, databases, or external dependencies during execution |
| Time limits | Very long tasks may time out — break them into smaller chunks |
| Non-deterministic | Same task may produce slightly different results each run |
| Requires review | Always review the PR — Background Agent can make mistakes |

---

## GitHub Actions Integration

Background Agent can be configured to run your CI pipeline after making changes:

```mermaid
sequenceDiagram
    participant BGA as Background Agent
    participant GH as GitHub Actions
    participant PR as Pull Request

    BGA->>GH: Pushes branch with changes
    GH->>GH: Runs CI pipeline (build + test)
    GH-->>BGA: Build results (pass/fail)
    alt Build failed
        BGA->>BGA: Analyses failures and retries
    else Build passed
        BGA->>PR: PR marked ready for review
    end
```

This means Background Agent won't create a PR with broken builds — it will try to fix failures first.

---

## Tips

- Write your task description like a detailed ticket — the more context, the better the result
- Include success criteria: *"All public methods in /Services/ should have XML doc comments. Build must pass."*
- For large migrations, start with a small scope and validate before expanding
- Check the Background Agent's progress in the **Copilot** panel — you can see what it's working on
