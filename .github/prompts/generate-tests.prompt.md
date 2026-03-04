---
name: "Generate Tests"
description: "Generates xUnit + Moq unit tests for a selected C# class or method"
---

# Generate xUnit Tests

Generate a complete set of xUnit unit tests for the following C# code.

## Requirements

- Use **xUnit** as the test framework
- Use **Moq** for all dependencies
- Follow the naming pattern: `MethodName_StateUnderTest_ExpectedBehavior`
- Include **Arrange / Act / Assert** sections with comments
- Test each public method independently
- Cover:
  - Happy path (valid inputs, expected success)
  - Edge cases (null inputs, empty collections, boundary values)
  - Exception scenarios (what should throw and when)
- Use `[Theory]` with `[InlineData]` for parameterised tests where appropriate

## Code to Test

```csharp
${selection}
```

## Output Format

Provide:
1. The full test class with all using statements
2. A `// WHY:` comment on any non-obvious test case explaining what scenario it covers
3. A summary table at the end listing each method and what was covered
