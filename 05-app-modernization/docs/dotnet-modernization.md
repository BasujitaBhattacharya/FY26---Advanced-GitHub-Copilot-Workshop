# .NET Upgrade Assistant

The **.NET Upgrade Assistant** is a VS Code extension (and CLI tool) that analyses your .NET Framework project and guides you through an incremental migration to .NET 8.

---

## Installing

1. In VS Code, install the extension:
   - Search for: **".NET Upgrade Assistant"** (publisher: Microsoft)
   - Extension ID: `ms-dotnettools.upgrade-assistant`

2. Or install the CLI globally:
   ```bash
   dotnet tool install -g upgrade-assistant
   ```

---

## Running on the Sample Project

1. Open the `dotnet-sample/` folder in VS Code
2. Right-click on any `.csproj` file in the Explorer → **Upgrade**
3. The Upgrade Assistant opens a guided panel with:
   - Summary of detected issues
   - Recommended upgrade steps in order
   - One-click application of each change

Or via CLI:
```bash
cd 05-app-modernization/dotnet-sample
upgrade-assistant upgrade LegacyPermitApi.csproj
```

---

## What It Detects in the Sample

| Issue | Current (Framework 4.7) | Target (.NET 8) |
|-------|------------------------|-----------------|
| Project file format | Old-style `.csproj` | SDK-style `.csproj` |
| `System.Web` imports | Present | Remove — not in .NET 8 |
| `ConfigurationManager` | Used for connection strings | `IConfiguration` + `appsettings.json` |
| `System.Data.SqlClient` | NuGet package | `Microsoft.Data.SqlClient` |
| Synchronous ADO.NET | `.Open()`, `.ExecuteReader()` | `async/await` equivalents |
| `ApiController` base class | Web API 2 | ASP.NET Core `ControllerBase` or minimal API |
| `Console.WriteLine` for logging | Direct console | `ILogger<T>` |
| `Web.config` | XML config | `appsettings.json` |

---

## Incremental Upgrade Approach

The Upgrade Assistant supports **incremental upgrades** — you don't have to migrate everything at once:

```bash
# 1. Upgrade just the project file format (lowest risk)
upgrade-assistant upgrade LegacyPermitApi.csproj --step ConvertProjectFile

# 2. Then upgrade NuGet packages
upgrade-assistant upgrade LegacyPermitApi.csproj --step UpgradeNuGetPackages

# 3. Apply source code fixes
upgrade-assistant upgrade LegacyPermitApi.csproj --step SourceUpdater
```

This allows you to commit after each step and validate with your CI pipeline before proceeding.

---

## Using Copilot Alongside It

As the Upgrade Assistant flags issues, use these Copilot prompts:

```
# Understand why something is flagged
"Why is ConfigurationManager deprecated in .NET 8? What's the replacement?"

# Get the replacement code
[Select flagged code] → use prompt file: modernize-dotnet.prompt.md

# Fix compilation errors after migration
"This code compiled in .NET Framework but now throws CS0234 in .NET 8. How do I fix it?"

# Validate the migration
"What tests should I write to verify this repository's behaviour hasn't changed after migration?"
```

---

## After Migration: What the Code Looks Like

### Before (see dotnet-sample/)
```csharp
// LEGACY
string conn = ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString;
var permits = _repository.GetAll(); // sync
Console.WriteLine("Error: " + ex.Message);
```

### After (.NET 8)
```csharp
// MODERN
public class PermitController(IPermitRepository repository, ILogger<PermitController> logger) : ControllerBase
{
    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var permits = await repository.GetAllAsync();
        return Ok(permits);
    }
}
```
