# Lab Prerequisites

> Complete this checklist **before the lab starts**. Each item has a validation command you can run to confirm it's ready.

---

## Checklist

### GitHub & Copilot

- [ ] GitHub account with Copilot Individual, Business, or Enterprise access
- [ ] GitHub Copilot extension installed in VS Code
- [ ] GitHub Copilot Chat extension installed in VS Code
- [ ] You can open Copilot Chat with `Ctrl+Alt+I`

### VS Code

- [ ] VS Code version 1.92 or later
- [ ] This repository cloned locally

```bash
# Validate VS Code version
code --version
# Expected: 1.92.x or higher
```

### .NET SDK

- [ ] .NET 8 SDK installed

```bash
dotnet --version
# Expected: 8.x.x
```

### Node.js (for Playwright exercises)

- [ ] Node.js 20 or later

```bash
node --version
# Expected: v20.x.x or higher

npm --version
# Expected: 10.x.x or higher
```

### SQL Server LocalDB (for database exercises)

- [ ] SQL Server LocalDB available

```bash
sqllocaldb i
# Should list at least one instance: MSSQLLocalDB
```

If not installed:
- Download from [SQL Server Express](https://www.microsoft.com/en-us/sql-server/sql-server-downloads)
- Or install **Visual Studio 2022** (includes LocalDB)

### VS Code Extensions

- [ ] `ms-mssql.mssql` — SQL Server (mssql)
- [ ] `ms-dotnettools.csharp` — C# Dev Kit

```bash
# Check installed extensions
code --list-extensions | findstr mssql
code --list-extensions | findstr csharp
```

### GitHub CLI (for Exercise 03)

- [ ] GitHub CLI installed and authenticated

```bash
gh --version
# Expected: gh version 2.x.x

gh auth status
# Expected: Logged in to github.com as <your-username>
```

Install: [https://cli.github.com/](https://cli.github.com/)

---

## Fast Validation

Run all checks in one go (PowerShell):

```powershell
# Validation script — run from repo root
Write-Host "=== Lab Prerequisites Validation ===" -ForegroundColor Cyan

# VS Code
$vscodeVersion = code --version | Select-Object -First 1
Write-Host "VS Code: $vscodeVersion" -ForegroundColor $(if ($vscodeVersion -ge "1.92") {"Green"} else {"Red"})

# .NET SDK
$dotnetVersion = dotnet --version
Write-Host ".NET SDK: $dotnetVersion" -ForegroundColor $(if ($dotnetVersion -like "8.*") {"Green"} else {"Red"})

# Node.js
try {
    $nodeVersion = node --version
    Write-Host "Node.js: $nodeVersion" -ForegroundColor Green
} catch {
    Write-Host "Node.js: NOT FOUND" -ForegroundColor Red
}

# GitHub CLI
try {
    $ghVersion = gh --version | Select-Object -First 1
    Write-Host "GitHub CLI: $ghVersion" -ForegroundColor Green
} catch {
    Write-Host "GitHub CLI: NOT FOUND" -ForegroundColor Red
}

# SQL LocalDB
$localdb = sqllocaldb i 2>&1
Write-Host "LocalDB: $($localdb -join ', ')" -ForegroundColor $(if ($localdb) {"Green"} else {"Yellow"})

Write-Host "=== Done ===" -ForegroundColor Cyan
```

---

## If Something Is Missing

| Missing | Quick fix |
|---|---|
| GitHub Copilot extension | Extensions panel → search "GitHub Copilot" → Install |
| .NET 8 SDK | [https://dotnet.microsoft.com/download/dotnet/8.0](https://dotnet.microsoft.com/download/dotnet/8.0) |
| Node.js | [https://nodejs.org/](https://nodejs.org/) — install LTS |
| GitHub CLI | [https://cli.github.com/](https://cli.github.com/) |
| SQL LocalDB | Install via Visual Studio Installer → Individual Components → SQL Server Express LocalDB |
| mssql extension | `code --install-extension ms-mssql.mssql` |

---

## You're Ready

Once all items are checked, proceed to [Exercise 01 → Customization](../exercises/exercise-01-customization.md).
