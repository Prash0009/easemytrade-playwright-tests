# EaseMyTrade — Playwright Automation Framework Guide

**Version:** 1.0.0 · **Updated:** June 2026  
**Repository:** [Prash0009/easemytrade-playwright-tests](https://github.com/Prash0009/easemytrade-playwright-tests)  
**Target Application:** [easemytrade.in](https://easemytrade.in)

---

## Table of Contents

1. [Purpose](#1-purpose)
2. [Technology Stack](#2-technology-stack)
3. [Project Directory Structure](#3-project-directory-structure)
4. [Architecture Overview](#4-architecture-overview)
5. [Configuration Reference](#5-configuration-reference)
6. [Page Object Model — All Pages](#6-page-object-model--all-pages)
7. [Test Suites — Complete Test Case Inventory](#7-test-suites--complete-test-case-inventory)
   - 7.1 [Navigation Tests](#71-navigationtest)
   - 7.2 [Home Page Tests](#72-homepagetest)
   - 7.3 [Login Page Tests](#73-loginpagetest)
   - 7.4 [Signal Logic Tests](#74-signallogictest)
   - 7.5 [Expert View Tests](#75-expertviewtest)
   - 7.6 [Index Page Tests](#76-indexpagetest)
   - 7.7 [Content Pages Tests](#77-contentpagestest)
8. [Utility Classes](#8-utility-classes)
9. [Logging Strategy](#9-logging-strategy)
10. [Allure Reporting](#10-allure-reporting)
11. [GitHub Actions CI/CD](#11-github-actions-cicd)
12. [Running Tests Locally](#12-running-tests-locally)
13. [Running from GitHub (No Local Setup)](#13-running-from-github-no-local-setup)
14. [Key Logical / Calculation Checks](#14-key-logical--calculation-checks)
15. [Test Hierarchy in Allure](#15-test-hierarchy-in-allure)
16. [Adding New Tests](#16-adding-new-tests)
17. [Troubleshooting](#17-troubleshooting)

---

## 1. Purpose

This framework provides end-to-end automated verification of the EaseMyTrade educational market dashboard. It covers:

- **Functional correctness** — every page loads, every navigation link works, every form behaves correctly.
- **Signal logic validation** — the most critical layer. Checks that the trade signal engine is producing mathematically and logically valid outputs (support < resistance, valid R:R ratio, correct signal states, session guard enforcement, forward record guard enforcement).
- **Expert View report quality** — verifies the AI analysis terminal generates complete, structured reports with valid confidence scores and recommendations.
- **Cross-browser parity** — the same tests run on Chromium, Firefox, and WebKit.
- **Regression safety** — runs automatically on every push and on a daily schedule so breakages are caught within hours.

---

## 2. Technology Stack

| Component | Technology | Version |
|---|---|---|
| Browser automation | [Microsoft Playwright for Java](https://playwright.dev/java/) | 1.44.0 |
| Test runner | [TestNG](https://testng.org/) | 7.9.0 |
| Reporting | [Allure Framework](https://docs.qameta.io/allure/) | 2.27.0 |
| Assertions | [AssertJ](https://assertj.github.io/doc/) | 3.25.3 |
| JSON parsing | [Jackson Databind](https://github.com/FasterXML/jackson) | 2.17.1 |
| Logging facade | [SLF4J](https://www.slf4j.org/) | 2.0.13 |
| Logging implementation | [Logback Classic](https://logback.qos.ch/) | 1.5.6 |
| AOP (Allure steps) | [AspectJ Weaver](https://www.eclipse.org/aspectj/) | 1.9.22 |
| Build tool | [Apache Maven](https://maven.apache.org/) | 3.x |
| Java version | OpenJDK (Temurin) | 17 |
| CI platform | GitHub Actions | — |

---

## 3. Project Directory Structure

```
easemytrade-playwright-tests/
│
├── pom.xml                                   # Maven build descriptor
├── README.md                                 # Quick-start reference
├── FRAMEWORK_GUIDE.md                        # This document
│
├── .github/
│   └── workflows/
│       └── playwright-tests.yml              # CI/CD pipeline definition
│
└── src/
    └── test/
        ├── java/
        │   └── com/easemytrade/
        │       │
        │       ├── base/
        │       │   └── BaseTest.java         # Suite setup/teardown, Playwright lifecycle
        │       │
        │       ├── config/
        │       │   └── TestConfig.java       # All URLs, timeouts, thresholds
        │       │
        │       ├── pages/                    # Page Object Model
        │       │   ├── BasePage.java         # Common locators + debug-logged helpers
        │       │   ├── HomePage.java         # Home dashboard
        │       │   ├── LoginPage.java        # Sign-in page
        │       │   ├── ExpertViewPage.java   # Expert View / Best Pick terminal
        │       │   ├── IndexPage.java        # NIFTY / BANKNIFTY / FINNIFTY / SENSEX
        │       │   ├── GenericContentPage.java # News, Methodology, Telegram, Contact, Disclaimer
        │       │   └── NewsPage.java         # News-specific locators
        │       │
        │       ├── tests/                    # Test classes (one per feature area)
        │       │   ├── NavigationTest.java
        │       │   ├── HomePageTest.java
        │       │   ├── LoginPageTest.java
        │       │   ├── SignalLogicTest.java
        │       │   ├── ExpertViewTest.java
        │       │   ├── IndexPageTest.java
        │       │   └── ContentPagesTest.java
        │       │
        │       └── utils/
        │           ├── AllureAttachmentHelper.java  # Screenshots, source, text/JSON attachments
        │           └── SignalParser.java             # Price parser, R:R calculator, geometry checks
        │
        └── resources/
            ├── testng.xml                    # TestNG suite definition and execution order
            ├── allure.properties             # Allure link patterns
            ├── categories.json               # Allure failure categorisation rules
            └── logback-test.xml              # DEBUG-level logging configuration
```

---

## 4. Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                        Test Classes                         │
│   NavigationTest · HomePageTest · LoginPageTest             │
│   SignalLogicTest · ExpertViewTest · IndexPageTest          │
│   ContentPagesTest                                          │
└────────────────────────┬────────────────────────────────────┘
                         │ extends
┌────────────────────────▼────────────────────────────────────┐
│                        BaseTest                             │
│  • Playwright + Browser lifecycle (@BeforeSuite/AfterSuite) │
│  • BrowserContext + Page per test (@BeforeMethod)           │
│  • Screenshot + source capture on failure (@AfterMethod)    │
│  • @Step("Navigate to {url}") helper                        │
└──────────┬──────────────────────────────┬───────────────────┘
           │ uses                          │ uses
┌──────────▼──────────┐        ┌──────────▼──────────────────┐
│    Page Objects      │        │       Utility Classes        │
│  BasePage            │        │  SignalParser                │
│  HomePage            │        │  • parsePrice()              │
│  LoginPage           │        │  • calcRiskRewardRatio()     │
│  ExpertViewPage      │        │  • isValidBuySetup()         │
│  IndexPage           │        │  • isValidSellSetup()        │
│  GenericContentPage  │        │  • supportBelowResistance()  │
│  NewsPage            │        │  • isValidSignalState()      │
└─────────────────────┘        │  AllureAttachmentHelper      │
                               │  • attachScreenshot()        │
┌─────────────────────┐        │  • attachPageSource()        │
│     TestConfig      │        │  • attachText/Json()         │
│  All URLs           │        └─────────────────────────────┘
│  All thresholds     │
│  Env properties     │
└─────────────────────┘
           │
           ▼
┌──────────────────────────────────────────────────────────────┐
│                   Allure Report                              │
│  Epic → Feature → Story hierarchy                            │
│  @Step breadcrumbs · screenshots · JSON dumps                │
│  Environment panel · Categories panel · Trend graph          │
└──────────────────────────────────────────────────────────────┘
```

### Key design decisions

| Decision | Rationale |
|---|---|
| One `Browser` instance per suite, one `BrowserContext` per test | Isolates cookies/storage between tests; avoids test pollution |
| `@Step` on every page action | Allure report shows exact step sequence without reading code |
| `AllureAttachmentHelper` on every failure | Failure is immediately self-diagnosable — no need to re-run locally |
| `SignalParser` utility class | All numeric parsing and financial-logic checks in one place; testable in isolation |
| Soft-fail for network-dependent Expert View scan tests | Those tests depend on a live third-party API; a network timeout should not block the suite |
| `categories.json` | Separates product defects, signal-logic failures, and infra errors in the Allure categories chart |

---

## 5. Configuration Reference

All configuration lives in `TestConfig.java` and is driven by Maven system properties.

### System properties

| Property | Default | Description |
|---|---|---|
| `app.base.url` | `https://easemytrade.in` | Root URL of the target deployment |
| `browser` | `chromium` | Browser engine: `chromium`, `firefox`, `webkit` |
| `headless` | `true` | Run browsers without a visible window |

### Key constants

| Constant | Value | Meaning |
|---|---|---|
| `DEFAULT_TIMEOUT_MS` | 30 000 | Playwright default element wait |
| `NAVIGATION_TIMEOUT_MS` | 60 000 | Page navigation wait |
| `SLOW_RESOURCE_TIMEOUT_MS` | 90 000 | API calls (Expert View live scan) |
| `MIN_RISK_REWARD_RATIO` | 2.2 | Minimum acceptable R:R per engine rules |
| `MIN_CONFIDENCE_SCORE` | 0 | Lower bound for confidence % |
| `MAX_CONFIDENCE_SCORE` | 100 | Upper bound for confidence % |
| `MIDDAY_MIN_CONFIDENCE` | 90 | Minimum confidence for 11:00–13:30 IST entries |
| `VALID_SIGNAL_STATES` | `["Trade Signal","Watchlist Only","No Trade","Hold","Pending"]` | Accepted signal engine outputs |

### Page URL constants

```
HOME_URL            https://easemytrade.in/
LOGIN_URL           https://easemytrade.in/login/
MARKET_OVERVIEW_URL https://easemytrade.in/market-overview/
NIFTY_URL           https://easemytrade.in/nifty/today/
BANKNIFTY_URL       https://easemytrade.in/indices/banknifty/
FINNIFTY_URL        https://easemytrade.in/indices/finnifty/
SENSEX_URL          https://easemytrade.in/indices/sensex/
GOLD_URL            https://easemytrade.in/commodities/gold/
SILVER_URL          https://easemytrade.in/commodities/silver/
CRUDE_OIL_URL       https://easemytrade.in/commodities/crude-oil/
NATURAL_GAS_URL     https://easemytrade.in/commodities/natural-gas/
NEWS_URL            https://easemytrade.in/news/
METHODOLOGY_URL     https://easemytrade.in/methodology/
EXPERT_VIEW_URL     https://easemytrade.in/expert-view/
TELEGRAM_URL        https://easemytrade.in/telegram/
CONTACT_URL         https://easemytrade.in/contact/
DISCLAIMER_URL      https://easemytrade.in/disclaimer/
```

---

## 6. Page Object Model — All Pages

### BasePage
Abstract base for all page objects. Provides:
- Common header/nav locators (brand logo, nav links, dropdown, live clock)
- Footer locators
- `@Step`-annotated action methods (`openIndianIndicesDropdown`, `clickBrandLogo`)
- Debug-logged helpers: `textOf(id)`, `attrOf(selector, attr)`, `isVisible(locator)`, `waitForVisible(locator)`

### HomePage
Covers: `/`
- Hero section (title, lead, CTA buttons)
- Market watchlist rows (NIFTY, BANKNIFTY, FINNIFTY, SENSEX, Gold)
- Trading zone cards with level/support/resistance/signal readers
- Completed trades spotlight grid
- Market drivers panel
- Session label, pulse chart canvas, live clock

### LoginPage
Covers: `/login/`
- Form elements (username input, password input, sign-in button, sign-out button)
- `@Step` actions: `fillUsername`, `fillPassword`, `clickSignIn`, `login`, `togglePasswordVisibility`
- State readers: `getStatusText`, `isStatusError`, `isStatusSuccess`, `isPasswordHidden`

### ExpertViewPage
Covers: `/expert-view/`
- Hero section, search form, Generate Report button, Best Pick button
- Universe dropdown with all option selectors
- `@Step` actions: `typeStock`, `submitSearch`, `searchForStock`, `openUniverseDropdown`, `selectUniverse`, `clickBestPick`, `clickNextBestPick`, `clickPrevBestPick`
- Wait conditions: `waitForReportToLoad` (up to 60 seconds)
- State readers: `getConfidenceMeter`, `getBias`, `getRecommendation`, `getShortScope`, `isReportLoaded`, `isDashboardVisible`, `areSystemsVisible`

### IndexPage
Covers: `/nifty/today/`, `/indices/banknifty/`, `/indices/finnifty/`, `/indices/sensex/`
- Multi-selector signal state reader (tries multiple DOM patterns)
- Support and resistance level readers
- Entry / stop-loss / target readers
- Completed trade cards count

### GenericContentPage
Covers: `/news/`, `/methodology/`, `/telegram/`, `/contact/`, `/disclaimer/`
- Main content visibility check
- Heading, link count, paragraph count
- Page-level text extraction

### NewsPage
Extends GenericContentPage with news-specific locators (news items, domestic/global sections).

---

## 7. Test Suites — Complete Test Case Inventory

The suite runs in the order defined in `testng.xml`:

```
NavigationTest → HomePageTest → LoginPageTest → SignalLogicTest
→ ExpertViewTest → IndexPageTest → ContentPagesTest
```

Total test methods: **~75 across 7 test classes**.

---

### 7.1 NavigationTest

**Allure:** Epic `EaseMyTrade` / Feature `Navigation & Page Load`  
**Purpose:** Verify every public URL loads cleanly, all nav links work, and page titles are consistent.

| # | Test Method | Description | Severity |
|---|---|---|---|
| 1 | `testPublicPageLoads` | All 8 public pages load without errors and show expected title keyword | CRITICAL |
| 2 | `testIndexPageLoads` | NIFTY, BANKNIFTY, FINNIFTY, SENSEX pages load and contain index name in title/body | CRITICAL |
| 3 | `testCommodityPageLoads` | Gold, Silver, Crude Oil, Natural Gas pages load with commodity name present | NORMAL |
| 4 | `testLogoNavigatesToHome` | Brand logo from Expert View page navigates back to home | NORMAL |
| 5 | `testExpertViewNavLink` | Expert View nav link navigates to `/expert-view/` | NORMAL |
| 6 | `testNewsNavLink` | News nav link navigates to `/news/` | NORMAL |
| 7 | `testFooterDisclaimerNavigation` | Footer disclaimer link navigates to `/disclaimer/` | NORMAL |
| 8 | `testHomeOpenMarketOverviewCta` | Home 'Open Market Overview' CTA stays on home page (anchor scroll) | NORMAL |
| 9 | `testMarketOverviewRedirect` | `/market-overview/` URL resolves without error | MINOR |
| 10 | `testAllNavLinksHaveHrefs` | Every `<a>` in the navigation has a non-empty `href` attribute | NORMAL |
| 11 | `testIndianIndicesDropdownLinks` | Indian Indices dropdown shows NIFTY, BANKNIFTY, FINNIFTY, SENSEX links | NORMAL |
| 12 | `testTitleFormatConsistency` | Home, Expert View, Login pages all contain "EaseMyTrade" in title | MINOR |

> **Data-driven:** `testPublicPageLoads`, `testIndexPageLoads`, `testCommodityPageLoads` use `@DataProvider` and each execute once per URL in the data set.

---

### 7.2 HomePageTest

**Allure:** Epic `EaseMyTrade` / Feature `Home Page`  
**Purpose:** Verify every visible section of the home dashboard, with numeric validation on the zone levels.

| # | Test Method | Description | Severity |
|---|---|---|---|
| 1 | `testHomePageTitle` | Page title contains "EaseMyTrade" | BLOCKER |
| 2 | `testHomePageUrl` | Page URL starts with the configured base URL | CRITICAL |
| 3 | `testHeaderNavigation` | All header nav links are visible (Expert View, News, Methodology, Telegram, Contact) | CRITICAL |
| 4 | `testHeroSection` | Hero band is visible; `<h1>` contains "EaseMyTrade" | CRITICAL |
| 5 | `testHeroCallToActionButtons` | "Open Market Overview" and "Indian Indices" CTAs are visible | NORMAL |
| 6 | `testMarketWatchlistInstruments` | All 5 watchlist rows (NIFTY, BANKNIFTY, FINNIFTY, SENSEX, Gold) are visible | CRITICAL |
| 7 | `testWatchlistNavigation` | Each watchlist row links to the correct index/commodity URL | CRITICAL |
| 8 | `testTradingZonesSectionVisible` | Trading zones section is present with exactly 4 zone cards | CRITICAL |
| 9 | `testNiftyZoneSupportBelowResistance` | **Logical:** NIFTY support (numeric) < resistance (numeric) | CRITICAL |
| 10 | `testBankNiftyZoneSupportBelowResistance` | **Logical:** BANKNIFTY support < resistance | CRITICAL |
| 11 | `testFinniftyZoneSupportBelowResistance` | **Logical:** FINNIFTY support < resistance | CRITICAL |
| 12 | `testSensexZoneSupportBelowResistance` | **Logical:** SENSEX support < resistance | CRITICAL |
| 13 | `testNiftyZoneSignalState` | NIFTY zone signal is one of the valid engine states | CRITICAL |
| 14 | `testCompletedTradesSection` | Previous completed trades spotlight section and grid are visible | NORMAL |
| 15 | `testMarketDriversPanel` | Market drivers panel is visible | NORMAL |
| 16 | `testFooterPresence` | Footer and disclaimer link are visible | NORMAL |
| 17 | `testFooterDisclaimerLinkHref` | Footer disclaimer link `href` is exactly `/disclaimer/` | NORMAL |
| 18 | `testMarketTickerPresence` | Live market ticker strip is present | NORMAL |
| 19 | `testLiveClockPresence` | IST live clock element is present in navigation | MINOR |
| 20 | `testPulseChartPresence` | Hero pulse chart canvas element is rendered | NORMAL |
| 21 | `testIndianIndicesDropdown` | Dropdown reveals NIFTY, BANKNIFTY, FINNIFTY, SENSEX links | NORMAL |

---

### 7.3 LoginPageTest

**Allure:** Epic `EaseMyTrade` / Feature `Login Page`  
**Purpose:** Verify the sign-in form structure, validation behaviour, and brute-force protection.

| # | Test Method | Description | Severity |
|---|---|---|---|
| 1 | `testLoginPageTitle` | Page title contains "Sign In" and "EaseMyTrade" | BLOCKER |
| 2 | `testLoginFormPresence` | Form, username input, password input, sign-in button, sign-out button all visible | BLOCKER |
| 3 | `testPasswordIsHiddenByDefault` | Password field type is `password` by default | CRITICAL |
| 4 | `testPasswordToggleVisibility` | Toggle changes type to `text` then back to `password` | NORMAL |
| 5 | `testEmptyFormSubmissionShowsError` | Submitting empty form shows an error status message | CRITICAL |
| 6 | `testMissingPasswordShowsError` | Submitting with only username shows an error | NORMAL |
| 7 | `testInvalidCredentialsShowError` | Invalid credentials produce an error status | CRITICAL |
| 8 | `testLoginCooldownAfterMaxAttempts` | After 4 failed attempts, a cooldown or error message appears | CRITICAL |
| 9 | `testBrandLogoLinksToHome` | Brand logo `href` is `/` | MINOR |
| 10 | `testHeroHeadingPresent` | Hero heading (secure access description) is non-blank | MINOR |
| 11 | `testLoginBodyAttribute` | `<body data-page="login">` attribute is set | MINOR |
| 12 | `testLoginNextQueryParam` | Login page preserves `?next=/expert-view/` query parameter | NORMAL |

---

### 7.4 SignalLogicTest

**Allure:** Epic `EaseMyTrade` / Feature `Signal Logic`  
**Purpose:** The most critical suite. Validates the mathematical and logical correctness of the signal engine output. Uses both browser page state and direct JSON API calls.

| # | Test Method | Description | Severity |
|---|---|---|---|
| 1 | `testNiftySignalStateIsValid` | NIFTY zone signal state is a valid engine enum value | BLOCKER |
| 2 | `testAllIndexZonesHaveSignalData` | All 4 index zone cards exist on the home page | CRITICAL |
| 3 | `testAllZonesSupportBelowResistance` | **Logical:** Support < resistance for all 4 indices, with structured report attachment | CRITICAL |
| 4 | `testConfidenceScoreRange` | Confidence score shown is in [0, 100] | CRITICAL |
| 5 | `testLiveMarketApiReturnsValidStructure` | Live market API (`/api/live-market`) returns HTTP 200/304 with valid JSON | CRITICAL |
| 6 | `testMarketDataHasRequiredSignalFields` | `market.json` has all required NIFTY signal fields: `level`, `signal.state`, `signal.confidence`, `signal.stopLoss`, `signal.targetPrice` | CRITICAL |
| 7 | `testBuySignalGeometryFromJson` | **Logical:** For any Trade Signal, stop < entry < target (buy) or target < entry < stop (sell), verified from raw JSON | CRITICAL |
| 8 | `testMinimumRiskRewardRatio` | **Logical:** R:R ratio `(target − entry) / (entry − stop) ≥ 2.2` for all active Trade Signal entries | CRITICAL |
| 9 | `testNoTradeStateHasNoActionableSignal` | No Trade state must not show "Buy Signal" or "Sell Signal" as the action | NORMAL |
| 10 | `testSessionGuardCompletedTradesLimit` | If note says "2 completed trades already used", state must not be Trade Signal | CRITICAL |
| 11 | `testForwardRecordGuardBlocksFreshSignals` | If forward record is net negative, no index should be in Trade Signal state | CRITICAL |

---

### 7.5 ExpertViewTest

**Allure:** Epic `EaseMyTrade` / Feature `Expert View`  
**Purpose:** Full validation of the AI analysis terminal — structure, form interaction, scan results, confidence, recommendation, and the Best Pick navigator.

| # | Test Method | Description | Severity |
|---|---|---|---|
| 1 | `testExpertViewPageTitle` | Title contains "Expert View" and "EaseMyTrade" | BLOCKER |
| 2 | `testHeroSectionContent` | Hero heading contains "AI" | CRITICAL |
| 3 | `testSearchFormElements` | Search form, input, Generate Report button, Best Pick button all visible | BLOCKER |
| 4 | `testUniverseDropdownPresence` | Universe dropdown is visible and shows a non-blank selection | CRITICAL |
| 5 | `testUniverseDropdownOptions` | Dropdown opens and contains more than 5 universe options | NORMAL |
| 6 | `testUniverseSelectionUpdatesLabel` | Selecting "nifty50" updates the dropdown summary text to contain "nifty" | NORMAL |
| 7 | `testBestPickNavButtonsPresent` | Prev and Next Best Pick buttons are visible | NORMAL |
| 8 | `testExampleChipsPresent` | All 5 quick-chip examples (Reliance, TCS, HDFC Bank, INFY, SBIN) are visible | NORMAL |
| 9 | `testChipFillsSearchInput` | Clicking the "TCS" chip fills the search input with "TCS" | NORMAL |
| 10 | `testInitialStatusMessage` | Status message is visible and non-blank before any search | NORMAL |
| 11 | `testInitialHeroMetricsState` | Hero metrics (confidence, trend) are present in initial state | MINOR |
| 12 | `testInitialReportAreaEmpty` | Report area shows the "Waiting for an asset" empty card before search | NORMAL |
| 13 | `testReportGenerationForReliance` | Searching "Reliance" generates at least one analysis card | BLOCKER |
| 14 | `testDashboardMetricsAfterScan` | After scanning "TCS": Confidence Meter, Bias, Recommendation are visible and non-blank | CRITICAL |
| 15 | `testExpertViewConfidenceScoreRange` | **Logical:** Confidence score from Expert View scan is in [0, 100] | CRITICAL |
| 16 | `testAiRecommendationIsValid` | Recommendation is one of: Strong Buy, Buy, Accumulate, Hold, Reduce, Sell, Strong Sell, Watch, Neutral | CRITICAL |
| 17 | `testFiveAiRobotCardsVisible` | AI Systems section shows exactly 5 robot cards after scan | NORMAL |
| 18 | `testReportSections` | Report HTML has substantive content (> 500 chars) | CRITICAL |
| 19 | `testBestPickButtonInitiatesScan` | Clicking Best Pick changes the status message | CRITICAL |
| 20 | `testEmptySearchDoesNotCrash` | Submitting empty search keeps the user on the Expert View page | NORMAL |
| 21 | `testShortHorizonScopeAfterScan` | Short Horizon Scope field is populated after a scan | NORMAL |
| 22 | `testPageBodyAttribute` | `<body data-page="expert-view">` | MINOR |
| 23 | `testSearchExamplesUpdateStatus` | Searching each of the 5 example stocks updates the status without a hard error | CRITICAL |

> **Data-driven:** `testSearchExamplesUpdateStatus` uses `@DataProvider(name = "stockSymbols")` and runs once per stock: Reliance, TCS, INFY, HDFC Bank, SBIN.

---

### 7.6 IndexPageTest

**Allure:** Epic `EaseMyTrade` / Feature `Index Pages`  
**Purpose:** Validate each index page (NIFTY, BANKNIFTY, FINNIFTY, SENSEX) plus all commodity pages, with numeric checks on zone levels and trade geometry.

| # | Test Method | Description | Severity |
|---|---|---|---|
| 1 | `testIndexPageLoadsWithContent` | Each of the 4 index pages loads with the index name in page body | BLOCKER |
| 2 | `testIndexPageTitle` | Each index page title contains "EaseMyTrade" | CRITICAL |
| 3 | `testIndexPageNavigation` | Each index page has brand logo and nav bar visible | CRITICAL |
| 4 | `testNiftySignalState` | NIFTY page shows a valid signal state string | CRITICAL |
| 5 | `testBankNiftySignalState` | BANKNIFTY page shows a valid signal state string | CRITICAL |
| 6 | `testIndexSupportBelowResistance` | **Logical:** For each of the 4 indices, support (numeric) < resistance (numeric) | CRITICAL |
| 7 | `testNiftyTradeGeometry` | **Logical:** When NIFTY is in Trade Signal state, buy geometry (stop < entry < target) and R:R ≥ 2.2 are valid | CRITICAL |
| 8 | `testNiftyCompletedTradeCards` | NIFTY page has main signal content visible | NORMAL |
| 9 | `testCommodityPageContent` | Each of 4 commodity pages contains the commodity name in body text | NORMAL |
| 10 | `testCommodityPageNavigation` | Each commodity page has brand logo and nav bar visible | NORMAL |
| 11 | `testGoldPriceIsPositive` | **Logical:** Gold page price value is a positive number | NORMAL |

> **Data-driven:** `testIndexPageLoadsWithContent`, `testIndexPageTitle`, `testIndexPageNavigation`, `testIndexSupportBelowResistance` use `@DataProvider(name = "indexData")`. `testCommodityPageContent`, `testCommodityPageNavigation` use `@DataProvider(name = "commodityData")`.

---

### 7.7 ContentPagesTest

**Allure:** Epic `EaseMyTrade` / Feature `Content Pages`  
**Purpose:** Verify all informational content pages load with appropriate content, brand consistency, and footer presence.

| # | Test Method | Description | Severity |
|---|---|---|---|
| 1 | `testNewsPageLoads` | News page loads with main content visible | CRITICAL |
| 2 | `testNewsPageHasHeadings` | News page contains at least one heading | NORMAL |
| 3 | `testMethodologyPageLoads` | Methodology page loads with visible main content | CRITICAL |
| 4 | `testMethodologyPageContent` | Methodology body references "signal", "nifty", or "indicator" | NORMAL |
| 5 | `testTelegramPageLoads` | Telegram page loads and body contains "telegram" | CRITICAL |
| 6 | `testContactPageLoads` | Contact page loads and contains "contact", "email", or "prashant" | CRITICAL |
| 7 | `testContactPageHasParagraphs` | Contact page has at least one paragraph | NORMAL |
| 8 | `testDisclaimerPageLoads` | Disclaimer page loads with "disclaimer" or "educational" in body | CRITICAL |
| 9 | `testDisclaimerEducationalStatement` | Disclaimer explicitly states educational/informational purpose | CRITICAL |
| 10 | `testTelegramStatusPageLoads` | `/telegram-status/` loads without 404 | MINOR |
| 11 | `testAllPagesHaveEaseMyTradeBranding` | 10 major pages all have "EaseMyTrade" in their title | NORMAL |
| 12 | `testAllPagesHaveFooter` | 5 major pages all have a visible footer | MINOR |

---

## 8. Utility Classes

### `SignalParser`

The financial-logic brain of the framework. Contains only static, stateless methods.

| Method | Description |
|---|---|
| `parsePercentage(text)` | Extracts `62.5` from `"62.5%"` or `"62"` |
| `parsePrice(text)` | Parses `"23,622.90"` → `23622.9`, handles `₹`, commas, spaces |
| `isValidSignalState(state)` | Returns `true` for any of the 5 valid engine states |
| `isConfidenceInRange(confidence)` | Returns `true` if `0 ≤ confidence ≤ 100` |
| `isValidBuySetup(entry, stop, target)` | Returns `true` if `stop < entry < target` |
| `isValidSellSetup(entry, stop, target)` | Returns `true` if `stop > entry > target` |
| `calcRiskRewardRatio(entry, stop, target)` | Returns `|target − entry| / |entry − stop|` |
| `supportBelowResistance(support, resistance)` | Returns `true` if `support < resistance` |

### `AllureAttachmentHelper`

Wraps all Allure attachment calls so test code stays clean.

| Method | Attachment Type | When Used |
|---|---|---|
| `attachScreenshot(page, name)` | `image/png` | On failure (automatic) + key steps (manual) |
| `attachPageSource(page, name)` | `text/html` | On failure (automatic) |
| `addPageInfo(url, title)` | `text/plain` | Every `navigateTo()` call |
| `attachText(name, content)` | `text/plain` | Computed values, analysis results |
| `attachJson(name, json)` | `application/json` | Raw API response bodies |

---

## 9. Logging Strategy

Logging uses **SLF4J + Logback** with two appenders: coloured console output and a rolling file log.

### Log levels by namespace

| Logger | Level | Rationale |
|---|---|---|
| `com.easemytrade` | **DEBUG** | Every action, state read, and wait is logged for full traceability |
| `io.qameta.allure` | INFO | Allure lifecycle events visible in CI output |
| `org.testng` | INFO | Suite/test start-stop events |
| `com.microsoft.playwright` | WARN | Only unexpected Playwright errors, no noise from page requests |
| root | WARN | Third-party libraries stay quiet |

### Log format

**Console** (colour-coded):
```
HH:mm:ss.SSS LEVEL  [thread] logger.name  message
```

**File** (full timestamp, pipe-separated):
```
yyyy-MM-dd HH:mm:ss.SSS | LEVEL | logger.full.name | message
```

Logs are written to `target/test-logs/test.log`, archived daily, retained for 14 days.

### Log conventions inside tests

| Prefix | Meaning |
|---|---|
| `[ACTION]` | User interaction performed (click, fill, clear) |
| `[STATE]` | Value read from the UI |
| `[WAIT]` | A wait condition started or completed |
| `[CHECK]` | A boolean visibility/state check |
| `[READ]` | Raw element text extracted |
| `[FLOW]` | A composite action composed of smaller steps |
| `[PAGE]` | Page object initialised |
| `NAV →` | Navigation started |
| `NAV ✓` | Navigation completed with timing |
| `📸` | Screenshot captured |

---

## 10. Allure Reporting

### Report panels

| Panel | What it shows |
|---|---|
| **Overview** | Pass/fail/skip counts, duration, environment info, categories breakdown |
| **Categories** | Failures grouped by type (Product Defect, Signal Logic, Network, Expert View, Navigation, Infrastructure) |
| **Suites** | Tests organised by Java class |
| **Graphs** | Severity breakdown, status distribution, duration chart |
| **Timeline** | Each test mapped to a time axis (useful for spotting slow tests) |
| **Behaviors** | Tests organised by Epic → Feature → Story hierarchy |
| **Packages** | Tests organised by Java package |
| **Trend** | Pass/fail rates across historical runs (requires history in `gh-pages`) |

### Hierarchy used

```
Epic: EaseMyTrade
└── Feature: Home Page / Login Page / Signal Logic / Expert View / etc.
    └── Story: Page Load / Signal State Validation / Trade Signal Geometry / etc.
        └── Test Case (individual @Test method)
            └── @Step breadcrumbs (every action and navigation)
                └── Attachments (screenshots, JSON, text)
```

### Failure categories (`categories.json`)

| Category | Trigger pattern |
|---|---|
| 🔴 Product Defects | Generic assertion failures |
| 🟠 Signal Logic Failures | Messages about support, resistance, R:R, confidence, signal state |
| 🟡 Network / API Failures | Timeouts, HTTP errors (404, 500) |
| 🔵 Expert View Failures | Messages about analysis cards, Best Pick, confidence meter |
| 🔶 Navigation Failures | href, URL, title, redirect messages |
| ⚫ Infrastructure Errors | PlaywrightException, NullPointer, ClassNotFound |
| ⚪ Skipped Tests | Any skipped test |

### Environment panel fields

The environment panel is populated by a file written by GitHub Actions before each run:

```properties
Application=EaseMyTrade
Base.URL=https://easemytrade.in
Browser=chromium
Java.Version=17
CI.Run.Number=42
CI.Run.Actor=Prash0009
CI.Branch=main
CI.Commit=a1b2c3d
Playwright.Version=1.44.0
TestNG.Version=7.9.0
Allure.Version=2.27.0
```

### How to open the report locally

```bash
# After running tests:
mvn allure:serve
# This starts a local HTTP server and opens the browser automatically.
```

---

## 11. GitHub Actions CI/CD

### Workflow file

`.github/workflows/playwright-tests.yml`

### Triggers

| Event | When it fires |
|---|---|
| `push` to `main` or `develop` | On every code push |
| `pull_request` to `main` | On every PR open/update |
| `schedule` cron `30 0 * * 1-5` | Weekdays at 6:00 AM IST |
| `workflow_dispatch` | Manual trigger from GitHub UI |

### Manual trigger parameters

| Input | Default | Description |
|---|---|---|
| `browser` | `chromium` | Browser to use: `chromium`, `firefox`, `webkit` |
| `base_url` | `https://easemytrade.in` | Override to test a staging or local deployment |
| `run_cross_browser` | `false` | Set `true` to also run the 3-browser matrix job |

### Jobs

#### `test` (always runs)
1. Checkout source
2. Set up JDK 17
3. Restore Maven cache
4. Restore Allure history from `gh-pages` branch (enables trend graph)
5. Install Playwright browsers and system dependencies
6. Write `environment.properties` and copy `categories.json` into `target/allure-results`
7. Run all tests (`mvn test`)
8. Generate Allure HTML report (`mvn allure:report`)
9. Save new history for next run
10. Publish report to GitHub Pages at `reports/{run-number}/`
11. Upload artifacts: `allure-results`, `allure-report`, `test-logs`
12. Write rich job summary to GitHub UI

#### `cross-browser` (conditional)
Runs a 3×parallel matrix (Chromium, Firefox, WebKit) when:
- The `test` job passes and `run_cross_browser=true` was selected, **or**
- A push to `main` happens

Each browser uploads its own `allure-results` artifact.

### Artifacts produced

| Artifact name | Contents | Retention |
|---|---|---|
| `allure-report-{browser}-run{N}` | Full HTML report — open `index.html` | 30 days |
| `allure-results-{browser}-run{N}` | Raw Allure JSON results | 30 days |
| `test-logs-run{N}` | `target/test-logs/test.log` | 14 days |

### GitHub Pages live report

After a run on `main`, the report is published to:
```
https://Prash0009.github.io/easemytrade-playwright-tests/reports/{run-number}/index.html
```

---

## 12. Running Tests Locally

### Prerequisites

- JDK 17 or later (`java -version`)
- Apache Maven 3.8+ (`mvn -version`)
- Internet access to `easemytrade.in`

### Install Playwright browsers (first time only)

```bash
cd easemytrade-playwright-tests
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI \
              -Dexec.args="install --with-deps chromium"
```

### Run all tests

```bash
mvn test
```

### Run with custom options

```bash
# Different browser
mvn test -Dbrowser=firefox

# Against a local dev server
mvn test -Dapp.base.url=http://localhost:4173

# Show browser window (not headless)
mvn test -Dheadless=false

# Combine options
mvn test -Dbrowser=webkit -Dapp.base.url=https://staging.easemytrade.in -Dheadless=false
```

### Run a single test class

```bash
mvn test -Dtest=SignalLogicTest
```

### View the Allure report

```bash
# After mvn test completes:
mvn allure:serve
# Opens http://localhost:PORT in your default browser automatically.

# Or generate static HTML:
mvn allure:report
open target/allure-report/index.html   # macOS
```

---

## 13. Running from GitHub (No Local Setup)

1. Go to the repository: **[Prash0009/easemytrade-playwright-tests](https://github.com/Prash0009/easemytrade-playwright-tests)**
2. Click the **Actions** tab.
3. Click **EaseMyTrade Playwright Tests** in the left sidebar.
4. Click **Run workflow** (top right).
5. Fill in the optional inputs (browser, base URL, cross-browser flag).
6. Click the green **Run workflow** button.
7. Wait for the job to complete (~8–15 minutes).
8. Click the finished run to see:
   - The **job summary** table with pass/fail counts.
   - The **Artifacts** section — download `allure-report-chromium-run{N}` and open `index.html`.
   - Or visit the **GitHub Pages URL** listed in the summary.

---

## 14. Key Logical / Calculation Checks

These are the tests that verify the signal engine is computing things correctly — not just that the page renders.

### A. Support must be below Resistance

**Files:** `HomePageTest`, `IndexPageTest`, `SignalLogicTest`  
**Logic:**
```
parsed_support < parsed_resistance
```
Both values are extracted from the page/JSON, stripped of commas and currency symbols, parsed as `double`, then compared numerically.

### B. Minimum Risk-Reward Ratio (2.2R)

**Files:** `SignalLogicTest.testMinimumRiskRewardRatio`, `IndexPageTest.testNiftyTradeGeometry`  
**Logic:**
```
R:R = |target − entry| / |entry − stop|
R:R ≥ 2.2  (engine requirement per EXPERT_VIEW_AND_SIGNAL_LOGIC.md)
```

### C. Buy Signal Geometry

**Files:** `SignalLogicTest.testBuySignalGeometryFromJson`, `IndexPageTest.testNiftyTradeGeometry`  
**Logic (CE / call signal):**
```
stop < entry       (stop loss is below entry)
target > entry     (target is above entry)
```

### D. Sell Signal Geometry

**Files:** `SignalLogicTest.testBuySignalGeometryFromJson`  
**Logic (PE / put signal):**
```
stop > entry       (stop loss is above entry)
target < entry     (target is below entry)
```

### E. Confidence Score Range

**Files:** `HomePageTest`, `ExpertViewTest`, `SignalLogicTest`  
**Logic:**
```
0 ≤ confidence ≤ 100
```

### F. Signal State Enum

**Files:** `HomePageTest`, `SignalLogicTest`, `IndexPageTest`  
**Logic:**
```
state ∈ {"Trade Signal", "Watchlist Only", "No Trade", "Hold", "Pending"}
```

### G. Session Guard (max 2 trades per day)

**Files:** `SignalLogicTest.testSessionGuardCompletedTradesLimit`  
**Logic:** If the signal note contains "2 completed trades already used today", the signal state must NOT be "Trade Signal".

### H. Forward Record Guard

**Files:** `SignalLogicTest.testForwardRecordGuardBlocksFreshSignals`  
**Logic:** If the signal summary contains "net negative" or "net -", the signal state must NOT be "Trade Signal".

### I. Expert View Confidence from Scan

**Files:** `ExpertViewTest.testExpertViewConfidenceScoreRange`  
**Logic:** After a live scan, the Confidence Meter value must parse as a number in [0, 100].

### J. AI Recommendation Enum

**Files:** `ExpertViewTest.testAiRecommendationIsValid`  
**Logic:**
```
recommendation ∈ {"Strong Buy", "Buy", "Accumulate", "Hold", "Reduce",
                   "Sell", "Strong Sell", "Watch", "Neutral",
                   "Standby", "Watchlist Only", "No Trade"}
```

---

## 15. Test Hierarchy in Allure

The following table shows how the Allure **Behaviors** tab is structured. Navigate: `Behaviors → Epic → Feature → Story`.

```
EaseMyTrade (Epic)
├── Navigation & Page Load (Feature)
│   ├── Page Load (Story)
│   ├── Index Pages (Story)
│   ├── Commodity Pages (Story)
│   ├── Navigation Links (Story)
│   ├── Logo Navigation (Story)
│   ├── Footer Navigation (Story)
│   ├── Home Page CTAs (Story)
│   ├── Redirects (Story)
│   └── Page Titles (Story)
│
├── Home Page (Feature)
│   ├── Page Load
│   ├── Navigation
│   ├── Hero Section
│   ├── Market Watchlist
│   ├── Trading Zones
│   ├── Signal Logic - Calculation   ← numeric support/resistance checks
│   ├── Signal Logic
│   ├── Completed Trades
│   ├── Market Drivers
│   ├── Footer
│   ├── Market Ticker
│   ├── Live Clock
│   └── Chart
│
├── Login Page (Feature)
│   ├── Page Load
│   ├── Form Elements
│   ├── Password Field
│   ├── Form Validation
│   ├── Authentication
│   ├── Brute Force Protection
│   ├── Navigation
│   └── Page Metadata
│
├── Signal Logic (Feature)               ← most critical feature
│   ├── Signal State Validation
│   ├── Support & Resistance Calculation
│   ├── Confidence Score Validation
│   ├── API Signal Validation
│   ├── Signal Data Integrity
│   ├── Trade Signal Geometry
│   ├── Session Guard Logic
│   └── Forward Record Guard
│
├── Expert View (Feature)
│   ├── Page Load
│   ├── Search Form
│   ├── Universe Selector
│   ├── Best Pick Navigation
│   ├── Best Pick
│   ├── Example Chips
│   ├── Initial State
│   ├── Report Generation
│   ├── Dashboard Metrics
│   ├── Confidence Score Validation
│   ├── Recommendation Validation
│   ├── AI Systems
│   ├── Report Sections
│   ├── Search Validation
│   └── Page Metadata
│
├── Index Pages (Feature)
│   ├── Page Load
│   ├── Navigation
│   ├── Signal Logic
│   ├── Support & Resistance Calculation
│   ├── Trade Signal Geometry
│   ├── Completed Trades
│   ├── Commodity Pages
│   ├── Commodity Pages - Navigation
│   └── Commodity Price Validation
│
└── Content Pages (Feature)
    ├── News Page
    ├── Methodology Page
    ├── Telegram Page
    ├── Contact Page
    ├── Disclaimer Page
    ├── Telegram Status Page
    ├── Brand Consistency
    └── Footer Consistency
```

---

## 16. Adding New Tests

### Step 1 — Add a page object method (if needed)

Open the relevant page object in `src/test/java/com/easemytrade/pages/`. Add the locator and a `@Step`-annotated action method:

```java
@Step("Click the refresh data button")
public void clickRefreshDataButton() {
    log.debug("  [ACTION] Clicking Refresh Data button");
    page.locator("#refreshDataButton").click();
}
```

### Step 2 — Write the test method

In the appropriate test class (or a new one), add your method with Allure annotations:

```java
@Test(description = "Clicking Refresh Data button shows a loading status")
@Story("Data Refresh")
@Severity(SeverityLevel.NORMAL)
public void testRefreshDataButtonTriggersStatus() {
    homePage.clickRefreshDataButton();
    String status = page.locator("#refreshStatus").innerText().trim();
    log.info("Refresh status: '{}'", status);
    AllureAttachmentHelper.attachText("Refresh Status", status);
    assertThat(status).isNotBlank();
    takeScreenshot("After Refresh Click");
}
```

### Step 3 — Register in testng.xml (if new class)

```xml
<test name="Data Refresh Tests" preserve-order="true">
    <classes>
        <class name="com.easemytrade.tests.DataRefreshTest"/>
    </classes>
</test>
```

### Naming conventions

| Item | Convention | Example |
|---|---|---|
| Test class | `{FeatureName}Test` | `SignalLogicTest` |
| Test method | `test{WhatIsBeingTested}` | `testNiftyZoneSupportBelowResistance` |
| DataProvider | camelCase, descriptive | `indexData`, `stockSymbols` |
| @Step | Imperative sentence, use `{param}` for parameters | `"Search for stock: {stock}"` |
| Log messages | Prefix with `[ACTION]`, `[STATE]`, `[WAIT]` | `"  [ACTION] typeStock('TCS')"` |

---

## 17. Troubleshooting

### Test times out waiting for Expert View report

**Cause:** The Expert View API calls Yahoo Finance and NSE — both are rate-limited and geographically variable.  
**Fix:** These tests already have soft-fail logic (`continue-on-error`). If consistently failing in CI, check whether the live APIs are accessible from GitHub Actions runner IPs.

### `AssertionError: expected: not null but was: null` on `textOf(id)`

**Cause:** The element ID does not yet exist in the DOM when the reader fires.  
**Fix:** Add a `waitForVisible` call before reading, or increase `DEFAULT_TIMEOUT_MS` in `TestConfig`.

### `PlaywrightException: Page.navigate: net::ERR_CONNECTION_REFUSED`

**Cause:** The application is not running at the configured `app.base.url`.  
**Fix:** Verify the URL in `TestConfig.BASE_URL` or pass `-Dapp.base.url=` explicitly.

### Allure report shows no Trend graph

**Cause:** The `gh-pages` branch does not yet have a `allure-history/` folder (first run).  
**Fix:** Run the workflow at least twice on `main`. The second run will restore the history from the first.

### Cross-browser job not running

**Cause:** By default, cross-browser only runs on `push` to `main` or when `run_cross_browser=true` in manual trigger.  
**Fix:** In workflow_dispatch, set **"Also run cross-browser matrix?"** to `true`.

### `categories.json` not appearing in the report

**Cause:** The file must be placed inside `target/allure-results/` before `allure:report` runs.  
**Fix:** The GitHub Actions workflow copies it automatically. Locally, copy it manually:
```bash
cp src/test/resources/categories.json target/allure-results/
mvn allure:report
```
