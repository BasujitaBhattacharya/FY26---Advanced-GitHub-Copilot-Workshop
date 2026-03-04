import { test, expect } from '@playwright/test';

/**
 * Permit submission E2E tests.
 * Locators generated with Playwright MCP — see Module 03.
 *
 * Copilot prompt used:
 * "Using Playwright MCP, navigate to http://localhost:3000 and generate
 *  stable Playwright locators for all interactive elements on the permit
 *  submission form. Then generate a full test file covering the happy path,
 *  validation errors, and invalid email format."
 */
test.describe('Permit Submission Form', () => {

  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  // ── Happy path ─────────────────────────────────────────────────────────────

  test('should submit a valid construction permit and show confirmation',
    async ({ page }) => {
      // Fill the form using labels (accessibility-first locators)
      await page.getByLabel('Applicant Name').fill('Jane Doe');
      await page.getByLabel('Email').fill('jane.doe@ontario.ca');
      await page.getByLabel('Permit Type').selectOption('CONSTRUCTION');
      await page.getByLabel('Region').selectOption('TORONTO');
      await page.getByLabel('Project Description')
                .fill('Office renovation at 100 Queen St W, Toronto');

      // Submit
      await page.getByRole('button', { name: 'Submit Application' }).click();

      // Confirmation heading should appear
      await expect(page.getByRole('heading', { name: 'Application Submitted' }))
        .toBeVisible();

      // Confirmation number should match Ontario permit format P-######
      await expect(page.getByTestId('confirmation-number'))
        .toHaveText(/^P-\d{6}$/);

      // Form should be hidden after submission
      await expect(page.locator('#permit-form')).not.toBeVisible();
    }
  );

  // ── Validation — empty required fields ────────────────────────────────────

  test('should show validation errors when required fields are empty',
    async ({ page }) => {
      // Submit without filling anything
      await page.getByRole('button', { name: 'Submit Application' }).click();

      await expect(page.getByText('Applicant Name is required')).toBeVisible();
      await expect(page.getByText('Email is required')).toBeVisible();
      await expect(page.getByText('Permit Type is required')).toBeVisible();

      // Confirmation must NOT appear
      await expect(page.getByRole('heading', { name: 'Application Submitted' }))
        .not.toBeVisible();
    }
  );

  // ── Validation — email format ───────────────────────────────────────────────

  test('should reject an invalidly formatted email address',
    async ({ page }) => {
      await page.getByLabel('Applicant Name').fill('John Smith');
      await page.getByLabel('Email').fill('not-an-email@@bad');
      await page.getByLabel('Permit Type').selectOption('RENOVATION');

      await page.getByRole('button', { name: 'Submit Application' }).click();

      await expect(page.getByText('Please enter a valid email address'))
        .toBeVisible();
    }
  );

  // ── Validation — name only missing ────────────────────────────────────────

  test('should show only name error when only name is empty',
    async ({ page }) => {
      await page.getByLabel('Email').fill('valid@ontario.ca');
      await page.getByLabel('Permit Type').selectOption('ELECTRICAL');

      await page.getByRole('button', { name: 'Submit Application' }).click();

      await expect(page.getByText('Applicant Name is required')).toBeVisible();
      await expect(page.getByText('Email is required')).not.toBeVisible();
      await expect(page.getByText('Permit Type is required')).not.toBeVisible();
    }
  );

});
