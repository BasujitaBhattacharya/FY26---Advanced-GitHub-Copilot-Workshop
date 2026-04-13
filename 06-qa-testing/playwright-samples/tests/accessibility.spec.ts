import { test, expect } from '@playwright/test';
import AxeBuilder from '@axe-core/playwright';

/**
 * WCAG 2.1 AA accessibility tests for the Customerr mock portal.
 *
 * Copilot prompt used:
 * "Generate Playwright accessibility tests using @axe-core/playwright.
 *  Cover: WCAG 2.1 AA conformance of the permit form, keyboard navigation,
 *  and focus management after form submission. Target WCAG tags:
 *  wcag2a, wcag2aa, wcag21aa."
 *
 * Customer <Name> standard: AODA — WCAG 2.0 Level AA minimum.
 */
test.describe('Accessibility — WCAG 2.1 AA', () => {

  // ── Automated axe-core scan ────────────────────────────────────────────────

  test('permit submission form has no critical accessibility violations',
    async ({ page }) => {
      await page.goto('/');

      const results = await new AxeBuilder({ page })
        .withTags(['wcag2a', 'wcag2aa', 'wcag21aa'])
        .analyze();

      // Surface violations as readable output
      if (results.violations.length > 0) {
        const summary = results.violations.map(v =>
          `[${v.impact}] ${v.id}: ${v.description}\n  Nodes: ${v.nodes.map(n => n.target).join(', ')}`
        ).join('\n\n');
        throw new Error(`Accessibility violations found:\n\n${summary}`);
      }

      expect(results.violations).toHaveLength(0);
    }
  );

  // ── Keyboard navigation ────────────────────────────────────────────────────

  test('all form fields are reachable via keyboard Tab order',
    async ({ page }) => {
      await page.goto('/');

      // Tab through key elements in expected order
      await page.keyboard.press('Tab');
      await expect(page.getByLabel('Applicant Name')).toBeFocused();

      await page.keyboard.press('Tab');
      await expect(page.getByLabel('Email')).toBeFocused();

      await page.keyboard.press('Tab');
      await expect(page.getByLabel('Permit Type')).toBeFocused();

      await page.keyboard.press('Tab');
      await expect(page.getByLabel('Region')).toBeFocused();

      await page.keyboard.press('Tab');
      await expect(page.getByLabel('Project Description')).toBeFocused();

      await page.keyboard.press('Tab');
      await expect(page.getByRole('button', { name: 'Submit Application' })).toBeFocused();
    }
  );

  // ── Focus management on error ──────────────────────────────────────────────

  test('error messages are announced to assistive technology via role=alert',
    async ({ page }) => {
      await page.goto('/');

      // Trigger validation errors
      await page.getByRole('button', { name: 'Submit Application' }).click();

      // All error elements must have role="alert" for screen reader announcement
      const errorLocator = page.locator('[role="alert"].visible');
      const count = await errorLocator.count();
      expect(count).toBeGreaterThan(0);
    }
  );

  // ── Colour contrast ───────────────────────────────────────────────────────

  test('page passes colour contrast requirements (axe best-practice)',
    async ({ page }) => {
      await page.goto('/');

      const results = await new AxeBuilder({ page })
        .withTags(['cat.color'])
        .analyze();

      expect(results.violations).toHaveLength(0);
    }
  );

  // ── Lang attribute ────────────────────────────────────────────────────────

  test('html element has lang attribute set to en',
    async ({ page }) => {
      await page.goto('/');
      const lang = await page.locator('html').getAttribute('lang');
      expect(lang).toBe('en');
    }
  );

});
