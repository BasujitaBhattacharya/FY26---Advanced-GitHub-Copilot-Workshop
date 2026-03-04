# Exercise 02 — Background Agent: Issue → Draft PR

⏱ **Estimated time:** 15 minutes  
📚 **References:** [Module 02 — Background Agent](../../02-vscode-agents/docs/background-agent.md)

---

## Learning Objectives

By the end of this exercise you will:

1. Write a GitHub Issue with clear acceptance criteria suitable for the Coding Agent
2. Assign the issue to Copilot and observe the agent working
3. Review the resulting Draft PR and provide iterative feedback

---

## Prerequisites

- GitHub repository with Copilot Business or Enterprise (Coding Agent requires org-level access)
- Push access to the repository

---

## Part A — Write the Issue (5 min)

### Step 1 — Open your GitHub repository on GitHub.com

Navigate to the Issues tab and click **New Issue**.

### Step 2 — Fill in the issue

Copy this template and fill in your repo details:

```markdown
Title: Add GetPermitsByApplicant endpoint to PermitController

## Summary
Add a new `GET /api/permits/by-applicant/{applicantId}` endpoint to the
permits API that returns all permits for a given applicant.

## Acceptance Criteria
- [ ] New endpoint: `GET /api/permits/by-applicant/{applicantId:int}`
- [ ] Returns `IReadOnlyList<PermitSummaryDto>` — a record with PermitId, Type, Status
- [ ] Returns 404 if no permits found for the applicant
- [ ] Returns 400 if applicantId <= 0
- [ ] Constructor-injected `IPermitRepository` used for data access
- [ ] Uses `ILogger<T>` to log at Debug level on entry
- [ ] XML doc comments on the new action method
- [ ] Unit tests added covering: applicant has permits (200), applicant has no permits (404),
     invalid id (400)

## Files to modify
- `05-app-modernization/dotnet-sample/Controllers/PermitController.cs`
- (Agent should create or update test file as appropriate)

## Notes
Follow the `.github/copilot-instructions.md` naming and style conventions.
```

### Step 3 — Submit the issue

Click **Submit new issue**.

---

## Part B — Assign to Copilot Agent (5 min)

### Step 4 — Assign the issue

On the issue page:
1. Click **Assignees** → search for **Copilot**
2. Select it — the agent will start within 1–2 minutes

### Step 5 — Watch the agent work

GitHub will show a "Copilot is working…" indicator. You can:
- Refresh the issue to see progress updates
- Click the agent's comment thread to see its plan

**While waiting:** Open the Background Agent documentation:  
[02-vscode-agents/docs/background-agent.md](../../02-vscode-agents/docs/background-agent.md)

---

## Part C — Review the Draft PR (5 min)

### Step 6 — Open the Pull Request

When the agent completes, it opens a Draft PR. Navigate to it.

### Step 7 — Review the diff

Look for:
- [ ] New action method in `PermitController.cs`
- [ ] `PermitSummaryDto` record created
- [ ] Repository method called correctly
- [ ] Guard clause for `applicantId <= 0`
- [ ] Log statement present
- [ ] XML doc comments on the action

### Step 8 — Provide iterative feedback

If anything is missing, add a review comment:

```
The endpoint returns 404 for "no permits" but the spec says it should return
an empty list (200 OK) — a 404 implies the applicant doesn't exist. Please
change the "no permits" case to return 200 with an empty list.
```

The agent will respond to your comment and push a new commit.

---

## ✅ Exercise Complete

You have:
- Written a well-structured issue for the Coding Agent
- Observed the agent produce a complete implementation
- Provided iterative feedback on the resulting PR

**Proceed to:** [Exercise 03 → GitHub Remote MCP](exercise-03-mcp-github.md)
