# EaseMyTrade Playwright Tests

Comprehensive Playwright Java automation test suite for [easemytrade.in](https://easemytrade.in).

## What This Tests

| Suite | Tests |
|---|---|
| Navigation | All public pages load, nav links work, redirects |
| Home Page | Hero, watchlist, trading zones, completed trades, market drivers |
| Login Page | Form validation, auth, brute-force cooldown |
| Signal Logic | State validation, support < resistance, 2.2R minimum, session guard, forward record guard |
| Expert View | Search, Best Pick, universe selector, confidence, recommendation, 5 robot cards |
| Index Pages | NIFTY, BANKNIFTY, FINNIFTY, SENSEX — signal state, levels, trade geometry |
| Content Pages | News, Methodology, Telegram, Contact, Disclaimer |

## Run Locally

```bash
# Install JDK 17+ and Maven, then:
mvn test -Dapp.base.url=https://easemytrade.in -Dbrowser=chromium -Dheadless=true

# View the Allure report:
mvn allure:serve
```

## Run on GitHub (no local setup)

1. Go to **Actions** → **EaseMyTrade Playwright Tests**
2. Click **Run workflow**
3. Choose browser, optionally override the base URL
4. Click **Run workflow**
5. Download the **allure-report** artifact once the run completes

The report is also published to GitHub Pages at:
`https://Prash0009.github.io/easemytrade-playwright-tests/reports/<run-number>/`

## Parameters

| Property | Default | Description |
|---|---|---|
| `app.base.url` | `https://easemytrade.in` | Target URL |
| `browser` | `chromium` | Browser: chromium / firefox / webkit |
| `headless` | `true` | Run headless |

## Key Logical Checks

- **Support < Resistance** — Verified for all 4 index zones on every run
- **2.2R minimum** — Any Trade Signal must have target at least 2.2× the risk
- **Buy geometry** — Stop below entry, target above entry for CE signals
- **Sell geometry** — Stop above entry, target below entry for PE signals
- **Confidence 0–100%** — Checked on both the home page and Expert View scan
- **Session guard** — If 2 completed trades shown, state must not be Trade Signal
- **Forward record guard** — Net negative record must block fresh Trade Signals
- **Signal state enum** — Must be one of: Trade Signal, Watchlist Only, No Trade, Hold, Pending

## Project Structure

```
src/test/java/com/easemytrade/
├── base/           BaseTest (setup / teardown / screenshot on failure)
├── config/         TestConfig (URLs, constants)
├── pages/          Page Object Model classes
├── tests/          Test classes (one per feature)
└── utils/          AllureAttachmentHelper, SignalParser
```
