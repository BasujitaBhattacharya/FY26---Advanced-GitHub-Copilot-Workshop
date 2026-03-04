# Copilot CLI — Features Reference

> **Source:** [About GitHub Copilot CLI](https://docs.github.com/en/copilot/concepts/agents/copilot-cli/about-copilot-cli)

GitHub Copilot CLI is a standalone AI agent for your terminal. It is invoked as `copilot` (not `gh copilot`) and supports both interactive conversational sessions and headless programmatic usage.

---

## Installation

See the [official install guide](https://docs.github.com/en/copilot/how-tos/set-up/install-copilot-cli) for platform-specific instructions.

**Requirements:**
- Linux, macOS, or Windows (PowerShell or WSL)
- GitHub account with any Copilot plan (Pro / Business / Enterprise)
- GitHub CLI authenticated (`gh auth login`)

---

## Supported Operating Systems

| OS | Notes |
|----|-------|
| **Linux** | Fully supported |
| **macOS** | Fully supported |
| **Windows** | Supported via PowerShell or WSL |

---

## Modes of Use

### Interactive Interface

Start an interactive session:

```bash
copilot
```

This opens a conversational interface. You can chat with Copilot, give it tasks, and steer the work iteratively.

**Two sub-modes** are available — press `Shift+Tab` to cycle between them:

| Mode | Behaviour |
|------|-----------|
| **Ask/Execute (default)** | Describe your goal; Copilot plans and acts step by step, requesting tool permissions as needed |
| **Plan mode** | Copilot asks clarifying questions, builds a structured implementation plan, and waits for your approval before writing any code |

Plan mode is particularly useful for complex or multi-step tasks where you want to catch misunderstandings **before** any code is written.

### Programmatic Interface

Pass a prompt directly on the command line — Copilot completes it and exits:

```bash
copilot -p "Show me this week's commits and summarize them" --allow-tool 'shell(git)'
```

Pipe from a script:

```bash
./generate-task-options.sh | copilot
```

The programmatic interface is designed for automation, CI pipelines, and scripted workflows.

> **Security note:** Use `--allow-all-tools` only in restricted environments (container/VM). It grants Copilot the same filesystem and execution access as you.

---

## Tool Permission Flags

When running programmatically (or to pre-approve tools in an interactive session), use these flags:

| Flag | Effect |
|------|--------|
| `--allow-tool 'shell(git)'` | Allow specific `git` subcommands without prompting |
| `--allow-tool 'shell(git push)'` | Allow only `git push` specifically |
| `--allow-tool 'write'` | Allow file edits without prompting |
| `--allow-tool 'MY-MCP-SERVER'` | Allow all tools from a named MCP server |
| `--allow-all-tools` | Allow all tools — no prompts |
| `--deny-tool 'shell(rm)'` | Block `rm` even if `--allow-all-tools` is set |
| `--deny-tool 'shell(git push)'` | Block `git push` specifically |

**Combining flags:**

```bash
# Allow everything except rm and git push
copilot --allow-all-tools --deny-tool 'shell(rm)' --deny-tool 'shell(git push)'

# Allow all tools from a specific MCP server, but block one tool within it
copilot --allow-tool 'azure-devops-mcp' --deny-tool 'azure-devops-mcp(delete_repo)'
```

In interactive sessions, when Copilot requests a tool permission you can respond:
1. **Yes** — allow this one time
2. **Yes, approve for this session** — allow for the duration of the current session
3. **No (Esc)** — cancel and give Copilot feedback on what to try instead

---

## Slash Commands

Use these inside an interactive session:

| Command | Description |
|---------|-------------|
| `/compact` | Manually compress conversation history to free up context |
| `/context` | Show a detailed token usage breakdown |
| `/model` | List available models and switch to a different one |
| `/mcp` | List connected MCP servers and their available tools |
| `/fleet` | Trigger parallel subagent execution for the current task |
| `/allow-all` | Approve all pending tool permissions for the session |
| `/yolo` | Approve all tool permissions and suppress further prompts |
| `/feedback` | Submit feedback, file a bug, or request a feature |

---

## Steering the Conversation

While Copilot is thinking or executing, you can:

- **Queue messages** — type follow-up instructions to redirect Copilot mid-task
- **Reject tool requests with feedback** — press Escape when a tool is requested and explain why; Copilot adapts without stopping

---

## Automatic Context Management

Copilot CLI manages context automatically:

- **Auto-compaction** — when a session reaches 95% of the token limit, Copilot automatically compresses history in the background. This enables effectively unlimited session length.
- **Manual compaction** — use `/compact` any time; press Escape to cancel
- **Visualize usage** — `/context` shows a detailed breakdown

---

## Use Cases

### Local Project Tasks

Ask Copilot to modify code, or explain changes:

```
Change the background-color of H1 headings to dark blue
```

Get a git history summary:

```
Show me the last 5 changes made to CHANGELOG.md — who changed it, when, and a brief summary
```

Improve code or documentation:

```
Suggest improvements to the PermitService class
Rewrite the README to be more accessible to newcomers
```

Perform git operations:

```
Commit the changes to this repo
Revert the last commit, leaving the changes unstaged
```

Build an app from scratch:

```
Use create-next-app and Tailwind CSS to create a Next.js dashboard that tracks
GitHub Actions build metrics for this project. After creating, give me
instructions to build and view it in my browser.
```

### GitHub.com Tasks

Work with pull requests:

```
List my open PRs

Merge all open PRs I've created in my-org/my-repo

Close PR #11 on my-org/my-repo

Check the changes in PR https://github.com/my-org/my-repo/pull/55 and report any serious errors
```

Work with issues:

```
List all open issues assigned to me in my-org/my-repo

I've been assigned this issue: https://github.com/my-org/my-repo/issues/42
Start working on it in a suitably named branch

Raise an improvement issue in my-org/my-repo — the file open() call in utils.py never closes
```

Create and manage workflows:

```
Branch off main and create a GitHub Actions workflow that runs ESLint on PRs,
adds inline diff comments for warnings/errors, and fails the check if any errors
are found. Push the branch and create a pull request.
```

---

## Model Usage

The default model is **Claude Sonnet 4.5** (1× premium request multiplier).
Change it in a session with `/model` or at launch with `--model`:

```bash
copilot --model claude-opus-4-5
```

Each interaction in an interactive session, and each programmatic invocation, consumes one premium request multiplied by the model's multiplier.

---

## Customizing Copilot CLI

| Feature | Description | Docs |
|---------|-------------|------|
| **Custom instructions** | Project-level instructions file; all files combine (not priority-based fallback) | [Add custom instructions](https://docs.github.com/en/copilot/how-tos/copilot-cli/customize-copilot/add-custom-instructions) |
| **MCP servers** | Connect Copilot CLI to external data and tools | [Using Copilot CLI](https://docs.github.com/en/copilot/how-tos/use-copilot-agents/use-copilot-cli#add-an-mcp-server) |
| **Custom agents** | Create specialized Copilot variants for specific tasks; reference with `@agent-name` | [Create custom agents](https://docs.github.com/en/copilot/how-tos/copilot-cli/customize-copilot/create-custom-agents-for-cli) |
| **Hooks** | Shell commands at key points in agent execution (validation, logging, security) | [About hooks](https://docs.github.com/en/copilot/concepts/agents/coding-agent/about-hooks) |
| **Skills** | Enhance Copilot with specialized instructions, scripts, and resources | [About agent skills](https://docs.github.com/en/copilot/concepts/agents/about-agent-skills) |
| **Copilot Memory** | Persistent understanding of repo conventions across sessions | [About agentic memory](https://docs.github.com/en/copilot/concepts/agents/copilot-memory) |

---

## Using Copilot CLI via ACP

ACP (Agent Client Protocol) is an open standard for interacting with AI agents. It allows Copilot CLI to be used as an agent in third-party tools, IDEs, and automation systems that support the protocol.

See [Copilot CLI ACP server](https://docs.github.com/en/copilot/reference/acp-server).

---

## Security Considerations

1. **Trusted directories** — launch Copilot CLI only from directories you trust. On start, you confirm trust for the working directory.
2. **Review permissions** — always review what each tool request will do before approving. Option 3 (reject + feedback) lets you steer the approach without stopping.
3. **Use restricted environments** — for automated workflows using `--allow-all-tools`, run inside a container or VM with controlled filesystem and network access.
4. **Sensitive data** — don't launch from directories containing secrets, credentials, or data you don't want modified.
