import { test, expect } from '@playwright/test';

/**
 * Permit search E2E tests.
 *
 * Copilot prompt used:
 * "Generate Playwright tests for the permit search section. Cover:
 *  known permit found (shows status), unknown permit (shows error message),
 *  empty search submitted."
 */
test.describe('Permit Search', () => {

  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  // ── Known permits ──────────────────────────────────────────────────────────

  test.each([
    ['P-000001', 'ACTIVE'],
    ['P-000002', 'PENDING'],
    ['P-000003', 'APPROVED'],
  ])('should display status %s for permit %s',
    async ({ page }, permitId: string, expectedStatus: string) => {
      await page.getByLabel('Permit ID').fill(permitId);
      await page.getByRole('button', { name: 'Search' }).click();

      await expect(page.getByTestId('permit-status'))
        .toHaveText(expectedStatus);
    }
  );

  // ── Unknown permit ─────────────────────────────────────────────────────────

  test('should show no-results message for an unknown permit ID',
    async ({ page }) => {
      await page.getByLabel('Permit ID').fill('P-UNKNOWN');
      await page.getByRole('button', { name: 'Search' }).click();

      await expect(page.getByText('No permit found with this ID'))
        .toBeVisible();

      // Status element should not be visible
      await expect(page.getByTestId('permit-status')).not.toBeVisible();
    }
  );

  // ── Search is case-insensitive ─────────────────────────────────────────────

  test('should match permit ID regardless of case',
    async ({ page }) => {
      await page.getByLabel('Permit ID').fill('p-000001'); // lowercase
      await page.getByRole('button', { name: 'Search' }).click();

      await expect(page.getByTestId('permit-status'))
        .toHaveText('ACTIVE');
    }
  );

  // ── Empty search ───────────────────────────────────────────────────────────

  test('should show no-results for an empty search',
    async ({ page }) => {
      await page.getByRole('button', { name: 'Search' }).click();

      await expect(page.getByText('No permit found with this ID'))
        .toBeVisible();
    }
  );

});
