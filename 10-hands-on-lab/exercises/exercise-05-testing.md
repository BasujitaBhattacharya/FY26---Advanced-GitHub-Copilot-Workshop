# Exercise 05 — AI-Assisted Testing: From SUT to Test Suite

⏱ **Estimated time:** 15–20 minutes
📚 **References:** [Module 06 — Test Plan Generation](../../06-qa-testing/docs/test-plan-generation.md), [generate-tests prompt](.github/prompts/generate-tests.prompt.md)

---

## Learning Objectives

By the end of this exercise you will:

1. Use the `generate-tests` prompt file to produce a structured test plan
2. Accept and run a Copilot-generated xUnit test suite
3. Identify test cases that emerge from AI analysis that a developer might overlook

---

## Part A — Generate a Test Plan (5 min)

### Step 1 — Open the service under test

Open the file:

```
06-qa-testing/csharp-unit-tests/PermitService.cs
```

Review the three public methods:

| Method | Description |
|---|---|
| `SubmitAsync(PermitRequest)` | Creates a new permit, throws on duplicate |
| `GetStatusAsync(string)` | Retrieves permit by ID, throws on null input |
| `CancelAsync(string)` | Cancels a permit, throws if not found |

### Step 2 — Run the `generate-tests` prompt

1. In VS Code, open Copilot Chat (`Ctrl+Alt+I`)
2. Type `/generate-tests` and press Tab to expand the prompt file
3. When prompted for the class, provide: `PermitService`

Copilot will produce a **test plan table** similar to:

| # | Method | Scenario | Expected | Test Type |
|---|---|---|---|---|
| 1 | Constructor | Null repository | Throws `ArgumentNullException` | Unit |
| 2 | Constructor | Null logger | Throws `ArgumentNullException` | Unit |
| 3 | SubmitAsync | Valid request, no duplicate | Returns new `PermitId` | Unit |
| 4 | SubmitAsync | Null request argument | Throws `ArgumentNullException` | Unit |
| 5 | SubmitAsync | Duplicate detected | Throws `DuplicatePermitException` | Unit |
| 6 | SubmitAsync | Repository throws | Throws `PermitStorageException` | Unit |
| 7 | GetStatusAsync | Valid ID, permit found | Returns `PermitStatus` | Unit |
| 8 | GetStatusAsync | Null ID | Throws `ArgumentNullException` | Unit |
| 9 | GetStatusAsync | Unknown ID | Returns `null` | Unit |
| 10 | CancelAsync | Valid existing permit | Calls repository `CancelAsync` once | Unit |
| 11 | CancelAsync | Null ID | Throws `ArgumentNullException` | Unit |
| 12 | CancelAsync | Permit not found | Throws `KeyNotFoundException` | Unit |

Record how many test cases Copilot generated: _______

---

## Part B — Review and Accept the Generated Tests (5 min)

### Step 3 — Ask Copilot to generate the full test file

Continue in the same Copilot Chat session:

```
Generate a complete xUnit test class for PermitService using the test plan above.
Use Moq to mock IPermitRepository and ILogger<PermitService>.
Follow the Arrange / Act / Assert structure with comments.
Name the test class PermitServiceTests and the file PermitServiceTests.cs.
```

Copilot will generate a test class. Review it for:

- [ ] All 12 test cases present
- [ ] `ArgumentNullException.ThrowIfNull()` guard tests use `Should().Throw<ArgumentNullException>()`
- [ ] Moq setup calls are in the Arrange block
- [ ] Each test has exactly one assertion in the Assert block
- [ ] Naming pattern: `MethodName_StateUnderTest_ExpectedBehavior`

Compare the generated file to the reference implementation at:

```
06-qa-testing/csharp-unit-tests/PermitServiceTests.cs
```

### Step 4 — Accept the test file

Use **Insert into file** to write the generated tests to a new file:

```
06-qa-testing/csharp-unit-tests/PermitServiceTests.Generated.cs
```

---

## Part C — Run the Tests (5 min)

### Step 5 — Run the test suite

```powershell
cd "06-qa-testing/csharp-unit-tests"
dotnet test --verbosity normal
```

> **Expected:** All tests pass. If any fail, read the failure message carefully.

### Step 6 — Fix a failing test (if needed)

If a test fails, paste the failure message into Copilot Chat:

```
This xUnit test is failing with the following error. Explain the cause
and suggest the minimal fix to make it pass:

[paste test name and error message here]
```

Accept the suggested fix and re-run `dotnet test`.

---

## Part D — Reflection (5 min)

### Step 7 — Compare coverage

Look at the 12 test cases in the plan. Answer these questions:

1. **Which test cases would you likely have skipped** when writing tests manually under time pressure? (Hint: look at the repository-throws scenario and the null-logger constructor test.)

2. **Which test case is most valuable from a security perspective?** Why?

3. **What is missing from this test suite?** Consider:
   - Integration tests with a real database
   - Tests for concurrent duplicate permit submissions
   - Performance / load tests

4. How would you use a Playwright test to complement these unit tests for the permit submission workflow? (Reference: `06-qa-testing/playwright-samples/tests/permit-submission.spec.ts`)

---

## End-of-Workshop Recap

Congratulations — you have completed all five exercises!

| Exercise | Skill Practiced |
|---|---|
| 01 | Customizing Copilot with always-on instructions and prompt files |
| 02 | Using Background Agent to implement a feature from a GitHub issue |
| 03 | Connecting GitHub Remote MCP and querying your repo from Chat |
| 04 | Modernising a .NET Framework project with Upgrade Assistant + Copilot |
| 05 | Generating and running a full xUnit test suite with Copilot |

### Next steps

- Review the full workshop module list in the [root README](../../README.md)
- Share your `prompt.md` files with your team using a shared GitHub repo
- Explore [Copilot Spaces](../../09-copilot-on-github/docs/copilot-spaces.md) for team knowledge sharing

---

## Troubleshooting

| Issue | Solution |
|---|---|
| `dotnet test` says "no test projects found" | Ensure you're in the `csharp-unit-tests` directory and it has a `.csproj` with `xunit` package |
| Moq throws `NotSupportedException` | You may be trying to mock a non-virtual method — check the interface definition |
| Test names don't match naming convention | Ask Copilot Chat: "Rename this test method to follow the pattern MethodName_StateUnderTest_ExpectedBehavior" |
| `/generate-tests` prompt not found | Ensure `.github/prompts/generate-tests.prompt.md` exists in the workspace |

---

## ✅ Workshop Complete

Well done! Return to your facilitator for the debrief session.
