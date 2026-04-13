-- =============================================================================
-- CustomerPermits — Sample Queries
-- Module 07 — Databases with GitHub Copilot
--
-- Each query was generated or refined using Copilot Chat.
-- The Copilot prompt is shown above each query.
-- =============================================================================

USE CustomerPermits;
GO

-- =============================================================================
-- Query 01 — All active permits by region
--
-- Copilot prompt: "List all permits that are not CANCELLED or REJECTED,
-- grouped by region, with a count. Order by count descending."
-- =============================================================================
SELECT
    r.Name                              AS Region,
    COUNT(p.PermitId)                   AS ActivePermitCount
FROM        dbo.Permits p
INNER JOIN  dbo.Regions r ON r.RegionId = p.RegionId
WHERE       p.Status NOT IN (N'CANCELLED', N'REJECTED')
GROUP BY    r.Name
ORDER BY    ActivePermitCount DESC;
GO

-- =============================================================================
-- Query 02 — Permits pending review for more than 30 days
--
-- Copilot prompt: "Find all permits in PENDING status that were submitted
-- more than 30 days ago. Show permit ID, applicant name, region, and days
-- since submission."
-- =============================================================================
SELECT
    p.PermitId,
    a.FullName                          AS ApplicantName,
    r.Name                              AS Region,
    p.Type,
    DATEDIFF(DAY, p.SubmittedAt, SYSUTCDATETIME()) AS DaysPending
FROM        dbo.Permits   p
INNER JOIN  dbo.Applicants a ON a.ApplicantId = p.ApplicantId
INNER JOIN  dbo.Regions    r ON r.RegionId    = p.RegionId
WHERE       p.Status = N'PENDING'
  AND       p.SubmittedAt < DATEADD(DAY, -30, SYSUTCDATETIME())
ORDER BY    DaysPending DESC;
GO

-- =============================================================================
-- Query 03 — Permit status distribution (percentage breakdown)
--
-- Copilot prompt: "Show each status, the count of permits in that status,
-- and its percentage of total permits. Round percentages to 1 decimal place."
-- =============================================================================
SELECT
    p.Status,
    COUNT(*)                                                    AS PermitCount,
    CAST(
        100.0 * COUNT(*) / SUM(COUNT(*)) OVER ()
    AS DECIMAL(5,1))                                            AS PctOfTotal
FROM  dbo.Permits p
GROUP BY p.Status
ORDER BY PermitCount DESC;
GO

-- =============================================================================
-- Query 04 — Full audit trail for a specific permit
--
-- Copilot prompt: "Show the complete status history for permit P-000001,
-- including who made each change and any notes, ordered by change date."
-- =============================================================================
DECLARE @PermitId NVARCHAR(20) = N'P-000001';

SELECT
    sh.ChangedAt,
    ISNULL(sh.OldStatus, N'(new)')  AS OldStatus,
    sh.NewStatus,
    sh.ChangedBy,
    sh.Notes
FROM       dbo.StatusHistory sh
WHERE      sh.PermitId = @PermitId
ORDER BY   sh.ChangedAt ASC;
GO

-- =============================================================================
-- Query 05 — Applicants with multiple permits
--
-- Copilot prompt: "Find applicants who have submitted 2 or more permits of
-- any status. Show their name, email, and permit count."
-- =============================================================================
SELECT
    a.FullName,
    a.Email,
    COUNT(p.PermitId)   AS PermitCount
FROM       dbo.Applicants a
INNER JOIN dbo.Permits    p ON p.ApplicantId = a.ApplicantId
GROUP BY   a.ApplicantId, a.FullName, a.Email
HAVING     COUNT(p.PermitId) >= 2
ORDER BY   PermitCount DESC;
GO

-- =============================================================================
-- Query 06 — Permits approved within the last 12 months by type
--
-- Copilot prompt: "Show counts of approved permits grouped by permit type
-- for the last 12 months. Include a column showing the percentage of total
-- approved permits that each type represents."
-- =============================================================================
SELECT
    p.Type,
    COUNT(*)                                            AS ApprovedCount,
    CAST(
        100.0 * COUNT(*) / SUM(COUNT(*)) OVER ()
    AS DECIMAL(5,1))                                    AS PctOfApproved
