# EaseMyTrade Playwright Framework — Technical Interview Questions

> A framework-level Q&A walkthrough of the **EaseMyTrade Playwright + Java + TestNG** automation
> suite. Questions are ordered to follow the natural **execution flow** of the framework — from
> "what is it built on" → "how does a test boot" → "how does a single test run end to end" →
> "how are results reported and run in CI". Use it for interview prep, onboarding, or as a design
> reference.
>
> Companion docs: [`README.md`](README.md) (quick start), [`FRAMEWORK_GUIDE.md`](FRAMEWORK_GUIDE.md)
> (full reference), [`PLAYWRIGHT_GUIDE.md`](PLAYWRIGHT_GUIDE.md) (Playwright tutorial / cheatsheet).

---

## Table of Contents

1. [Stack & Tooling](#1-stack--tooling)
2. [Architecture & Design](#2-architecture--design)
3. [The Page Object Model Layer](#3-the-page-object-model-layer)
4. [Configuration & Environment](#4-configuration--environment)
5. [Test Lifecycle — How a Test Boots and Runs (the Flow)](#5-test-lifecycle--how-a-test-boots-and-runs-the-flow)
6. [Playwright Mechanics in This Framework](#6-playwright-mechanics-in-this-framework)
7. [Authentication & Protected Pages](#7-authentication--protected-pages)
8. [Reliability — Retries, Waits, Soft-Fail & Skips](#8-reliability--retries-waits-soft-fail--skips)
9. [API-Level Testing](#9-api-level-testing)
10. [Signal Logic — Business Validation](#10-signal-logic--business-validation)
11. [Data-Driven Testing](#11-data-driven-testing)
12. [Reporting — Allure](#12-reporting--allure)
13. [Logging](#13-logging)
14. [CI/CD — GitHub Actions](#14-cicd--github-actions)
15. [Scenario / "What Would You Do" Questions](#15-scenario--what-would-you-do-questions)

---

## 1. Stack & Tooling

**Q1. What is the technology stack of this framework and why these choices?**
- **Playwright for Java 1.47.0** — browser automation (Chromium, Firefox, WebKit from one API).
- **TestNG 7.9.0** — test runner (annotations, suite ordering, data providers, parallelism).
- **Allure 2.27.0** — rich HTML reporting with steps, attachments, history/trend.
- **AssertJ 3.25.3** — fluent, readable assertions with descriptive `.as(...)` messages.
- **Jackson 2.17.1** — JSON parsing for API-level signal validation.
- **SLF4J + Logback** — logging facade + implementation.
- **AspectJ Weaver 1.9.22** — load-time weaving so Allure's `@Step` annotations are captured.
- **Maven** — build, dependency management, system-property injection.

**Q2. Why Playwright over Selenium for this project?**
- Auto-waiting / actionability checks built in — fewer explicit waits and flakiness.
- A single API drives all three engines (Chromium/Firefox/WebKit); no separate driver binaries.
- First-class `APIRequestContext` for hitting JSON endpoints in the *same* session — this suite
  validates signal logic directly against `/data/market.json` and `/api/live-market`.
- Browser **contexts** are cheap and fully isolated (own cookies/storage), which maps perfectly to
  "one fresh context per test".

**Q3. Why TestNG instead of JUnit?**
- `testng.xml` controls suite composition and **execution order** (`preserve-order="true"`).
- Rich `@DataProvider` support for data-driven tests.
- Lifecycle granularity: `@BeforeSuite` / `@BeforeMethod` / `@AfterMethod` / `@AfterSuite` map
  cleanly to "browser once, context per test".
- `SkipException` for first-class conditional skipping (used for the login-gate).

**Q4. What role does AspectJ play? What breaks without it?**
Allure's `@Step` annotation is implemented via aspects. The `aspectjweaver` java agent is attached
in the Surefire `<argLine>`. Without it, `@Step`-annotated methods would still execute but **would
not appear as steps** in the Allure report — you'd lose the step-by-step breadcrumb tree.

---

## 2. Architecture & Design

**Q5. Describe the layered architecture.**
```
Test Classes (one per feature)         e.g. SignalLogicTest, HomePageTest
        │ extends
BaseTest                               Playwright lifecycle, context per test, failure capture
        │ uses
Page Objects (BasePage + subclasses)   locators + @Step actions + state readers
        │ uses
Utilities (SignalParser, AllureAttachmentHelper)
        │ reads
TestConfig                             URLs, timeouts, thresholds, credentials
        │ produces
Allure Report                          Epic→Feature→Story→Test→Step→Attachments
```

**Q6. What are the key design decisions and their rationale?**

| Decision | Rationale |
|---|---|
| One `Browser` per suite, one `BrowserContext` per test | Browser launch is expensive (do once); context is cheap and isolates cookies/storage to prevent test pollution. |
| `@Step` on every page action | Allure shows the exact action sequence without reading code. |
| Auto screenshot + page source on failure | Failures are self-diagnosable; no need to re-run locally to see what happened. |
| `SignalParser` static utility | All numeric parsing + financial logic in one place, unit-testable in isolation. |
| Soft-fail for live Expert View scans | Those depend on third-party APIs (Yahoo/NSE); a network blip shouldn't fail the suite. |
| `categories.json` | Buckets failures into Product Defect / Signal Logic / Network / Infra in Allure. |

**Q7. Why is `SignalParser` stateless with only static methods?**
It holds pure functions (parse a price, compute R:R, check geometry). No shared state means no
concurrency concerns under parallel execution, and each method is independently testable. The
private constructor (`private SignalParser() {}`) prevents instantiation.

**Q8. How would parallel execution affect this design?**
`Browser` is a `static` shared field — safe to share read-only across threads. `context` and `page`
are **instance** fields created fresh per `@BeforeMethod`, so each test thread gets its own isolated
browser context. As long as page objects don't share mutable state (they don't — they only hold a
`Page` reference), TestNG method/class-level parallelism is safe.

---

## 3. The Page Object Model Layer

**Q9. What does `BasePage` provide and why is it abstract?**
`BasePage` is `abstract` — it's never instantiated directly. It holds the shared `Page` reference,
a per-class `Logger`, common header/footer/nav locators, shared `@Step` actions
(`openIndianIndicesDropdown`, `clickBrandLogo`), and defensive helpers (`textOf`, `attrOf`,
`isVisible`, `waitForVisible`). Every concrete page (`HomePage`, `LoginPage`, …) extends it.

**Q10. Why do locators return `Locator` objects rather than resolve immediately?**
```java
public Locator headerLogo() { return page.locator(".brand"); }
```
Playwright `Locator`s are **lazy** — they describe *how to find* an element, not a snapshot of it.
Resolution + auto-waiting happens at action time (`.click()`, `.innerText()`). This makes locators
reusable and resistant to staleness (unlike Selenium `WebElement`).

**Q11. Why are `textOf()` and `isVisible()` wrapped in try/catch returning a default?**
```java
public boolean isVisible(Locator locator) {
    try { return locator.isVisible(); }
    catch (Exception e) { return false; }   // treat "not found" as not visible
}
```
On a live, data-driven dashboard some elements may not be present yet. These read helpers degrade
gracefully (empty string / `false`) and log at debug, so a *missing* element doesn't throw — the
*test's* assertion decides whether that's a failure, keeping the failure semantics in the test, not
the helper.

**Q12. Why does `IndexPage` use multi-selector readers like `[id*='confidence'], [class*='confidence'], [data-confidence]`?**
The same logical value (signal state, confidence) is rendered with different DOM patterns across
index pages. A comma-separated selector list tries multiple patterns and takes `.first()` match,
making the reader resilient to per-page markup differences without duplicating page objects.

**Q13. How is a page object constructed and used in a test?**
```java
@BeforeMethod
public void openHomePage() {
    navigateTo(TestConfig.HOME_URL);   // BaseTest helper
    homePage = new HomePage(page);     // wrap the live Page
}
```
The page object is a thin wrapper over the already-navigated `Page`. Tests then call semantic
methods (`homePage.niftyZoneSupport()`) instead of raw selectors.

---

## 4. Configuration & Environment

**Q14. How is configuration injected? Walk through `app.base.url`.**
Maven `pom.xml` declares the property and pipes it into Surefire's `systemPropertyVariables`.
`TestConfig` reads it: `System.getProperty("app.base.url", "https://easemytrade.in")`. Override at
runtime with `mvn test -Dapp.base.url=http://localhost:4173`. Same pattern for `browser` and
`headless`.

**Q15. How are credentials resolved, and why two sources?**
```java
TEST_USERNAME = resolveCredential("test.username", "TEST_VIEWER_USERNAME");
```
`resolveCredential` checks the **system property** first (`-Dtest.username=...` for local runs),
then falls back to an **environment variable** (`TEST_VIEWER_USERNAME`, set as a CI secret).
If both are blank it returns `""`, and the framework gracefully skips protected-page tests rather
than failing. This keeps secrets out of source and supports both local and CI usage.

**Q16. What are the three timeout tiers and why three?**

| Constant | Value | Used for |
|---|---|---|
| `DEFAULT_TIMEOUT_MS` | 30s | Normal element waits |
| `NAVIGATION_TIMEOUT_MS` | 60s | Page navigations (can chain redirects) |
| `SLOW_RESOURCE_TIMEOUT_MS` | 120s | Live API scans (Yahoo/NSE are slow & rate-limited) |

Different operations have genuinely different latency profiles; one global timeout would either be
too tight for live scans or too loose for everything else.

**Q17. Where do the business thresholds live?**
In `TestConfig`: `MIN_RISK_REWARD_RATIO = 2.2`, `MIN/MAX_CONFIDENCE_SCORE = 0/100`,
`MIDDAY_MIN_CONFIDENCE = 90`, and `VALID_SIGNAL_STATES[]`. Centralizing them means a rule change is a
one-line edit, not a hunt through test code.

---

## 5. Test Lifecycle — How a Test Boots and Runs (the Flow)

> This is the heart of "explain the flow." Trace one test from suite start to teardown.

**Q18. Walk through the complete lifecycle of a test run, start to finish.**

1. **`@BeforeSuite launchBrowser()`** (once):
   - `Playwright.create()` boots the Playwright driver process.
   - Reads `TestConfig.BROWSER` and launches the matching engine via a `switch` expression, with
     `setHeadless()` and `setSlowMo(headless ? 0 : 50)` (50ms slow-mo when visible, for debugging).
   - Calls `loginAsTestViewer()` to obtain a session cookie (see §7).
2. **`@BeforeMethod createContext(Method)`** (per test):
   - `browser.newContext(...)` with viewport 1440×900, **dark** color scheme, `en-IN` locale,
     `Asia/Kolkata` timezone, `ignoreHTTPSErrors`.
   - Sets default + navigation timeouts on the context.
   - Conditionally adds the session cookie if `useAuthenticatedSession()` is true.
   - `context.newPage()` then attaches **event listeners** (`onPageError`, `onConsoleMessage`,
     `onRequest`, `onResponse`) that log into the framework's logger.
3. **The test's own `@BeforeMethod`** (e.g. `openHomePage()`) navigates and builds the page object.
4. **The `@Test` method** executes: page object actions → state reads → AssertJ assertions →
   Allure attachments.
5. **`@AfterMethod tearDown(ITestResult)`** (per test):
   - Logs PASS/FAIL/SKIP.
   - On **failure**: attaches screenshot, page source, and the exception text to Allure.
   - On **success**: attaches a final-state screenshot.
   - Closes `page` then `context` (frees the per-test isolation).
6. **`@AfterSuite closeBrowser()`** (once): closes `browser` and `playwright`.

**Q19. Why `alwaysRun = true` on the lifecycle methods?**
It guarantees setup/teardown runs even when groups/filters are applied or when a prior config method
fails — critical so the browser always launches and always closes (no leaked processes).

**Q20. Why is the browser launched once but the context created per test?**
Launching a browser is expensive (spawns a real engine); doing it per test would be slow. A
`BrowserContext` is lightweight and gives each test a clean cookie/storage jar — equivalent to a
fresh incognito window — preventing one test's login/state from leaking into the next.

**Q21. How does the framework capture diagnostics on failure?**
In `tearDown`, when `result.getStatus() == FAILURE`:
```java
AllureAttachmentHelper.attachScreenshot(page, "📸 Failure Screenshot");
AllureAttachmentHelper.attachPageSource(page, "📄 Page Source at Failure");
AllureAttachmentHelper.attachText("💥 Exception", result.getThrowable().toString());
```
So every failure in the report carries a screenshot, the full HTML at failure time, and the stack —
enough to triage without re-running.

**Q22. What's the purpose of the page event listeners attached in `createContext`?**
They surface browser-side problems in the test logs: `onPageError` logs uncaught JS errors,
`onConsoleMessage` mirrors console output by severity, and `onRequest`/`onResponse` trace network
(warning on HTTP ≥ 400). This turns the test log into a near-complete picture of what the page did.

---

## 6. Playwright Mechanics in This Framework

**Q23. What is a `BrowserContext` and how does it differ from a `Page`?**
A `BrowserContext` is an isolated session (cookies, storage, permissions) — like an incognito
profile. A `Page` is a single tab within a context. Multiple pages in one context share cookies;
different contexts share nothing. This suite uses one context → one page per test.

**Q24. How does Playwright's auto-waiting help here?**
Locator actions (`click`, `innerText`, `getAttribute`) automatically wait for the element to be
attached, visible, and stable (actionable) up to the timeout. That's why the codebase rarely needs
explicit sleeps — and where it does wait, it uses semantic waits like
`page.waitForURL("**/login/**")` or `locator.waitFor(state=VISIBLE)`.

**Q25. How does the framework set browser context options, and why these specific ones?**
```java
browser.newContext(new Browser.NewContextOptions()
    .setViewportSize(1440, 900)
    .setColorScheme(ColorScheme.DARK)
    .setIgnoreHTTPSErrors(true)
    .setLocale("en-IN")
    .setTimezoneId("Asia/Kolkata"));
```
- `en-IN` + `Asia/Kolkata` → the app is an Indian-market dashboard; locale/timezone affect the live
  IST clock, date logic, and number formatting being tested.
- `DARK` → the app's default theme; matches what users see.
- `ignoreHTTPSErrors` → tolerate cert issues on staging/local URLs.

**Q26. How are the three browsers selected at runtime?**
```java
browser = switch (TestConfig.BROWSER.toLowerCase()) {
    case "firefox" -> playwright.firefox().launch(opts);
    case "webkit"  -> playwright.webkit().launch(opts);
    default        -> playwright.chromium().launch(opts);
};
```
Driven by `-Dbrowser=...`. CI runs a 3-way matrix for cross-browser parity.

---

## 7. Authentication & Protected Pages

**Q27. Explain the authentication flow. Why log in via API instead of the UI?**
`loginAsTestViewer()` (in `@BeforeSuite`) uses Playwright's `APIRequestContext` to `POST` to
`/api/auth/login/` with the test-viewer credentials, reads the `set-cookie` header, extracts the
`emt_session` token, and builds a `Cookie` object. UI login per test would be slow and brittle;
logging in once via API and **injecting the cookie into each context** is fast and reliable.

**Q28. How is the session cookie reused across tests?**
The cookie is a `static` field set once at suite start. In `createContext`, if
`useAuthenticatedSession()` is true and the cookie exists, it's added via `context.addCookies(...)`.
Each fresh context starts already authenticated.

**Q29. How does a test opt out of being authenticated, and why would it?**
```java
protected boolean useAuthenticatedSession() { return true; }   // override → false
```
`LoginPageTest` overrides this to `false` because a valid session changes login-page behavior (it
may auto-redirect away or render differently) — to test the logged-out experience, the cookie must
not be injected.

**Q30. What do `dropAuthenticatedSession()` / `restoreAuthenticatedSession()` solve?**
A single test that loops over several URLs may need to visit the login page mid-test as a
logged-out user, then continue authenticated. `dropAuthenticatedSession()` clears cookies for that
stretch; `restoreAuthenticatedSession()` re-adds the session cookie afterward.

**Q31. What is the "page-access unlock window" and how does the framework handle it?**
Pages like indices/commodities/expert-view are gated client-side: the static HTML loads, then
`app.js`'s `guardProtectedPageAccess()` async-probes access and, if locked, redirects to `/login/`.
The framework handles this via `skipIfRedirectedToLogin(featureName)` — it gives the redirect a
~2s window to settle (`page.waitForURL("**/login/**", timeout=2000)`), and if the page ends up on
login it throws `SkipException` (a *skip*, not a *failure*) because that's expected behavior when
the window is closed.

**Q32. Why does the API login fail *gracefully* (return null) rather than throw?**
If no credentials are configured or login fails, `loginAsTestViewer()` logs a warning and returns
`null`. Protected-page tests then fall back to skipping (via the gate logic) instead of erroring out
the whole suite. The suite still runs all the public-page tests.

---

## 8. Reliability — Retries, Waits, Soft-Fail & Skips

**Q33. Why does `navigateTo` retry navigation, and how?**
`navigateWithRetry` retries `page.navigate(url)` up to 3 times with 400ms pauses. A prior in-flight
navigation (e.g. the previous test's redirect still resolving) can abort the new one with
`net::ERR_ABORTED`; a brief retry absorbs that race instead of failing.

**Q34. Why is `page.title()` wrapped in `titleWithRetry`?**
A redirect still settling (bare domain → www, or the auth guard bouncing to `/login/`) can destroy
the JS execution context, making `page.title()` throw "Execution context was destroyed."
`titleWithRetry` does `waitForLoadState()` + `title()` with up to 3 attempts.

**Q35. What's the difference between a skipped and a failed test here, and why does it matter?**
- **Skip** (`SkipException`): expected, non-defect conditions — login gate closed, API returns 401
  because the unlock window is closed, `market.json` not publicly served.
- **Fail** (assertion): a genuine product/logic defect.

Keeping these separate keeps the pass/fail signal trustworthy — infra/gating noise doesn't masquerade
as a bug.

**Q36. Explain `waitUntil(BooleanSupplier, timeoutMs)`. Why not rely on Playwright auto-wait?**
```java
protected void waitUntil(BooleanSupplier condition, int timeoutMs) {
    long deadline = System.currentTimeMillis() + timeoutMs;
    while (System.currentTimeMillis() < deadline) {
        if (condition.getAsBoolean()) return;
        page.waitForTimeout(500);
    }
}
```
Some async UI updates (Expert View dashboard/AI-systems sections unhiding *after* the first analysis
card renders) aren't tied to a single locator's visibility — they're a composite app-state. A custom
polling predicate handles "wait until this arbitrary condition is true," which auto-wait can't
express.

**Q37. How is "soft-fail" implemented for live-API-dependent tests?**
Tests that hit `/data/market.json` or live scans check the HTTP status; on a non-200 (or a 401),
they `log.warn(...)` and `return` early (or assert the status is within an accepted set including
`401`). Combined with CI `continue-on-error` on those steps, a third-party outage doesn't sink the
run.

---

## 9. API-Level Testing

**Q38. How does the framework test APIs, and why in the same suite as UI tests?**
Via Playwright's `APIRequestContext`:
```java
APIRequestContext api = playwright.request().newContext();
APIResponse response = api.get(TestConfig.BASE_URL + "/api/live-market",
        RequestOptions.create().setTimeout(TestConfig.SLOW_RESOURCE_TIMEOUT_MS));
```
The signal *logic* is best validated against raw JSON (exact numbers, all states), while the UI
tests validate *rendering*. Doing both in one suite means a single run proves the engine is correct
*and* correctly displayed. `api.dispose()` is always called to release the context.

**Q39. How is JSON parsed and validated?**
Jackson `ObjectMapper.readTree(body)` → `JsonNode`. Field presence is checked with `node.has(...)`,
and values read defensively with `path(...).asText("")` / `.asDouble(-1)` so a missing field yields
a sentinel rather than an NPE.

**Q40. Why accept HTTP 200, 304, *and* 401 in `testLiveMarketApiReturnsValidStructure`?**
`200` = fresh data, `304` = valid cached response, `401` = the API is behind the same unlock window
as protected pages (closed window, not a bug). Asserting `isIn(200, 304, 401)` distinguishes "broken
API" from "gated API."

---

## 10. Signal Logic — Business Validation

> These are the most important tests: they prove the trading-signal engine is *mathematically*
> correct, not just that pages render.

**Q41. What core financial rules does the suite validate?**

| Rule | Check |
|---|---|
| Support < Resistance | `supportBelowResistance(s, r)` for all 4 indices |
| Risk:Reward ≥ 2.2 | `calcRiskRewardRatio = |target−entry| / |entry−stop| ≥ 2.2` |
| Buy geometry (CE) | `stop < entry < target` |
| Sell geometry (PE) | `stop > entry > target` |
| Confidence ∈ [0,100] | `isConfidenceInRange` on UI and JSON |
| Valid signal state | state ∈ {Trade Signal, Watchlist Only, No Trade, Hold, Pending} |
| Session guard | if "2 completed trades" note → state ≠ Trade Signal |
| Forward record guard | if "net negative" → state ≠ Trade Signal |

**Q42. Walk through `testBuySignalGeometryFromJson`.**
It fetches `market.json`, iterates the four index keys, and only acts on entries whose
`signal.state == "Trade Signal"`. It parses entry/stop/target, then **branches on direction**: if
`target > entry` it's a buy and must satisfy `isValidBuySetup` + R:R ≥ 2.2; if `target < entry` it's
a sell and must satisfy `isValidSellSetup` + R:R ≥ 2.2. Each branch attaches a geometry report to
Allure. Entries with unpopulated/zero values are skipped (live data may be empty off-hours).

**Q43. How does `SignalParser.parsePrice` handle messy UI values like "₹23,622.90"?**
```java
String cleaned = text.replaceAll("[,₹\\s]", "");   // strip commas, ₹, whitespace
return Double.parseDouble(cleaned);
```
Returns `-1` for `null`, blank, or the placeholder `"--"`, so callers can treat `< 0` as
"not populated yet" and skip rather than fail on missing live data.

**Q44. How is the session guard tested without server internals?**
`testSessionGuardCompletedTradesLimit` reads the signal `note`/`summary` text. If it contains
"2 completed trades", it asserts the state is **not** "Trade Signal" — i.e. the daily 2-trade cap
must have blocked a fresh signal. It's a black-box check of an internal rule via its observable note.

**Q45. Why guard numeric checks with `if (sup > 0 && res > 0)` before asserting?**
Live dashboards may not have levels populated off-market-hours. Asserting on `-1` sentinels would
produce false failures. The pattern is "skip the numeric assertion when data isn't there, fail only
when present-and-wrong" — distinguishing absent data from invalid data.

---

## 11. Data-Driven Testing

**Q46. How is data-driven testing done, with an example?**
TestNG `@DataProvider` returns an `Object[][]`; the test runs once per row.
```java
@DataProvider(name = "indexData")
public Object[][] indexData() { return new Object[][]{ {"NIFTY", NIFTY_URL}, {"BANKNIFTY", BANKNIFTY_URL}, ... }; }

@Test(dataProvider = "indexData")
public void testIndexSupportBelowResistance(String name, String url) { ... }
```
Used for `testPublicPageLoads`, the index/commodity page tests, and
`testSearchExamplesUpdateStatus` (`@DataProvider(name="stockSymbols")` → Reliance, TCS, INFY, HDFC
Bank, SBIN). One method, many cases, each reported separately in Allure.

**Q47. What's the benefit over a `for` loop inside one test?**
Each data row is an independent reported result — one failing index doesn't hide the others, and the
report shows exactly which input failed. A loop would fail-fast on the first bad case and report a
single test.

---

## 12. Reporting — Allure

**Q48. How is the Allure hierarchy structured?**
`@Epic("EaseMyTrade")` → `@Feature("Signal Logic")` → `@Story("Trade Signal Geometry")` →
the `@Test` method → `@Step` breadcrumbs → attachments. This shows up in Allure's **Behaviors** tab
as a navigable tree.

**Q49. What does `AllureAttachmentHelper` provide?**
Typed attachment wrappers: `attachScreenshot` (image/png), `attachPageSource` (text/html),
`attachText` (text/plain for computed values/analysis), `attachJson` (application/json for API
bodies), and `addPageInfo(url,title)` on every navigation. Centralizing them keeps test code clean
and attachment types consistent.

**Q50. What is `categories.json` and what problem does it solve?**
It maps failure message/trace patterns to buckets (Product Defect, Signal Logic, Network/API,
Expert View, Navigation, Infrastructure, Skipped). In the Allure **Categories** tab you instantly
see "is this 5 product bugs or 5 network blips?" — triage by class of failure, not one-by-one.

**Q51. How does the trend graph work across runs?**
CI restores `allure-history/` from the `gh-pages` branch before generating the report and saves new
history after. With ≥2 runs on `main`, Allure renders pass/fail trend over time. (First run shows no
trend — there's no history yet.)

**Q52. How are run-specific details surfaced in the report?**
CI writes an `environment.properties` (Base URL, browser, Java version, CI run number/actor/branch/
commit, tool versions) into `allure-results` before report generation, populating Allure's
Environment panel.

---

## 13. Logging

**Q53. Describe the logging setup.**
SLF4J facade + Logback impl, two appenders: colored console and a rolling file
(`target/test-logs/test.log`, daily rotation, 14-day retention). Levels by namespace:
`com.easemytrade=DEBUG` (full traceability), `io.qameta.allure`/`org.testng=INFO`,
`com.microsoft.playwright=WARN` (suppress request noise), root `WARN`.

**Q54. What are the log prefix conventions and why use them?**
`[ACTION]`, `[STATE]`, `[WAIT]`, `[CHECK]`, `[READ]`, `[FLOW]`, `[PAGE]`, plus `NAV →`/`NAV ✓` and
`📸`. They make logs greppable and let you reconstruct a test's exact behavior from the file alone —
"what did it click, what did it read, where did it wait."

---

## 14. CI/CD — GitHub Actions

**Q55. What triggers the pipeline?**
`push` to `main`/`develop`, `pull_request` to `main`, a `schedule` cron `30 0 * * 1-5` (weekdays
06:00 IST), and `workflow_dispatch` (manual, with browser / base_url / run_cross_browser inputs).

**Q56. Walk through the `test` job steps.**
Checkout → set up JDK 17 → restore Maven cache → restore Allure history from `gh-pages` → install
Playwright browsers + system deps → write `environment.properties` & copy `categories.json` into
`allure-results` → `mvn test` → `mvn allure:report` → save new history → publish report to GitHub
Pages under `reports/{run-number}/` → upload artifacts (results, report, logs) → write a rich job
summary.

**Q57. When does the cross-browser matrix run?**
The `cross-browser` job (3× parallel: Chromium/Firefox/WebKit) runs when the `test` job passes and
`run_cross_browser=true`, **or** on any push to `main`. Each browser uploads its own results.

**Q58. Why install Playwright browsers in CI explicitly?**
The Playwright **Java dependency** doesn't bundle the browser binaries; `playwright install
--with-deps` downloads the engines and OS-level libs the runner needs. Without it, launch fails on a
clean runner.

---

## 15. Scenario / "What Would You Do" Questions

**Q59. A test is flaky — passes locally, fails ~20% in CI. How do you debug it with this framework?**
Open the Allure result: check the **failure screenshot** and **page source** (auto-attached), the
**console/network logs** from the page listeners, and the timing. Likely culprits given the design:
a race the retry/`waitUntil` helpers don't cover, the unlock-window redirect (should be a skip — is
`skipIfRedirectedToLogin` called?), or a live-data field not yet populated (should be guarded by
`> 0`). Reproduce with `-Dheadless=false` and `slowMo` to watch it.

**Q60. How would you add a test for a brand-new "Refresh Data" button?**
1. Add a `@Step` action + locator in the relevant page object.
2. Write a `@Test` with `@Story`/`@Severity`, calling the action, reading state, asserting with
   AssertJ, attaching results, and a screenshot.
3. If it's a new class, register it in `testng.xml`.
(Exactly the pattern in `FRAMEWORK_GUIDE.md` §16.)

**Q61. The signal engine changes the minimum R:R from 2.2 to 2.5. What's the change?**
One line: `MIN_RISK_REWARD_RATIO = 2.5` in `TestConfig`. Every R:R test references the constant, so
nothing else changes — this is the payoff of centralizing thresholds.

**Q62. How would you run only the signal-logic tests against a local build, non-headless, on Firefox?**
```bash
mvn test -Dtest=SignalLogicTest -Dapp.base.url=http://localhost:4173 -Dheadless=false -Dbrowser=firefox
```

**Q63. A reviewer says "your tests skip too much — they're hiding bugs." How do you respond?**
Skips are reserved for *expected, non-defect* states (login gate closed, API gated by unlock window,
static JSON not served) — provably distinct from assertion failures which mean defects. The Allure
Categories tab keeps "Skipped" visible as its own bucket, so skips are auditable, not hidden. If a
skip ever masks a real condition, the fix is to tighten the skip predicate, not to fail on infra.

**Q64. How would you make the suite resilient if `easemytrade.in` is fully down?**
Public-page tests would fail (correctly — the app is down). To distinguish "app down" from "test
bug," the navigation retry + failure attachments already capture the connection error. A pre-flight
health-check test (or CI step) hitting the home URL first could short-circuit the run with a clear
"target unreachable" signal rather than 75 confusing failures.

**Q65. Explain end-to-end what happens when you run `mvn test` on a fresh machine.**
Maven resolves dependencies → Surefire reads `testng.xml` → attaches the AspectJ agent → TestNG
runs suites in order → `@BeforeSuite` launches the browser + API-logs-in → for each test:
fresh context (authenticated if applicable) → navigate → act → assert → attach diagnostics →
close context → `@AfterSuite` closes the browser → Allure results land in `target/allure-results`,
viewable with `mvn allure:serve`.
