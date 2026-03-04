# Azure DevOps MCP Server

Connect GitHub Copilot Agent mode to your Azure DevOps organization — query work items, manage pipelines, review PRs, and interact with repos directly from Copilot Chat.

**Source:** [github.com/microsoft/azure-devops-mcp](https://github.com/microsoft/azure-devops-mcp)

---

## Prerequisites

1. **Node.js 20+** installed
2. An **Azure DevOps Personal Access Token (PAT)** with appropriate scopes:
   - Work Items: Read & Write
   - Code: Read
   - Build: Read
   - Pull Requests: Read & Write

   Create a PAT at: `https://dev.azure.com/{your-org}/_usersSettings/tokens`

3. Set environment variables:
   ```bash
   # Windows PowerShell
   $env:AZURE_DEVOPS_ORG_URL = "https://dev.azure.com/your-org"
   $env:AZURE_DEVOPS_PAT = "your-pat-here"

   # Or add to your system environment variables for persistence
   ```

---

## Setup

1. Copy [`mcp.json`](mcp.json) to your workspace `.vscode/` folder
2. Set the two environment variables above
3. In VS Code: `Ctrl+Shift+P` → **MCP: List Servers** — verify `azure-devops` shows as connected
4. Open Copilot Chat in Agent mode — the Azure DevOps tools are now available

---

## Example Prompts

### 1. List Open Work Items

```
List all open work items assigned to me in the current sprint.
```

*What it does:* Queries the Azure DevOps Boards API for your active sprint's work items, filtered by assignee.

---

### 2. Create a Branch for a Work Item

```
Create a new branch called feature/add-permit-validation for work item #1847.
Link the branch to the work item.
```

*What it does:* Creates the branch in the specified repo and links it to the work item in Azure DevOps.

---

### 3. Summarise Recent Pull Requests

```
Summarise the last 5 pull requests in the PermitSystem repo.
Include author, status, number of comments, and what each PR changed.
```

*What it does:* Fetches PR metadata and diff summaries, then produces a human-readable recap.

---

### 4. Check Pipeline Status

```
Which pipelines ran today in the PermitSystem project?
Which ones failed and what was the failure message?
```

*What it does:* Queries the Builds API for today's runs, filters failures, and fetches the failure summary from the build logs.

---

### 5. Get Latest Build Status

```
What is the current build status for the main branch of the PermitSystem repo?
When was the last successful build?
```

*What it does:* Fetches the latest build status for the specified branch from the Azure DevOps Pipelines API.

---

## Available Tools (from the server)

Once connected, Copilot can use tools including:

- `listWorkItems` — Query boards and backlogs
- `getWorkItem` — Get a specific work item by ID
- `createWorkItem` — Create a new work item
- `updateWorkItem` — Update work item fields
- `listPullRequests` — List PRs in a repo
- `getPullRequest` — Get PR details and threads
- `listBuilds` — Query build history
- `getBuildLogs` — Retrieve build log content
- `listRepositories` — List repos in a project
- `getBranches` — List branches in a repo
