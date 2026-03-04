# Exercise 03 — GitHub Remote MCP: Query Your Repo from Chat

⏱ **Estimated time:** 15 minutes  
📚 **References:** [Module 03 — GitHub Remote MCP](../../03-mcp-samples/github-remote-mcp/README.md)

---

## Learning Objectives

By the end of this exercise you will:

1. Connect the GitHub Remote MCP server in VS Code
2. Use Copilot Chat to answer questions about your GitHub repository using MCP tools
3. Query issues, PRs, and repository metadata without leaving VS Code

---

## Part A — Connect the GitHub Remote MCP (5 min)

### Step 1 — Get a GitHub Personal Access Token

```bash
# Option A: Use gh CLI to get your current token
gh auth token

# Option B: Create a new PAT at https://github.com/settings/tokens
# Required scopes: repo, read:org, read:user
```

Copy the token value.

### Step 2 — Add the MCP configuration

Open (or create) `.vscode/mcp.json` in the root of this repository:

```json
{
  "servers": {
    "github": {
      "type": "http",
      "url": "https://api.githubcopilot.com/mcp/",
      "headers": {
        "Authorization": "Bearer ${input:GITHUB_TOKEN}"
      }
    }
  },
  "inputs": [
    {
      "id": "GITHUB_TOKEN",
      "type": "promptString",
      "description": "GitHub Personal Access Token",
      "password": true
    }
  ]
}
```

> **Security note:** Never commit a real token here. The `${input:GITHUB_TOKEN}` pattern prompts you at connection time and never writes to disk.

### Step 3 — Connect via VS Code

1. Open Copilot Chat (`Ctrl+Alt+I`)
2. Click the **tools** icon (🔧) at the bottom of the chat panel
3. Enable **GitHub MCP** from the list
4. VS Code will prompt: "Enter your GitHub Personal Access Token"
5. Paste the token from Step 1

A `✓ GitHub connected` indicator should appear.

---

## Part B — Query Your Repository (5 min)

### Step 4 — List open issues

In Copilot Chat, type:

```
Using the GitHub MCP, list all open issues in this repository.
For each issue, show: number, title, labels, and assignees.
```

Copilot should return a formatted list of your repository's open issues — including the one you created in Exercise 02.

### Step 5 — Query a specific issue

```
Using GitHub MCP, show me the details of issue #1 in this repository,
including all comments and the current assignees.
```

### Step 6 — List recent pull requests

```
Using GitHub MCP, list the 5 most recently updated pull requests.
Show: number, title, status (open/closed/draft), and author.
```

---

## Part C — Practical Copilot + MCP Query (5 min)

### Step 7 — Ask a cross-reference question

```
Using GitHub MCP, find all issues in this repository labelled 'bug'.
For any that are assigned to an open PR, show the PR number and title.
```

### Step 8 — Repository metadata

```
Using GitHub MCP, what is the default branch of this repository?
When was it last pushed to?
How many open issues and open PRs are there right now?
```

### Reflection Questions

- How does using MCP differ from browsing GitHub.com manually?
- What repetitive repository tasks could you automate using Copilot + GitHub MCP?
- What information from GitHub would be useful to pull into a Copilot Spaces context?

---

## Troubleshooting

| Issue | Solution |
|---|---|
| "MCP server not connected" | Check token scopes — needs `repo` scope minimum |
| "Authentication failed" | Token may be expired — generate a new one at github.com/settings/tokens |
| Tool list doesn't show GitHub MCP | Ensure `.vscode/mcp.json` is saved and VS Code has reloaded |
| Token prompt doesn't appear | Ctrl+Shift+P → "MCP: Connect to Server" |

---

## ✅ Exercise Complete

You have:
- Connected the GitHub Remote MCP server in VS Code
- Queried issues and PRs from Copilot Chat using MCP tools
- Explored cross-reference queries combining multiple GitHub data types

**Proceed to:** [Exercise 04 → .NET Modernization](exercise-04-modernization.md)
