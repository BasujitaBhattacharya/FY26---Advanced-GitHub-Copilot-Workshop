# Playwright MCP Server

Connect GitHub Copilot Agent mode to a real browser via Playwright. This lets Copilot navigate websites, take screenshots, interact with UI elements, fill forms, and check for accessibility violations — all from a chat prompt.

This is extremely useful for **exploratory UI testing**, **generating Playwright test scripts** from live application behaviour, and **validating accessibility** before writing formal tests.

**Source:** [github.com/microsoft/playwright-mcp](https://github.com/microsoft/playwright-mcp)

---

## Prerequisites

1. **Node.js 20+** installed
2. Playwright browsers installed:
   ```bash
   npx playwright install chromium
   ```
   That's it — no additional auth or credentials required.

---

## Setup

1. Copy [`mcp.json`](mcp.json) to your workspace `.vscode/` folder
2. Run `npx playwright install chromium` once (if not already done)
3. In VS Code: `Ctrl+Shift+P` → **MCP: List Servers** — verify `playwright` shows as connected
4. Open Copilot Chat in Agent mode — Playwright tools are now available

> When Copilot uses a Playwright tool, a browser window will open on your machine. This is expected behaviour.

---

## Example Prompts

### 1. Navigate and Screenshot

```
Navigate to https://www.Customer.ca and take a screenshot.
What is the main content on this page?
```

*What it does:* Opens a browser, navigates to the URL, takes a screenshot, and describes what it sees.

---

### 2. Extract Page Links

```
Go to https://www.Customer.ca/page/government-Customer and list all the navigation links on the page.
Include the link text and the URL each link points to.
```

*What it does:* Navigates to the page and extracts all `<a>` elements.

---

### 3. Interact with a Button

```
Navigate to the login page at http://localhost:3000/login.
Click the "Sign in with GitHub" button and take a screenshot of what happens.
```

*What it does:* Navigates to the page, finds the button, clicks it, and captures the result.

---

### 4. Fill a Form

```
Go to http://localhost:3000/permits/new.
Fill in the form with:
- Applicant name: "Jane Smith"
- Permit type: "Building"
- Region: "Toronto"
Then click Submit and take a screenshot of the confirmation page.
```

*What it does:* Navigates to the form, fills each field, submits, and captures the result.

---

### 5. Generate a Playwright Test from Live Behaviour

```
Navigate to http://localhost:3000 and perform the permit application flow:
1. Click "Apply for a Permit"
2. Fill in the form with test data
3. Submit the form
4. Verify the confirmation message appears

Then generate a Playwright TypeScript test that replicates this exact flow.
```

*What it does:* Follows the flow in a real browser, then generates a `*.spec.ts` test file based on what it observed — a massive time-saver for writing UI tests.

---

## Available Tools (from the server)

Once connected, Copilot can use tools including:

- `navigate` — Navigate to a URL
- `screenshot` — Take a screenshot of the current page
- `click` — Click an element (by selector or description)
- `fill` — Fill an input field
- `select` — Select an option from a dropdown
- `getText` — Get text content of an element
- `getLinks` — Extract all links from the page
- `waitForElement` — Wait for an element to appear
- `executeJavaScript` — Run JS in the browser context
- `checkAccessibility` — Run accessibility checks (axe-core)

---

## Connection to Module 06 (QA & Testing)

The Playwright MCP server pairs perfectly with the testing workflow in [Module 06](../../06-qa-testing/README.md):

- Use the MCP server to **explore the app** and understand its UI
- Have Copilot generate Playwright `*.spec.ts` tests from the observed flows
- Refine and run those tests with the full Playwright test setup in `06-qa-testing/playwright-samples/`
