// csharp-unit-tests/PermitServiceTests.cs
// Copilot-generated xUnit + Moq test class for PermitService.
// Generated via: /generate-tests #file:PermitService.cs
// Manually reviewed and approved.

using Microsoft.Extensions.Logging;
using Moq;
using OntarioPermits.Models;
using OntarioPermits.Repositories;
using OntarioPermits.Services;

namespace OntarioPermits.Tests.Services;

public class PermitServiceTests
{
    // ──────────────────────────────────────────────────────────────────────────
    // Test fixture setup — Copilot identifies mocked dependencies from constructor
    // ──────────────────────────────────────────────────────────────────────────

    private readonly Mock<IPermitRepository>      _mockRepo;
    private readonly Mock<ILogger<PermitService>> _mockLogger;
    private readonly PermitService                _sut;

    public PermitServiceTests()
    {
        _mockRepo   = new Mock<IPermitRepository>();
        _mockLogger = new Mock<ILogger<PermitService>>();
        _sut        = new PermitService(_mockRepo.Object, _mockLogger.Object);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Constructor guard tests
    // ──────────────────────────────────────────────────────────────────────────

    [Fact]
    public void Constructor_WhenRepositoryIsNull_ThrowsArgumentNullException()
    {
        // Act & Assert
        Assert.Throws<ArgumentNullException>(
            () => new PermitService(null!, _mockLogger.Object));
    }

    [Fact]
    public void Constructor_WhenLoggerIsNull_ThrowsArgumentNullException()
    {
        // Act & Assert
        Assert.Throws<ArgumentNullException>(
            () => new PermitService(_mockRepo.Object, null!));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // SubmitAsync
    // ──────────────────────────────────────────────────────────────────────────

    [Fact]
    public async Task SubmitAsync_WhenRequestIsValid_ReturnsPendingResult()
    {
        // Arrange
        var request = new PermitRequest("REF-001", PermitType.Construction);
        _mockRepo.Setup(r => r.ExistsAsync("REF-001", It.IsAny<CancellationToken>()))
                 .ReturnsAsync(false);
        _mockRepo.Setup(r => r.SaveAsync(It.IsAny<Permit>(), It.IsAny<CancellationToken>()))
                 .ReturnsAsync(true);

        // Act
        var result = await _sut.SubmitAsync(request);

        // Assert
        Assert.Equal(PermitStatus.Pending, result.Status);
        Assert.False(string.IsNullOrWhiteSpace(result.PermitId));
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
        var request = new PermitRequest("REF-DUPE", PermitType.Renovation);
        _mockRepo.Setup(r => r.ExistsAsync("REF-DUPE", It.IsAny<CancellationToken>()))
                 .ReturnsAsync(true);

        // Act & Assert
        await Assert.ThrowsAsync<DuplicatePermitException>(
            () => _sut.SubmitAsync(request));

        // Verify we never attempted to save
        _mockRepo.Verify(
            r => r.SaveAsync(It.IsAny<Permit>(), It.IsAny<CancellationToken>()),
            Times.Never);
    }

    [Fact]
    public async Task SubmitAsync_WhenSaveFails_ThrowsPermitStorageException()
    {
        // Arrange
        var request = new PermitRequest("REF-002", PermitType.Construction);
        _mockRepo.Setup(r => r.ExistsAsync("REF-002", It.IsAny<CancellationToken>()))
                 .ReturnsAsync(false);
        _mockRepo.Setup(r => r.SaveAsync(It.IsAny<Permit>(), It.IsAny<CancellationToken>()))
                 .ReturnsAsync(false); // simulate DB failure

        // Act & Assert
        await Assert.ThrowsAsync<PermitStorageException>(
            () => _sut.SubmitAsync(request));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // GetStatusAsync
    // ──────────────────────────────────────────────────────────────────────────

    [Fact]
    public async Task GetStatusAsync_WhenPermitExists_ReturnsCorrectStatus()
    {
        // Arrange
        var permit = new Permit { PermitId = "P-001", Status = PermitStatus.Approved };
        _mockRepo.Setup(r => r.FindByIdAsync("P-001", It.IsAny<CancellationToken>()))
                 .ReturnsAsync(permit);

        // Act
        var status = await _sut.GetStatusAsync("P-001");

        // Assert
        Assert.Equal(PermitStatus.Approved, status);
    }

    [Fact]
    public async Task GetStatusAsync_WhenPermitNotFound_ReturnsNull()
    {
        // Arrange
        _mockRepo.Setup(r => r.FindByIdAsync("UNKNOWN", It.IsAny<CancellationToken>()))
                 .ReturnsAsync((Permit?)null);

        // Act
        var status = await _sut.GetStatusAsync("UNKNOWN");

        // Assert
        Assert.Null(status);
    }

    [Fact]
    public async Task GetStatusAsync_WhenPermitIdIsNull_ThrowsArgumentNullException()
    {
        // Act & Assert
        await Assert.ThrowsAsync<ArgumentNullException>(
            () => _sut.GetStatusAsync(null!));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // CancelAsync
    // ──────────────────────────────────────────────────────────────────────────

    [Fact]
    public async Task CancelAsync_WhenPermitIsPending_ReturnsTrueAndSetsStatusCancelled()
    {
        // Arrange
        var permit = new Permit { PermitId = "P-002", Status = PermitStatus.Pending };
        _mockRepo.Setup(r => r.FindByIdAsync("P-002", It.IsAny<CancellationToken>()))
                 .ReturnsAsync(permit);
        _mockRepo.Setup(r => r.UpdateAsync(It.IsAny<Permit>(), It.IsAny<CancellationToken>()))
                 .ReturnsAsync(true);

        // Act
        var result = await _sut.CancelAsync("P-002");

        // Assert
        Assert.True(result);
        _mockRepo.Verify(
            r => r.UpdateAsync(
                It.Is<Permit>(p => p.Status == PermitStatus.Cancelled),
                It.IsAny<CancellationToken>()),
            Times.Once);
    }

    [Fact]
    public async Task CancelAsync_WhenPermitIsApproved_ReturnsFalseWithNoUpdate()
    {
        // Arrange
        var permit = new Permit { PermitId = "P-003", Status = PermitStatus.Approved };
        _mockRepo.Setup(r => r.FindByIdAsync("P-003", It.IsAny<CancellationToken>()))
                 .ReturnsAsync(permit);

        // Act
        var result = await _sut.CancelAsync("P-003");

        // Assert
        Assert.False(result);
        _mockRepo.Verify(
            r => r.UpdateAsync(It.IsAny<Permit>(), It.IsAny<CancellationToken>()),
            Times.Never);
    }

    [Fact]
    public async Task CancelAsync_WhenPermitNotFound_ReturnsFalse()
    {
        // Arrange
        _mockRepo.Setup(r => r.FindByIdAsync("P-GONE", It.IsAny<CancellationToken>()))
                 .ReturnsAsync((Permit?)null);

        // Act
        var result = await _sut.CancelAsync("P-GONE");

        // Assert
        Assert.False(result);
    }

    [Fact]
    public async Task CancelAsync_WhenPermitIdIsNull_ThrowsArgumentNullException()
    {
        // Act & Assert
        await Assert.ThrowsAsync<ArgumentNullException>(
            () => _sut.CancelAsync(null!));
    }
}