FROM  dbo.Permits p
WHERE p.Status = N'APPROVED'
  AND p.UpdatedAt >= DATEADD(MONTH, -12, SYSUTCDATETIME())
GROUP BY p.Type
ORDER BY ApprovedCount DESC;
GO

-- =============================================================================
-- Query 07 — Average processing time from submission to approval
--
-- Copilot prompt: "Calculate the average number of days from permit
-- submission to the APPROVED status change, by permit type."
-- =============================================================================
SELECT
    p.Type,
    AVG(DATEDIFF(DAY, p.SubmittedAt, sh.ChangedAt))    AS AvgDaysToApproval,
    COUNT(*)                                            AS SampleSize
FROM        dbo.Permits       p
INNER JOIN  dbo.StatusHistory sh
    ON  sh.PermitId  = p.PermitId
    AND sh.NewStatus = N'APPROVED'
GROUP BY    p.Type
ORDER BY    AvgDaysToApproval;
GO

-- =============================================================================
-- Query 08 — Monthly submission trend (last 6 months)
--
-- Copilot prompt: "Show the number of permits submitted per month for the
-- last 6 months, including months with zero submissions. Display as
-- YYYY-MM labels ordered chronologically."
-- =============================================================================
WITH Months AS (
    SELECT DATEADD(MONTH, -n, DATEFROMPARTS(YEAR(SYSUTCDATETIME()), MONTH(SYSUTCDATETIME()), 1)) AS MonthStart
    FROM (VALUES (0),(1),(2),(3),(4),(5)) AS v(n)
)
SELECT
    FORMAT(m.MonthStart, 'yyyy-MM')             AS [Month],
    COUNT(p.PermitId)                           AS SubmissionCount
FROM       Months m
LEFT JOIN  dbo.Permits p
    ON  p.SubmittedAt >= m.MonthStart
    AND p.SubmittedAt <  DATEADD(MONTH, 1, m.MonthStart)
GROUP BY   m.MonthStart
ORDER BY   m.MonthStart ASC;
GO

-- =============================================================================
-- Query 09 — Permits with no status history (data quality check)
--
-- Copilot prompt: "Find any permits that have no record in StatusHistory.
-- This indicates missing audit trail data and should be investigated."
-- =============================================================================
SELECT
    p.PermitId,
    p.ApplicantId,
    p.Status,
    p.SubmittedAt
FROM       dbo.Permits p
WHERE NOT EXISTS (
    SELECT 1
    FROM   dbo.StatusHistory sh
    WHERE  sh.PermitId = p.PermitId
);
GO

-- =============================================================================
-- Query 10 — Region performance dashboard (executive summary)
--
-- Copilot prompt: "Create a region dashboard showing: region name, total
-- permits, pending count, approved count, rejected count, and approval rate
-- percentage. Sort by total permits descending."
-- =============================================================================
SELECT
    r.Name                                                          AS Region,
    COUNT(p.PermitId)                                               AS TotalPermits,
    SUM(CASE WHEN p.Status = N'PENDING'      THEN 1 ELSE 0 END)    AS Pending,
    SUM(CASE WHEN p.Status = N'UNDER_REVIEW' THEN 1 ELSE 0 END)    AS UnderReview,
    SUM(CASE WHEN p.Status = N'APPROVED'     THEN 1 ELSE 0 END)    AS Approved,
    SUM(CASE WHEN p.Status = N'REJECTED'     THEN 1 ELSE 0 END)    AS Rejected,
    SUM(CASE WHEN p.Status = N'CANCELLED'    THEN 1 ELSE 0 END)    AS Cancelled,
    CAST(
        100.0
        * SUM(CASE WHEN p.Status = N'APPROVED' THEN 1 ELSE 0 END)
        / NULLIF(COUNT(p.PermitId), 0)
    AS DECIMAL(5,1))                                                AS ApprovalRatePct
FROM       dbo.Regions  r
LEFT JOIN  dbo.Permits  p ON p.RegionId = r.RegionId
WHERE      r.IsActive = 1
GROUP BY   r.RegionId, r.Name
ORDER BY   TotalPermits DESC;
GO
