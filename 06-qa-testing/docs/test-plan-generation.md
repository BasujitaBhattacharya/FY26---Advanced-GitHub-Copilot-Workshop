# Test Plan Generation with Copilot Agent Mode

> **Outcome:** Turn a C# interface or business requirement into a structured test plan and working xUnit tests — entirely within VS Code, using Agent mode + the `generate-tests` prompt file.

---

## Why Agent Mode for Test Plans?

Agent mode can:

1. Read the source interface **and** its dependencies automatically (`#file` or workspace indexing)
2. Reason about all methods, their parameters, and return types
3. Identify happy paths, null-guard paths, boundary conditions, and integration failure scenarios
4. Generate the full test class
5. Run `dotnet test` and iterate until all tests pass

---

## Worked Example — `IPermitSubmissionService`

### Source interface

```csharp
// Services/IPermitSubmissionService.cs
namespace OntarioPermits.Services;

/// <summary>Handles submission and tracking of permit applications.</summary>
public interface IPermitSubmissionService
{
    /// <summary>Submits a new permit application.</summary>
    /// <param name="request">The permit request payload.</param>
    /// <param name="cancellationToken">Cancellation token.</param>
    /// <returns>The created permit with its assigned ID and initial status.</returns>
    /// <exception cref="ArgumentNullException">
    ///   Thrown when <paramref name="request"/> is null.
    /// </exception>
    /// <exception cref="DuplicatePermitException">
    ///   Thrown when a permit with the same reference already exists.
    /// </exception>
    Task<PermitResult> SubmitAsync(PermitRequest request, CancellationToken cancellationToken = default);

    /// <summary>Retrieves the current status of a permit.</summary>
    Task<PermitStatus?> GetStatusAsync(string permitId, CancellationToken cancellationToken = default);

    /// <summary>Cancels a permit if it is still in Pending status.</summary>
    Task<bool> CancelAsync(string permitId, CancellationToken cancellationToken = default);
}
```

---

### Step 1 — Generate the test plan

Open Copilot Chat in Agent mode, then run:

```text
/generate-tests
#file:Services/IPermitSubmissionService.cs
#file:Services/PermitSubmissionService.cs
```

The prompt file (`generate-tests.prompt.md`) instructs Copilot to:
1. List all test **cases** as a markdown table (method × scenario)
2. Identify mocked dependencies
3. Generate the full `PermitSubmissionServiceTests.cs` class
4. Run `dotnet test` and fix failures

---

### Step 2 — Review the generated test plan (Copilot output)

Copilot will produce a table similar to:

| Method | Scenario | Expected Outcome |
|---|---|---|
| `SubmitAsync` | Valid request | Returns `PermitResult` with `Status = Pending` |
| `SubmitAsync` | Null request | Throws `ArgumentNullException` |
| `SubmitAsync` | Duplicate reference | Throws `DuplicatePermitException` |
| `SubmitAsync` | Repository save fails | Throws `PermitStorageException` |
| `SubmitAsync` | Cancellation requested | `OperationCanceledException` propagated |
| `GetStatusAsync` | Permit found | Returns correct `PermitStatus` |
| `GetStatusAsync` | Permit not found | Returns `null` |
| `GetStatusAsync` | Null permit ID | Throws `ArgumentNullException` |
| `CancelAsync` | Permit in Pending | Returns `true`, status updated to Cancelled |
| `CancelAsync` | Permit already Approved | Returns `false`, no status change |
| `CancelAsync` | Permit not found | Returns `false` |
| `CancelAsync` | Null permit ID | Throws `ArgumentNullException` |

---

### Step 3 — Review the generated test class

