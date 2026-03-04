---
name: "SQL Query"
description: "Generates or optimises a T-SQL query for the OntarioPermits database schema"
---

# SQL Query Assistant

Help me write or optimise a T-SQL query for the **OntarioPermits** database.

## Schema Reference

```sql
-- See full schema: 07-databases/samples/schema.sql
Permits        (PermitId, ApplicantId, PermitTypeCode, StatusCode, RegionId, SubmittedDate, ProcessedDate, ExpiryDate, Notes)
Applicants     (ApplicantId, FirstName, LastName, Email, Phone, OrganizationName, CreatedDate)
StatusHistory  (HistoryId, PermitId, OldStatus, NewStatus, ChangedBy, ChangedDate, Reason)
Regions        (RegionId, RegionCode, RegionName, ManagerName, IsActive)
```

## My Request

${input:Describe the query you need — e.g. "Show all active permits per region for the last 30 days, ranked by volume"}

## Requirements

- Target: **T-SQL (SQL Server)**
- Prefer set-based operations over cursors or row-by-row processing
- Use CTEs for readability when the query has multiple logical steps
- Include column aliases that are descriptive
- Add a brief `-- Comment` above each CTE or major block explaining its purpose
- If optimising an existing query, show the before/after and explain each change

## Output Format

1. The complete T-SQL query
2. A brief explanation of the approach
3. (If relevant) An index recommendation to support this query
