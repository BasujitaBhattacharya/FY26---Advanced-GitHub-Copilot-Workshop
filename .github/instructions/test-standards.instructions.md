---
name: "Test Standards"
description: "xUnit + Moq test conventions for all C# test files"
applyTo: "**/*Tests.cs"
---

# Test Standards

## Framework

- Test framework: **xUnit**
- Mock library: **Moq**
- Assertions: xUnit built-in (`Assert.*`) — no FluentAssertions unless already in project

## Naming

Use the pattern: `MethodName_StateUnderTest_ExpectedBehavior`

```csharp
// Good
public async Task GetPermitAsync_ValidId_ReturnsPermit() { }
public async Task GetPermitAsync_NullId_ThrowsArgumentNullException() { }
public async Task GetPermitAsync_NotFound_ReturnsNull() { }
```

## Structure

```csharp
[Fact]
public async Task MethodName_State_Expected()
{
    // Arrange
    var mock = new Mock<IDependency>();
    mock.Setup(x => x.MethodAsync(It.IsAny<string>())).ReturnsAsync(value);

    // Act
    var result = await sut.MethodAsync("input");

    // Assert
    Assert.NotNull(result);
}
```

## Coverage Requirements

Each public method should have tests for:
1. Happy path
2. Null/empty/invalid inputs
3. Exception scenarios
4. Boundary conditions where applicable

## Parameterised Tests

Use `[Theory]` + `[InlineData]` for multiple similar cases.