```csharp
// PermitSubmissionServiceTests.cs (Copilot-generated — review before committing)
using Moq;
using OntarioPermits.Repositories;
using OntarioPermits.Services;
using OntarioPermits.Models;

namespace OntarioPermits.Tests.Services;

public class PermitSubmissionServiceTests
{
    private readonly Mock<IPermitRepository> _mockRepo;
    private readonly PermitSubmissionService _sut;

    public PermitSubmissionServiceTests()
    {
        _mockRepo = new Mock<IPermitRepository>();
        _sut      = new PermitSubmissionService(_mockRepo.Object);
    }

    // ─── SubmitAsync ───────────────────────────────────────────────────────────

    [Fact]
    public async Task SubmitAsync_WhenRequestIsValid_ReturnsPendingPermit()
    {
        // Arrange
        var request = new PermitRequest("REF-001", PermitType.Construction);
        _mockRepo.Setup(r => r.ExistsAsync("REF-001")).ReturnsAsync(false);
        _mockRepo.Setup(r => r.SaveAsync(It.IsAny<Permit>())).ReturnsAsync(true);

        // Act
        var result = await _sut.SubmitAsync(request);

        // Assert
        result.Status.Should().Be(PermitStatus.Pending);
        result.PermitId.Should().NotBeNullOrEmpty();
    }

    [Fact]
    public async Task SubmitAsync_WhenRequestIsNull_ThrowsArgumentNullException()
    {
        // Act & Assert
        await Assert.ThrowsAsync<ArgumentNullException>(
            () => _sut.SubmitAsync(null!));
    }

    [Fact]
    public async Task SubmitAsync_WhenDuplicateReference_ThrowsDuplicatePermitException()
    {
        // Arrange
        var request = new PermitRequest("REF-001", PermitType.Construction);
        _mockRepo.Setup(r => r.ExistsAsync("REF-001")).ReturnsAsync(true);

        // Act & Assert
        await Assert.ThrowsAsync<DuplicatePermitException>(
            () => _sut.SubmitAsync(request));
    }

    // ─── GetStatusAsync ────────────────────────────────────────────────────────

    [Fact]
    public async Task GetStatusAsync_WhenPermitFound_ReturnsStatus()
    {
        // Arrange
        _mockRepo.Setup(r => r.FindByIdAsync("P-001"))
                 .ReturnsAsync(new Permit("P-001", PermitStatus.Approved));

        // Act
        var status = await _sut.GetStatusAsync("P-001");

        // Assert
        status.Should().Be(PermitStatus.Approved);
    }

    [Fact]
    public async Task GetStatusAsync_WhenPermitNotFound_ReturnsNull()
    {
        // Arrange
        _mockRepo.Setup(r => r.FindByIdAsync("UNKNOWN")).ReturnsAsync((Permit?)null);

        // Act
        var status = await _sut.GetStatusAsync("UNKNOWN");

        // Assert
        status.Should().BeNull();
    }

    // ─── CancelAsync ───────────────────────────────────────────────────────────

    [Fact]
    public async Task CancelAsync_WhenPermitIsPending_ReturnsTrueAndUpdatesStatus()
    {
        // Arrange
        var permit = new Permit("P-001", PermitStatus.Pending);
        _mockRepo.Setup(r => r.FindByIdAsync("P-001")).ReturnsAsync(permit);
        _mockRepo.Setup(r => r.UpdateAsync(It.IsAny<Permit>())).ReturnsAsync(true);

        // Act
        var result = await _sut.CancelAsync("P-001");

        // Assert
        result.Should().BeTrue();
        _mockRepo.Verify(r => r.UpdateAsync(
            It.Is<Permit>(p => p.Status == PermitStatus.Cancelled)), Times.Once);
    }

    [Fact]
    public async Task CancelAsync_WhenPermitIsApproved_ReturnsFalseWithNoUpdate()
    {
        // Arrange
        _mockRepo.Setup(r => r.FindByIdAsync("P-002"))
                 .ReturnsAsync(new Permit("P-002", PermitStatus.Approved));

        // Act
        var result = await _sut.CancelAsync("P-002");

        // Assert
        result.Should().BeFalse();
        _mockRepo.Verify(r => r.UpdateAsync(It.IsAny<Permit>()), Times.Never);
    }
}
```

---

### Step 4 — Run and iterate

In Agent mode, Copilot will automatically run:

```bash
dotnet test --filter "FullyQualifiedName~PermitSubmissionServiceTests"
```

If tests fail, Copilot will show you the failure, propose a fix, apply it, and re-run — without you needing to intervene.

---

## Prompts for Extending the Test Plan

| Goal | Prompt |
|---|---|
| Add edge-case coverage | "What test cases are missing to reach 90% branch coverage of SubmitAsync?" |
| Add cancellation tests | "Add a [Theory] that verifies all three methods respect CancellationToken" |
| Add performance test | "Generate a benchmark using BenchmarkDotNet for SubmitAsync with 1000 concurrent calls" |
| Add mutation test note | "Identify 3 mutation testing scenarios for the CancelAsync guard logic" |

---

## Anti-Patterns Copilot Will Avoid (per repo instructions)

| Anti-pattern | What Copilot does instead |
|---|---|
| `.Result`/`.Wait()` in async tests | Uses `await` throughout |
| Multiple assertions per `[Fact]` | Splits into separate `[Fact]` methods |
| Magic strings for permit IDs | Uses named constants or well-named variables |
| Testing private methods directly | Tests via the public interface |
| Mocking concrete classes | Mocks interfaces only |

---

## Related

- [Testing Strategies](testing-strategies.md) — Layer overview
- [Playwright UI Testing](playwright-ui-testing.md) — E2E test generation
- [generate-tests prompt file](../../.github/prompts/generate-tests.prompt.md)
