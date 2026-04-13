# Azure MCP Server

Connect GitHub Copilot Agent mode to your Azure subscription — list resources, inspect App Services, query storage, check Key Vault, and more, all without leaving VS Code.

**Source:** [github.com/Azure/azure-mcp](https://github.com/Azure/azure-mcp)

---

## Prerequisites

1. **Node.js 20+** installed
2. **Azure CLI** installed and logged in:
   ```bash
   az login
   az account set --subscription "Your Subscription Name or ID"
   ```
3. Your account needs at least **Reader** role on the resources you want to query.

---

## Setup

1. Copy [`mcp.json`](mcp.json) to your workspace `.vscode/` folder
2. Ensure `az login` is active (run `az account show` to confirm)
3. In VS Code: `Ctrl+Shift+P` → **MCP: List Servers** — verify `azure` shows as connected
4. Open Copilot Chat in Agent mode — Azure tools are now available

---

## Example Prompts

### 1. List All Resources in a Resource Group

```
List all resources in the rg-Customer-permits-dev resource group.
Include resource type, name, location, and current status.
```

*What it does:* Calls the Azure Resource Manager API to enumerate all resources in the group.

---

### 2. Inspect Storage Account

```
What containers and blobs are in the storage account stCustomerpermits?
How much total data is stored?
```

*What it does:* Queries the storage account's containers and blob metadata.

---

### 3. Check App Service Configuration

```
Show the App Service configuration for app-permit-api including:
- Environment variables (mask any secrets)
- Current SKU and region
- Deployment slots
```

*What it does:* Retrieves App Service settings via the Azure Resource Manager API.

---

### 4. List Key Vault Secret Names

```
List all secret names in the kv-Customer-permits Key Vault.
Do not show the secret values — just the names and their expiry dates.
```

*What it does:* Lists Key Vault secret metadata (names + expiry) without exposing values.

---

### 5. Check Resource Group Status

```
What is the current state of all resources in rg-Customer-permits-prod?
Are there any resources in a failed or degraded state?
```

*What it does:* Fetches provisioning state and health for all resources in the group.

---

## Available Tools (from the server)

Once connected, Copilot can use tools including:

- `listResourceGroups` — List all resource groups in a subscription
- `listResources` — List resources in a resource group
- `getResource` — Get details of a specific resource
- `listStorageAccounts` — List storage accounts
- `listBlobContainers` — List containers in a storage account
- `listAppServices` — List App Service apps
- `getAppServiceConfig` — Get App Service configuration
- `listKeyVaults` — List Key Vaults
- `listKeyVaultSecrets` — List secret names (not values) in a Key Vault
- `listSubscriptions` — List available Azure subscriptions
