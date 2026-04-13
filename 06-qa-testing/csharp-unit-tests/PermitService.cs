// csharp-unit-tests/PermitService.cs
// Service Under Test (SUT) for Module 06 — QA & Testing
// This is the implementation that Copilot will generate tests against.

using CustomerPermits.Models;
using CustomerPermits.Repositories;

namespace CustomerPermits.Services;

/// <summary>Handles business logic for permit submissions and status tracking.</summary>
public sealed class PermitService : IPermitService
{
    private readonly IPermitRepository _repository;
    private readonly ILogger<PermitService> _logger;

    /// <summary>Initialises a new instance of <see cref="PermitService"/>.</summary>
    public PermitService(IPermitRepository repository, ILogger<PermitService> logger)
    {
        ArgumentNullException.ThrowIfNull(repository);
        ArgumentNullException.ThrowIfNull(logger);
        _repository = repository;
        _logger = logger;
    }

    /// <inheritdoc />
    public async Task<PermitResult> SubmitAsync(PermitRequest request, CancellationToken ct = default)
    {
        ArgumentNullException.ThrowIfNull(request);

        _logger.LogInformation("Submitting permit for reference {Reference}", request.Reference);

        if (await _repository.ExistsAsync(request.Reference, ct))
        {
            throw new DuplicatePermitException(request.Reference);
        }

        var permit = new Permit
        {
            PermitId = Guid.NewGuid().ToString("N")[..8].ToUpper(),
            Reference = request.Reference,
            Type = request.Type,
            Status = PermitStatus.Pending,
            CreatedAt = DateTimeOffset.UtcNow,
        };

        var saved = await _repository.SaveAsync(permit, ct);
        if (!saved)
        {
            throw new PermitStorageException("Failed to persist permit.");
        }

        return new PermitResult(permit.PermitId, PermitStatus.Pending);
    }

    /// <inheritdoc />
    public async Task<PermitStatus?> GetStatusAsync(string permitId, CancellationToken ct = default)
    {
        ArgumentNullException.ThrowIfNull(permitId);

        var permit = await _repository.FindByIdAsync(permitId, ct);
        return permit?.Status;
    }

    /// <inheritdoc />
    public async Task<bool> CancelAsync(string permitId, CancellationToken ct = default)
    {
        ArgumentNullException.ThrowIfNull(permitId);

        var permit = await _repository.FindByIdAsync(permitId, ct);
        if (permit is null)
            return false;

        if (permit.Status != PermitStatus.Pending)
        {
            _logger.LogWarning(
                "Cannot cancel permit {PermitId} — current status is {Status}",
                permitId, permit.Status);
            return false;
        }

        permit.Status = PermitStatus.Cancelled;
        return await _repository.UpdateAsync(permit, ct);
    }
}
