# Contributing to the GitHub Copilot Advanced Workshop

Thank you for helping improve this workshop! Whether you are fixing a typo, adding a new exercise, or improving a code sample, all contributions are welcome.

---

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Before You Start](#before-you-start)
- [Workflow](#workflow)
- [Code Style](#code-style)
- [Testing Requirements](#testing-requirements)
- [Pull Request Checklist](#pull-request-checklist)
- [Content Guidelines](#content-guidelines)

---

## Code of Conduct

This project follows the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/). By participating, you agree to treat all contributors with respect.

---

## Before You Start

1. **Check for an existing issue.** Search the [Issues](../../issues) tab before opening a new one.
2. **Open an issue first** for non-trivial changes. This avoids wasted effort if the direction is unclear.
3. **Small PRs merge faster.** One concern per pull request is strongly preferred.

---

## Workflow

```bash
# 1. Fork the repository on GitHub
# 2. Clone your fork
git clone https://github.com/<your-username>/GitHubCopilot-AdvancedRepo.git
cd GitHubCopilot-AdvancedRepo

# 3. Create a feature branch (use kebab-case)
git checkout -b fix/typo-in-module-06-readme
# or
git checkout -b feat/add-exercise-06-security

# 4. Make your changes

# 5. Run all tests (see Testing Requirements below)

# 6. Commit with a clear message
git commit -m "fix: correct Playwright selector in permit-submission.spec.ts"

# 7. Push and open a Pull Request against main
git push origin fix/typo-in-module-06-readme
```

---

## Code Style

All C# code must follow the conventions in [`.github/copilot-instructions.md`](.github/copilot-instructions.md):

- Target framework: **`net8.0`**
- Private fields: `_camelCase`
- Public members: `PascalCase`
- Async methods: suffix with `Async`
- XML doc comments on all `public` / `protected` members
- Use `ArgumentNullException.ThrowIfNull()` for guard clauses
- Use `IHttpClientFactory` — never `new HttpClient()`

SQL samples must target **T-SQL (SQL Server / LocalDB)**. No MySQL or PostgreSQL dialect.

Mermaid diagrams must render via GitHub's native renderer — test at [mermaid.live](https://mermaid.live) before committing.

---

## Testing Requirements

Before submitting a PR, verify:

### C# unit tests

```powershell
cd 06-qa-testing/csharp-unit-tests
dotnet test
```

All tests must pass with no failures or skipped tests.

### Playwright UI tests

```powershell
cd 06-qa-testing/playwright-samples
npm install
npx playwright install --with-deps
npx playwright test
```

All spec files must pass.

### Markdown

- All internal links must resolve (no 404s)
- Code blocks must specify a language (` ```csharp `, ` ```sql `, ` ```bash `, etc.)
- Mermaid blocks must use ` ```mermaid ` fences

---

## Pull Request Checklist

Before marking your PR ready for review, confirm:

- [ ] Branch is up to date with `main`
- [ ] `dotnet test` passes
- [ ] `npx playwright test` passes (if Playwright files changed)
- [ ] No secrets, tokens, or connection strings committed
- [ ] New documentation follows the module structure (README + `docs/` folder)
- [ ] Mermaid diagrams render correctly on GitHub
- [ ] Code follows naming conventions in `copilot-instructions.md`

---

## Content Guidelines

### Writing style

- Use **second person** ("you") when addressing the reader
- Keep sentences short — aim for one idea per sentence
- Use tables for comparisons and step lists for procedures
- Inline code for: file names, paths, commands, type names, method names

### Module structure

Each module should contain at minimum:

```
NN-module-name/
├── README.md          # Overview, diagrams, quick-start
├── docs/              # One .md per sub-topic
└── samples/           # Working code (buildable / runnable)
```

### Exercise structure

Each hands-on exercise should:

1. State learning objectives (3 bullet points max)
2. Provide a time estimate
3. Reference the relevant module docs
4. Include a troubleshooting table
5. End with a clear "✅ Exercise Complete" checkpoint

---

## Questions

Open a [Discussion](../../discussions) for questions that are not bug reports or feature requests. The maintainers monitor discussions during active workshop seasons.
