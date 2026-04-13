# GitHub Remote MCP Server

Connect GitHub Copilot Agent mode to GitHub's own API — search issues, create PRs, review code, inspect workflows, and interact with repos directly from Copilot Chat.

**This is GitHub's official remote MCP server** — it runs in GitHub's cloud, so there's nothing to install locally beyond authentication.

**Source:** [github.com/github/github-mcp-server](https://github.com/github/github-mcp-server)
**Endpoint:** `https://api.githubcopilot.com/mcp/`

---

## Prerequisites

1. A **GitHub account** with GitHub Copilot access
2. A **GitHub Personal Access Token** with scopes:
   - `repo` — full repo access
   - `workflow` — workflow read access
   - `read:org` — org membership read

   Create one at: `https://github.com/settings/tokens`

3. Set the token as an environment variable:
   ```bash
   # Windows PowerShell
   $env:GITHUB_TOKEN = "ghp_your_token_here"
   ```

> **Alternative:** If you have GitHub CLI installed, you can use `gh auth token` to get the active token:
> ```bash
> $env:GITHUB_TOKEN = (gh auth token)
> ```

---

## Setup

1. Copy [`mcp.json`](mcp.json) to your workspace `.vscode/` folder
2. Set the `GITHUB_TOKEN` environment variable
3. In VS Code: `Ctrl+Shift+P` → **MCP: List Servers** — verify `github` shows as connected
4. Open Copilot Chat in Agent mode — GitHub tools are now available

---

## Example Prompts

### 1. Find Open Bugs

```
Find all open issues labelled 'bug' in the Customer/permit-system repo.
Sort by most recently updated. Include title, author, and days open.
```

*What it does:* Queries the GitHub Issues API with label + state filters.

---

### 2. Create a Pull Request

```
Create a pull request from branch feature/add-permit-validation to main
in the Customer/permit-system repo.

Title: "feat: add input validation for permit submission"
Body: "Adds FluentValidation rules for the PermitCreateRequest model.
Closes #1847."
```

*What it does:* Creates a PR via the GitHub REST API with the specified details.

---

### 3. Summarise a Pull Request

```
Summarise the changes in PR #42 in Customer/permit-system.
What files were changed? What's the overall intent of the PR?
Are there any reviewer comments still unresolved?
```

*What it does:* Fetches the PR diff, review threads, and comments, then summarises.

---

### 4. Check Workflow Status

```
What GitHub Actions workflows are defined in Customer/permit-system?
Which ones ran in the last 24 hours and what was the result of each run?
```

*What it does:* Lists workflows and their recent run history.

---

### 5. Recent Commit History

```
Show the last 10 commits on the main branch of Customer/permit-system.
Include author, date, commit message, and which files were changed.
```

*What it does:* Queries the Git commits API for the branch history.

---

## Available Tools (from the server)

Once connected, Copilot can use tools including:

- `listIssues` — List and filter issues
- `createIssue` — Create a new issue
- `getIssue` — Get issue details and comments
- `listPullRequests` — List PRs with filters
- `createPullRequest` — Create a new PR
- `getPullRequest` — Get PR diff and review threads
- `listWorkflows` — List GitHub Actions workflows
- `listWorkflowRuns` — Get workflow run history
- `listCommits` — Get commit history for a branch
- `getRepository` — Get repo metadata and stats
- `searchCode` — Search code across repos
