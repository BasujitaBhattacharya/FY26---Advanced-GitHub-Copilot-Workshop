#!/usr/bin/env bash
# =============================================================================
# GitHub Copilot CLI — Example Sessions
# Module 04
#
# NOTE: GitHub Copilot CLI is a standalone 'copilot' command — NOT a gh extension.
# See: https://docs.github.com/en/copilot/how-tos/set-up/install-copilot-cli
#
# Prerequisites:
#   - copilot installed and authenticated (gh auth login)
#   - Run from a git repo directory you trust
# =============================================================================

echo "================================================================="
echo "  GitHub Copilot CLI — Demo Examples"
echo "  These are example PROMPTS to type inside: copilot"
echo "================================================================="
echo ""
echo "Start an interactive session with:   copilot"
echo ""

echo "--- DEMO 1: Interactive session — basic local task ---"
echo ""
echo "  Start: copilot"
echo "  Then type:"
echo '    Change the background-color of H1 headings to dark blue'
echo ""
echo "  Copilot will locate the relevant CSS file and modify it."
echo ""

echo "--- DEMO 2: Plan mode for a complex task ---"
echo ""
echo "  Start: copilot"
echo "  Press Shift+Tab to enter plan mode"
echo "  Then type:"
echo '    Refactor the PermitService class to use ILogger<PermitService>'
echo '    instead of Console.WriteLine. Add structured log messages for'
echo '    each public method entry, exit, and any exception paths.'
echo ""
echo "  Copilot will ask clarifying questions, show you a plan,"
echo "  and wait for approval before writing any code."
echo ""

echo "--- DEMO 3: Programmatic mode — summarise recent git activity ---"
echo ""
echo "  copilot -p \"Show me this week's commits and summarize the key changes\" \\"
echo "    --allow-tool 'shell(git)'"
echo ""
echo "  This runs non-interactively and exits when done."
echo ""

echo "--- DEMO 4: GitHub.com task — list open PRs ---"
echo ""
echo "  Start: copilot"
echo "  Then type:"
echo '    List my open PRs'
echo ""
echo "  Or filter to a specific repo:"
echo '    List all open issues assigned to me in my-org/permits-api'
echo ""

echo "--- DEMO 5: GitHub.com task — start working on an issue ---"
echo ""
echo "  Start: copilot"
echo "  Then type:"
echo '    I have been assigned this issue:'
echo '    https://github.com/my-org/permits-api/issues/42'
echo '    Start working on it in a suitably named branch.'
echo ""
echo "  Copilot will read the issue, plan the implementation, and start coding."
echo ""

echo "--- DEMO 6: Create a PR from the terminal ---"
echo ""
echo "  Start: copilot"
echo "  Then type:"
echo '    In the root of this repo, add a SECURITY.md file with:'
echo '    - Our security disclosure email: security@ontario.ca'
echo '    - A link to our vulnerability policy at ontario.ca/security'
echo '    - A note that all reports are responded to within 5 business days'
echo '    Then create a pull request to add this file.'
echo ""

echo "--- DEMO 7: Context management ---"
echo ""
echo "  During any interactive session, type:"
echo '    /compact        — manually compress history if session is long'
echo '    /context        — see token usage breakdown'
echo '    /model          — switch to a different model'
echo ""

echo "--- DEMO 8: /fleet — parallel test generation ---"
echo ""
echo "  Start: copilot"
echo "  Press Shift+Tab to enter plan mode first, then type:"
echo '    /fleet'
echo '    Generate complete xUnit test files for each of the 5 service classes'
echo '    in src/Services/. Use Moq for dependencies. Cover happy path, null'
echo '    inputs, and exception paths. Place each test file in tests/Services/'
echo '    named <ServiceName>Tests.cs'
echo ""
echo "  /fleet runs all 5 test files in parallel via subagents."
echo ""

echo "--- DEMO 9: Programmatic mode with denial ---"
echo ""
echo "  Allow everything EXCEPT git push and rm:"
echo '  copilot -p "Refactor all TODO comments to GitHub issues" \'
echo '    --allow-all-tools --deny-tool '"'"'shell(git push)'"'"' --deny-tool '"'"'shell(rm)'"'"''
echo ""

echo "--- DEMO 10: Check a PR for errors (code review) ---"
echo ""
echo "  Start: copilot"
echo "  Then type:"
echo '    Check the changes made in PR https://github.com/my-org/permits-api/pull/99.'
echo '    Report any serious code quality or security issues you find.'
echo ""

echo "================================================================="
echo "  Tip: In interactive mode, you can always press Escape to"
echo "  reject a tool request and give Copilot feedback on what to"
echo "  do instead — it will adapt without stopping."
echo "================================================================="
