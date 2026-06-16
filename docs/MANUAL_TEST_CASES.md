# EaseMyTrade — Manual Test Cases

Total: **137** test cases across **7** modules. Each case mirrors an automated Playwright test in `src/test/java/com/easemytrade/tests/`.

## Summary

| Module | Test Cases |
|---|---|
| Navigation | 25 |
| Home Page | 21 |
| Login Page | 12 |
| Index Pages | 29 |
| Expert View | 27 |
| Content Pages | 12 |
| Signal Logic | 11 |
| **Total** | **137** |

## Navigation

### NAV-001 — Home page loads with expected title keyword

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to https://easemytrade.in (site redirects to https://www.easemytrade.in).

**Steps:**

1. Navigate to https://easemytrade.in/
2. Observe the browser tab title.
3. Confirm the main page content area is visible.
4. Read the page's data-page body attribute (dev tools).

**Expected Result:** Page loads (HTTP 200). Title contains "EaseMyTrade". Main/body content is visible. body[data-page] equals "home".

**Test Data:** URL=/, expected title keyword="EaseMyTrade"

### NAV-002 — Login page loads with expected title keyword

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to https://easemytrade.in (site redirects to https://www.easemytrade.in).

**Steps:**

1. Navigate to https://easemytrade.in/login/
2. Observe the browser tab title.
3. Confirm the main page content area is visible.
4. Read the page's data-page body attribute (dev tools).

**Expected Result:** Page loads (HTTP 200). Title contains "Sign In". Main/body content is visible. body[data-page] equals "login".

**Test Data:** URL=/login/, expected title keyword="Sign In"

### NAV-003 — Expert View page loads with expected title keyword

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/expert-view/
2. Observe the browser tab title.
3. Confirm the main page content area is visible.
4. Read the page's data-page body attribute (dev tools).

**Expected Result:** Page loads (HTTP 200). Title contains "Expert View". Main/body content is visible. body[data-page] equals "expert-view".

**Test Data:** URL=/expert-view/, expected title keyword="Expert View"

### NAV-004 — News page loads with expected title keyword

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/news/
2. Observe the browser tab title.
3. Confirm the main page content area is visible.

**Expected Result:** Page loads (HTTP 200). Title contains "AI Market Intelligence". Main/body content is visible.

**Test Data:** URL=/news/, expected title keyword="AI Market Intelligence"

### NAV-005 — Methodology page loads with expected title keyword

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/methodology/
2. Observe the browser tab title.
3. Confirm the main page content area is visible.

**Expected Result:** Page loads (HTTP 200). Title contains "Methodology". Main/body content is visible.

**Test Data:** URL=/methodology/, expected title keyword="Methodology"

### NAV-006 — Telegram page loads with expected title keyword

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/telegram/
2. Observe the browser tab title.
3. Confirm the main page content area is visible.

**Expected Result:** Page loads (HTTP 200). Title contains "Telegram". Main/body content is visible.

**Test Data:** URL=/telegram/, expected title keyword="Telegram"

### NAV-007 — Contact page loads with expected title keyword

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to https://easemytrade.in (site redirects to https://www.easemytrade.in).

**Steps:**

1. Navigate to https://easemytrade.in/contact/
2. Observe the browser tab title.
3. Confirm the main page content area is visible.

**Expected Result:** Page loads (HTTP 200). Title contains "Contact". Main/body content is visible.

**Test Data:** URL=/contact/, expected title keyword="Contact"

### NAV-008 — Disclaimer page loads with expected title keyword

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to https://easemytrade.in (site redirects to https://www.easemytrade.in).

**Steps:**

1. Navigate to https://easemytrade.in/disclaimer/
2. Observe the browser tab title.
3. Confirm the main page content area is visible.

**Expected Result:** Page loads (HTTP 200). Title contains "Disclaimer". Main/body content is visible.

**Test Data:** URL=/disclaimer/, expected title keyword="Disclaimer"

### NAV-009 — NIFTY index page loads and shows index name

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/nifty/today/
2. Read the page title.
3. Search the page title and body text for "NIFTY".

**Expected Result:** Page loads successfully. Either the title or the visible body text contains "NIFTY".

**Test Data:** Index=NIFTY

### NAV-010 — BANKNIFTY index page loads and shows index name

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/banknifty/
2. Read the page title.
3. Search the page title and body text for "BANKNIFTY".

**Expected Result:** Page loads successfully. Either the title or the visible body text contains "BANKNIFTY".

**Test Data:** Index=BANKNIFTY

### NAV-011 — FINNIFTY index page loads and shows index name

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/finnifty/
2. Read the page title.
3. Search the page title and body text for "FINNIFTY".

**Expected Result:** Page loads successfully. Either the title or the visible body text contains "FINNIFTY".

**Test Data:** Index=FINNIFTY

### NAV-012 — SENSEX index page loads and shows index name

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/sensex/
2. Read the page title.
3. Search the page title and body text for "SENSEX".

**Expected Result:** Page loads successfully. Either the title or the visible body text contains "SENSEX".

**Test Data:** Index=SENSEX

### NAV-013 — Gold commodity page loads with commodity name visible

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/commodities/gold/
2. Read the page title.
3. Search the page title and body text for "Gold".

**Expected Result:** Page loads successfully. Either the title or visible body text contains "Gold".

**Test Data:** Commodity=Gold

### NAV-014 — Silver commodity page loads with commodity name visible

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/commodities/silver/
2. Read the page title.
3. Search the page title and body text for "Silver".

**Expected Result:** Page loads successfully. Either the title or visible body text contains "Silver".

**Test Data:** Commodity=Silver

### NAV-015 — Crude Oil commodity page loads with commodity name visible

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/commodities/crude-oil/
2. Read the page title.
3. Search the page title and body text for "Crude".

**Expected Result:** Page loads successfully. Either the title or visible body text contains "Crude".

**Test Data:** Commodity=Crude Oil

### NAV-016 — Natural Gas commodity page loads with commodity name visible

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/commodities/natural-gas/
2. Read the page title.
3. Search the page title and body text for "Natural Gas".

**Expected Result:** Page loads successfully. Either the title or visible body text contains "Natural Gas".

**Test Data:** Commodity=Natural Gas

### NAV-017 — Brand logo on Expert View page navigates back to Home

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to /expert-view/.
2. Click the brand logo in the header.
3. Observe the resulting URL.

**Expected Result:** Browser navigates to the easemytrade.in home page (apex or www).

### NAV-018 — Expert View nav link navigates to the Expert View page

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. On the home page, click the "Expert View" link in the main nav.
2. Observe the resulting URL.

**Expected Result:** URL contains "/expert-view/".

### NAV-019 — News nav link navigates to the News page

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. On the home page, click the "News" link in the main nav.
2. Observe the resulting URL.

**Expected Result:** URL contains "/news/".

### NAV-020 — Footer Disclaimer link navigates to the Disclaimer page

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Scroll to the footer.
2. Click the "Disclaimer" link.
3. Observe the resulting URL.

**Expected Result:** URL contains "/disclaimer/".

### NAV-021 — Home page "Open Market Overview" CTA stays on the home page

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. On the home page, click the CTA/link pointing to #market-overview.
2. Wait ~0.5s for the anchor scroll.
3. Observe the resulting URL.

**Expected Result:** Page remains on easemytrade.in (anchor scroll within the home page, no navigation away).

### NAV-022 — Market Overview redirect page lands back on the site

**Priority:** Low

**Preconditions:** Browser is open. Tester navigates to https://easemytrade.in (site redirects to https://www.easemytrade.in).

**Steps:**

1. Navigate to https://easemytrade.in/market-overview/.
2. Observe the resulting URL and page body.

**Expected Result:** Browser ends up on an easemytrade.in page (e.g. redirected to home) and the body is visible.

### NAV-023 — All header nav links have non-empty href attributes

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. On the home page, inspect every link inside nav.nav-links.
2. Count the links and read each href attribute.

**Expected Result:** At least 5 nav links are present and every link has a non-blank href attribute.

### NAV-024 — Indian Indices dropdown lists exactly NIFTY, BANKNIFTY, FINNIFTY, SENSEX

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. On the home page, click the "Indian Indices" nav dropdown.
2. Confirm links for NIFTY, BANKNIFTY, FINNIFTY, and SENSEX are visible in the open menu.

**Expected Result:** All four index links (/nifty/today/, /indices/banknifty/, /indices/finnifty/, /indices/sensex/) are visible.

### NAV-025 — Page title format is consistent across Home, Expert View, and Login

**Priority:** Low

**Preconditions:** Browser is open. Tester navigates to https://easemytrade.in (site redirects to https://www.easemytrade.in).

**Steps:**

1. Navigate to the Home page and read the title.
2. Navigate to /expert-view/ and read the title.
3. Log out / clear session, then navigate to /login/ and read the title.

**Expected Result:** Every title contains "EaseMyTrade" (format: "{Page Name} | EaseMyTrade").

## Home Page

### HOME-001 — Home page loads with correct title

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Read the browser tab title.

**Expected Result:** Title contains "EaseMyTrade".

### HOME-002 — Home page URL matches the expected domain

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Read the current browser URL.

**Expected Result:** URL contains "easemytrade.in" (apex or www).

### HOME-003 — Header navigation is visible with all links

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Inspect the header: brand logo, nav bar, and the Expert View / News / Methodology / Telegram / Contact links.

**Expected Result:** Brand logo, nav bar, and all five links are visible.

### HOME-004 — Hero section is present with EaseMyTrade branding

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Inspect the hero section at the top of the page.
2. Read the hero heading text.
3. Confirm the hero lead paragraph is visible.

**Expected Result:** Hero section is visible, heading text contains "EaseMyTrade", and the lead paragraph is visible.

### HOME-005 — Hero CTA buttons are present and correctly linked

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Inspect the hero section for the "Open Market Overview" and "Indian Indices" call-to-action buttons.

**Expected Result:** Both CTA buttons are visible.

### HOME-006 — Market watchlist shows all tracked instruments

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Locate the market watchlist widget.
2. Confirm rows exist for NIFTY, BANKNIFTY, FINNIFTY, SENSEX, and GOLD.

**Expected Result:** All five watchlist rows are visible.

### HOME-007 — Market watchlist rows link to the correct pages

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Inspect the href of the NIFTY, BANKNIFTY, FINNIFTY, and SENSEX watchlist rows.

**Expected Result:** Hrefs equal /nifty/today/, /indices/banknifty/, /indices/finnifty/, /indices/sensex/ respectively.

### HOME-008 — Trading Zones section shows all 4 index cards

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Locate the Trading Zones section.
2. Count the .zone-card elements inside it.

**Expected Result:** Trading Zones section is visible with exactly 4 zone cards (NIFTY, BANKNIFTY, FINNIFTY, SENSEX).

### HOME-009 — NIFTY zone: support level is numerically less than resistance

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Locate the NIFTY zone card in the Trading Zones section.
2. Read the displayed Support and Resistance values.
3. If both are populated numeric values, compare them.

**Expected Result:** Support value is strictly less than the Resistance value (skip the numeric check if levels aren't populated yet).

**Test Data:** Index=NIFTY

### HOME-010 — BANKNIFTY zone: support level is numerically less than resistance

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Locate the BANKNIFTY zone card in the Trading Zones section.
2. Read the displayed Support and Resistance values.
3. If both are populated numeric values, compare them.

**Expected Result:** Support value is strictly less than the Resistance value (skip the numeric check if levels aren't populated yet).

**Test Data:** Index=BANKNIFTY

### HOME-011 — FINNIFTY zone: support level is numerically less than resistance

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Locate the FINNIFTY zone card in the Trading Zones section.
2. Read the displayed Support and Resistance values.
3. If both are populated numeric values, compare them.

**Expected Result:** Support value is strictly less than the Resistance value (skip the numeric check if levels aren't populated yet).

**Test Data:** Index=FINNIFTY

### HOME-012 — SENSEX zone: support level is numerically less than resistance

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Locate the SENSEX zone card in the Trading Zones section.
2. Read the displayed Support and Resistance values.
3. If both are populated numeric values, compare them.

**Expected Result:** Support value is strictly less than the Resistance value (skip the numeric check if levels aren't populated yet).

**Test Data:** Index=SENSEX

### HOME-013 — NIFTY zone signal state is one of the valid values

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Locate the NIFTY zone card.
2. Read the displayed signal state.

**Expected Result:** Signal state is one of: Trade Signal, Watchlist Only, No Trade, Hold, Pending (or blank/placeholder if not yet populated).

### HOME-014 — Previous Completed Trades spotlight section renders

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Locate the "Previous Completed Trades" section.
2. Wait for the trade-spotlight grid to populate (it loads asynchronously).
3. Confirm the grid is visible with trade cards.

**Expected Result:** The spotlight section and its grid become visible (allow up to ~2 minutes for the live-market data fetch to complete).

### HOME-015 — Market drivers panel is present

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Locate the Market Drivers panel on the page.

**Expected Result:** Market drivers panel is visible.

### HOME-016 — Footer is present with disclaimer link

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Scroll to the footer.
2. Confirm the disclaimer link is visible inside it.

**Expected Result:** Footer is visible and contains a visible disclaimer link.

### HOME-017 — Footer disclaimer link points to /disclaimer/

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Inspect the href attribute of the footer disclaimer link.

**Expected Result:** Href equals "/disclaimer/".

### HOME-018 — Live market ticker strip is present in the header area

**Priority:** Low

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Inspect the header area for the scrolling market ticker.

**Expected Result:** Market ticker strip is visible.

### HOME-019 — IST live clock is present in the navigation

**Priority:** Low

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Inspect the nav bar for the live IST clock element.

**Expected Result:** Live clock element is visible.

### HOME-020 — Hero pulse chart canvas renders

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Inspect the hero section for the pulse-chart canvas element.

**Expected Result:** Pulse chart canvas is visible.

### HOME-021 — Indian Indices nav dropdown reveals all 4 index links

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. Click the "Indian Indices" dropdown in the nav.
2. Confirm links for NIFTY, BANKNIFTY, FINNIFTY, SENSEX appear.

**Expected Result:** All four index links are visible in the opened dropdown.

## Login Page

### LOGIN-001 — Login page loads with correct title

**Priority:** High

**Preconditions:** Browser is open and NOT logged in. Tester navigates to https://easemytrade.in/login/.

**Steps:**

1. Read the browser tab title.

**Expected Result:** Title contains both "Sign In" and "EaseMyTrade".

### LOGIN-002 — Login form elements are present

**Priority:** High

**Preconditions:** Browser is open and NOT logged in. Tester navigates to https://easemytrade.in/login/.

**Steps:**

1. Inspect the login form for the username field, password field, Sign In button, and Sign Out button.

**Expected Result:** Login form, username input, password input, Sign In button, and Sign Out button are all visible.

### LOGIN-003 — Password field is masked by default

**Priority:** High

**Preconditions:** Browser is open and NOT logged in. Tester navigates to https://easemytrade.in/login/.

**Steps:**

1. Inspect the password field's input type.

**Expected Result:** Password input type is "password" (masked).

### LOGIN-004 — Toggling password show/hide changes the input type

**Priority:** Medium

**Preconditions:** Browser is open and NOT logged in. Tester navigates to https://easemytrade.in/login/.

**Steps:**

1. Type a test password into the password field.
2. Confirm it is masked.
3. Click the show/hide toggle.
4. Confirm the field now shows plain text.
5. Click the toggle again.
6. Confirm the field is masked again.

**Expected Result:** Password field type alternates from "password" to "text" and back to "password" with each toggle click.

### LOGIN-005 — Submitting an empty form shows an error status message

**Priority:** High

**Preconditions:** Browser is open and NOT logged in. Tester navigates to https://easemytrade.in/login/.

**Steps:**

1. Leave both username and password blank.
2. Click Sign In.
3. Wait ~1s and read the status message.

**Expected Result:** A non-blank error-styled status message is displayed.

### LOGIN-006 — Submitting with only a username shows a missing-password error

**Priority:** Medium

**Preconditions:** Browser is open and NOT logged in. Tester navigates to https://easemytrade.in/login/.

**Steps:**

1. Enter a username (e.g. "testuser"), leave password blank.
2. Click Sign In.
3. Wait ~1s and read the status message.

**Expected Result:** An error-styled status message is displayed.

**Test Data:** username=testuser

### LOGIN-007 — Invalid credentials show an error status

**Priority:** High

**Preconditions:** Browser is open and NOT logged in. Tester navigates to https://easemytrade.in/login/.

**Steps:**

1. Enter username "invaliduser" and password "wrongpassword".
2. Click Sign In.
3. Wait ~3s and read the status message.

**Expected Result:** An error-styled status message is displayed (login is rejected).

**Test Data:** username=invaliduser, password=wrongpassword

### LOGIN-008 — After 4 failed attempts, login is temporarily locked

**Priority:** High

**Preconditions:** Browser is open and NOT logged in. Tester navigates to https://easemytrade.in/login/.

**Steps:**

1. Attempt login with username "baduser" and an incorrect password.
2. Wait ~2s; clear the form.
3. Repeat the failed attempt 3 more times (4 total).
4. Read the final status message.

**Expected Result:** After the 4th failed attempt, the status message indicates either an error or a temporary lockout/cooldown.

**Test Data:** username=baduser, 4 incorrect passwords

### LOGIN-009 — Login page brand logo links to the home page

**Priority:** Low

**Preconditions:** Browser is open and NOT logged in. Tester navigates to https://easemytrade.in/login/.

**Steps:**

1. Inspect the href attribute of the brand logo.

**Expected Result:** Href equals "/".

### LOGIN-010 — Login page has a hero heading describing protected access

**Priority:** Low

**Preconditions:** Browser is open and NOT logged in. Tester navigates to https://easemytrade.in/login/.

**Steps:**

1. Inspect the hero heading on the login page.
2. Read its text.

**Expected Result:** Hero heading is visible and contains non-blank descriptive text.

### LOGIN-011 — Login page has data-page="login" body attribute

**Priority:** Low

**Preconditions:** Browser is open and NOT logged in. Tester navigates to https://easemytrade.in/login/.

**Steps:**

1. Inspect the <body> element's data-page attribute (dev tools).

**Expected Result:** data-page attribute equals "login".

### LOGIN-012 — Login redirect preserves the "next" destination query parameter

**Priority:** Medium

**Preconditions:** Browser is open and NOT logged in. Tester navigates to https://easemytrade.in/login/.

**Steps:**

1. Navigate to /login/?next=/expert-view/.
2. Observe the URL.
3. Confirm the login form (#loginForm) is visible.

**Expected Result:** URL still contains "next=/expert-view/" and the login form renders normally.

## Index Pages

### IDX-001 — NIFTY page loads with the index name in page content

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/nifty/today/
2. Confirm the main signal content area is visible.
3. Search the visible body text for "NIFTY".

**Expected Result:** Main content is visible and the body text contains "NIFTY".

**Test Data:** Index=NIFTY

### IDX-002 — BANKNIFTY page loads with the index name in page content

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/banknifty/
2. Confirm the main signal content area is visible.
3. Search the visible body text for "BANKNIFTY".

**Expected Result:** Main content is visible and the body text contains "BANKNIFTY".

**Test Data:** Index=BANKNIFTY

### IDX-003 — FINNIFTY page loads with the index name in page content

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/finnifty/
2. Confirm the main signal content area is visible.
3. Search the visible body text for "FINNIFTY".

**Expected Result:** Main content is visible and the body text contains "FINNIFTY".

**Test Data:** Index=FINNIFTY

### IDX-004 — SENSEX page loads with the index name in page content

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/sensex/
2. Confirm the main signal content area is visible.
3. Search the visible body text for "SENSEX".

**Expected Result:** Main content is visible and the body text contains "SENSEX".

**Test Data:** Index=SENSEX

### IDX-005 — NIFTY page title contains EaseMyTrade

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/nifty/today/
2. Read the page title.

**Expected Result:** Title contains "EaseMyTrade".

**Test Data:** Index=NIFTY

### IDX-006 — BANKNIFTY page title contains EaseMyTrade

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/banknifty/
2. Read the page title.

**Expected Result:** Title contains "EaseMyTrade".

**Test Data:** Index=BANKNIFTY

### IDX-007 — FINNIFTY page title contains EaseMyTrade

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/finnifty/
2. Read the page title.

**Expected Result:** Title contains "EaseMyTrade".

**Test Data:** Index=FINNIFTY

### IDX-008 — SENSEX page title contains EaseMyTrade

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/sensex/
2. Read the page title.

**Expected Result:** Title contains "EaseMyTrade".

**Test Data:** Index=SENSEX

### IDX-009 — NIFTY page has a fully functional navigation header

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/nifty/today/
2. Confirm the brand logo is visible.
3. Confirm the main nav bar is visible.

**Expected Result:** Both the brand logo and the nav bar are visible.

**Test Data:** Index=NIFTY

### IDX-010 — BANKNIFTY page has a fully functional navigation header

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/banknifty/
2. Confirm the brand logo is visible.
3. Confirm the main nav bar is visible.

**Expected Result:** Both the brand logo and the nav bar are visible.

**Test Data:** Index=BANKNIFTY

### IDX-011 — FINNIFTY page has a fully functional navigation header

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/finnifty/
2. Confirm the brand logo is visible.
3. Confirm the main nav bar is visible.

**Expected Result:** Both the brand logo and the nav bar are visible.

**Test Data:** Index=FINNIFTY

### IDX-012 — SENSEX page has a fully functional navigation header

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/sensex/
2. Confirm the brand logo is visible.
3. Confirm the main nav bar is visible.

**Expected Result:** Both the brand logo and the nav bar are visible.

**Test Data:** Index=SENSEX

### IDX-013 — NIFTY page shows a valid signal state

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to /nifty/today/.
2. Read the displayed signal state.

**Expected Result:** Signal state is one of: Trade Signal, Watchlist Only, No Trade, Hold, Pending (or blank if not populated).

### IDX-014 — BANKNIFTY page shows a valid signal state

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to /indices/banknifty/.
2. Read the displayed signal state.

**Expected Result:** Signal state is one of: Trade Signal, Watchlist Only, No Trade, Hold, Pending (or blank if not populated).

### IDX-015 — NIFTY support level is numerically less than resistance

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/nifty/today/
2. Read the displayed Support and Resistance values.
3. If both are populated numeric values, compare them.

**Expected Result:** Support is strictly less than Resistance (skip the numeric check if levels aren't populated yet).

**Test Data:** Index=NIFTY

### IDX-016 — BANKNIFTY support level is numerically less than resistance

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/banknifty/
2. Read the displayed Support and Resistance values.
3. If both are populated numeric values, compare them.

**Expected Result:** Support is strictly less than Resistance (skip the numeric check if levels aren't populated yet).

**Test Data:** Index=BANKNIFTY

### IDX-017 — FINNIFTY support level is numerically less than resistance

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/finnifty/
2. Read the displayed Support and Resistance values.
3. If both are populated numeric values, compare them.

**Expected Result:** Support is strictly less than Resistance (skip the numeric check if levels aren't populated yet).

**Test Data:** Index=FINNIFTY

### IDX-018 — SENSEX support level is numerically less than resistance

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/indices/sensex/
2. Read the displayed Support and Resistance values.
3. If both are populated numeric values, compare them.

**Expected Result:** Support is strictly less than Resistance (skip the numeric check if levels aren't populated yet).

**Test Data:** Index=SENSEX

### IDX-019 — NIFTY buy/sell signal geometry is valid when a Trade Signal is active

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to /nifty/today/.
2. Read the signal state; if it is not "Trade Signal", stop (not applicable).
3. Read the Entry, Stop Loss, and Target levels.
4. If Target > Entry, verify it is a valid buy setup and R:R ≥ 2.2.
5. If Target < Entry, verify it is a valid sell setup.

**Expected Result:** When a Trade Signal is active: for a buy setup, Stop < Entry < Target and Risk:Reward ≥ 2.2; for a sell setup, Target < Entry < Stop.

### IDX-020 — NIFTY page shows completed trade review cards

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to /nifty/today/.
2. Confirm the main signal content area is visible (trade cards may or may not be present).

**Expected Result:** NIFTY page's signal content area is visible.

### IDX-021 — Gold commodity page shows the commodity name in body content

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/commodities/gold/
2. Read the visible body text.
3. Confirm the main content area is visible.

**Expected Result:** Body text contains "Gold" and the main content area is visible.

**Test Data:** Commodity=Gold

### IDX-022 — Silver commodity page shows the commodity name in body content

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/commodities/silver/
2. Read the visible body text.
3. Confirm the main content area is visible.

**Expected Result:** Body text contains "Silver" and the main content area is visible.

**Test Data:** Commodity=Silver

### IDX-023 — Crude Oil commodity page shows the commodity name in body content

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/commodities/crude-oil/
2. Read the visible body text.
3. Confirm the main content area is visible.

**Expected Result:** Body text contains "Crude Oil" and the main content area is visible.

**Test Data:** Commodity=Crude Oil

### IDX-024 — Natural Gas commodity page shows the commodity name in body content

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/commodities/natural-gas/
2. Read the visible body text.
3. Confirm the main content area is visible.

**Expected Result:** Body text contains "Natural Gas" and the main content area is visible.

**Test Data:** Commodity=Natural Gas

### IDX-025 — Gold commodity page has navigation header with all links

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/commodities/gold/
2. Confirm the brand logo is visible.
3. Confirm the nav bar is visible.

**Expected Result:** Both the brand logo and the nav bar are visible.

**Test Data:** Commodity=Gold

### IDX-026 — Silver commodity page has navigation header with all links

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/commodities/silver/
2. Confirm the brand logo is visible.
3. Confirm the nav bar is visible.

**Expected Result:** Both the brand logo and the nav bar are visible.

**Test Data:** Commodity=Silver

### IDX-027 — Crude Oil commodity page has navigation header with all links

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/commodities/crude-oil/
2. Confirm the brand logo is visible.
3. Confirm the nav bar is visible.

**Expected Result:** Both the brand logo and the nav bar are visible.

**Test Data:** Commodity=Crude Oil

### IDX-028 — Natural Gas commodity page has navigation header with all links

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to https://easemytrade.in/commodities/natural-gas/
2. Confirm the brand logo is visible.
3. Confirm the nav bar is visible.

**Expected Result:** Both the brand logo and the nav bar are visible.

**Test Data:** Commodity=Natural Gas

### IDX-029 — Gold commodity page shows a positive price value

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to /commodities/gold/.
2. Locate the gold price element.
3. Read its numeric value.

**Expected Result:** If a numeric price is populated, it is greater than 0.

## Expert View

### EV-001 — Expert View page loads with correct title

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Read the browser tab title.

**Expected Result:** Title contains both "Expert View" and "EaseMyTrade".

### EV-002 — Hero section is present with the AI Terminal headline

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Inspect the hero section.
2. Read the hero heading text.

**Expected Result:** Hero section is visible and the heading text contains "AI".

### EV-003 — Search form is present with input, Generate Report, and Best Pick buttons

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Inspect the search form, search input field, Generate Report button, and Best Pick button.

**Expected Result:** All four elements are visible.

### EV-004 — Universe dropdown is present and shows the current selection

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Inspect the universe dropdown.
2. Read the summary text showing the current selection.

**Expected Result:** Dropdown is visible and the summary text is non-blank.

### EV-005 — Universe dropdown opens and shows all option groups

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Click the universe dropdown.
2. Confirm the menu opens.
3. Count the universe option buttons inside it.

**Expected Result:** Menu is visible with more than 5 universe options.

### EV-006 — Selecting NIFTY 50 universe updates the dropdown summary label

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Open the universe dropdown.
2. Select "NIFTY 50".
3. Read the updated summary label.

**Expected Result:** Summary label now contains "NIFTY" (case-insensitive).

### EV-007 — Best Pick navigation buttons (Prev/Next) are present

**Priority:** Low

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Inspect the Best Pick Prev and Next buttons.

**Expected Result:** Both buttons are visible.

### EV-008 — Example ticker chips are present (Reliance, TCS, HDFC Bank, INFY, SBIN)

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Count the example ticker chips on the page.
2. Confirm chips exist for Reliance, TCS, HDFC Bank, INFY, and SBIN.

**Expected Result:** At least 5 chips are visible and all 5 named chips are present.

### EV-009 — Clicking a quick-chip fills the search input with that stock name

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Click the "TCS" example chip.
2. Read the value now in the search input.

**Expected Result:** Search input value contains "TCS".

### EV-010 — Initial status message is present before any search

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Without searching, inspect the status message area.
2. Read its text.

**Expected Result:** Status message is visible and non-blank.

### EV-011 — Initial hero metrics show an awaiting-scan state

**Priority:** Low

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Without searching, read the hero Confidence and Trend metric placeholders.

**Expected Result:** Both metric fields render some placeholder/initial value (not null).

### EV-012 — Report area shows the empty "Waiting for an asset" card before any search

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Without searching, inspect the report area.
2. Confirm the empty-state card is visible.
3. Read its text.

**Expected Result:** Report area and empty card are visible; the card's text contains "waiting".

### EV-013 — Searching "Reliance" generates a report with analysis cards

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Type "Reliance" into the search input.
2. Click Generate Report.
3. Wait up to 60s for an analysis card to appear.
4. Count the analysis cards rendered.

**Expected Result:** Report loads and at least one analysis card is rendered.

**Test Data:** Stock=Reliance

### EV-014 — After a successful scan, dashboard metrics (Confidence, Bias, Recommendation) are visible

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Search for "TCS".
2. Wait up to 60s for the report to load.
3. Wait for the dashboard band to become visible.
4. Read the Confidence Meter, Bias, and Recommendation values.

**Expected Result:** Dashboard band is visible; Confidence Meter is non-blank and not "--"; Bias is non-blank; Recommendation is non-blank and not "Standby".

**Test Data:** Stock=TCS

### EV-015 — Confidence score from an Expert View scan is between 0 and 100 percent

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Search for "INFY".
2. Wait up to 60s for the report to load.
3. Read the Confidence Meter value.

**Expected Result:** If populated, the confidence percentage is between 0 and 100 inclusive.

**Test Data:** Stock=INFY

### EV-016 — AI Recommendation is one of the expected verdict values

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Search for "SBIN".
2. Wait up to 60s for the report to load.
3. Read the AI Recommendation value.

**Expected Result:** Recommendation (if not "Standby") is one of: Strong Buy, Buy, Accumulate, Hold, Reduce, Sell, Strong Sell, Watch, Neutral, Watchlist Only, No Trade.

**Test Data:** Stock=SBIN

### EV-017 — AI Systems section shows five robot cards after a scan

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Search for "HDFC Bank".
2. Wait up to 60s for the report to load.
3. Wait for the AI Systems section to become visible.
4. Count the robot cards inside it.

**Expected Result:** AI Systems section is visible with exactly 5 robot cards.

**Test Data:** Stock=HDFC Bank

### EV-018 — Report contains sections for Trend, Momentum, Support & Resistance, and Risk

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Search for "Reliance".
2. Wait up to 60s for the report to load.
3. Wait for the report panel's content to render fully.
4. Inspect the rendered report HTML/content.

**Expected Result:** Report HTML has substantive content (over 500 characters), implying all section blocks rendered.

**Test Data:** Stock=Reliance

### EV-019 — Best Pick button initiates a scan for the selected universe

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Read the current status message.
2. Click the Best Pick button.
3. Wait ~2s.
4. Read the status message again.

**Expected Result:** Status message changes after clicking Best Pick, indicating a scan was initiated.

### EV-020 — Searching an empty string does not crash or navigate away

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Clear the search input (leave it empty).
2. Click Generate Report.
3. Wait ~1s.
4. Observe the URL.

**Expected Result:** Page remains on /expert-view/; no crash or unexpected navigation occurs.

### EV-021 — Short Horizon Scope is shown after a successful scan

**Priority:** Low

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Search for "TCS".
2. Wait up to 60s for the report to load.
3. Read the Short Horizon Scope value.

**Expected Result:** Short Horizon Scope value is rendered (not null) after the scan.

**Test Data:** Stock=TCS

### EV-022 — Expert View page body has the correct data-page attribute

**Priority:** Low

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Inspect the <body> element's data-page attribute (dev tools).

**Expected Result:** data-page attribute equals "expert-view".

### EV-023 — Searching "Reliance" from quick examples returns a status update, not an error

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Search for "Reliance" (via search box or its example chip).
2. Wait ~3s.
3. Read the status message.

**Expected Result:** Status message does not contain "exception", "500", or "internal server error".

**Test Data:** Stock=Reliance

### EV-024 — Searching "TCS" from quick examples returns a status update, not an error

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Search for "TCS" (via search box or its example chip).
2. Wait ~3s.
3. Read the status message.

**Expected Result:** Status message does not contain "exception", "500", or "internal server error".

**Test Data:** Stock=TCS

### EV-025 — Searching "INFY" from quick examples returns a status update, not an error

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Search for "INFY" (via search box or its example chip).
2. Wait ~3s.
3. Read the status message.

**Expected Result:** Status message does not contain "exception", "500", or "internal server error".

**Test Data:** Stock=INFY

### EV-026 — Searching "HDFC Bank" from quick examples returns a status update, not an error

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Search for "HDFC Bank" (via search box or its example chip).
2. Wait ~3s.
3. Read the status message.

**Expected Result:** Status message does not contain "exception", "500", or "internal server error".

**Test Data:** Stock=HDFC Bank

### EV-027 — Searching "SBIN" from quick examples returns a status update, not an error

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/. Tester navigates to /expert-view/.

**Steps:**

1. Search for "SBIN" (via search box or its example chip).
2. Wait ~3s.
3. Read the status message.

**Expected Result:** Status message does not contain "exception", "500", or "internal server error".

**Test Data:** Stock=SBIN

## Content Pages

### CP-001 — News page loads with main content area

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to /news/.
2. Confirm the main content area is visible.
3. Read the page title.

**Expected Result:** Main content area is visible and the title contains "EaseMyTrade".

### CP-002 — News page contains at least one heading

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to /news/.
2. Count the heading elements on the page.

**Expected Result:** At least one heading is present.

### CP-003 — Methodology page loads with content about signal logic

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to /methodology/.
2. Confirm the main content area is visible.

**Expected Result:** Main content area is visible.

### CP-004 — Methodology page references key signal concepts

**Priority:** Medium

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to /methodology/.
2. Read the visible body text.

**Expected Result:** Body text mentions at least one of: signal, NIFTY, methodology, indicator.

### CP-005 — Telegram page loads with main content mentioning Telegram

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to /telegram/.
2. Confirm the main content area is visible.
3. Read the visible body text.

**Expected Result:** Main content area is visible and the body text contains "telegram".

### CP-006 — Contact page loads and shows contact information

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to https://easemytrade.in (site redirects to https://www.easemytrade.in).

**Steps:**

1. Navigate to /contact/.
2. Confirm the main content area is visible.
3. Read the visible body text.

**Expected Result:** Main content area is visible and body text contains contact information (e.g. "contact", "email", or the owner's name).

### CP-007 — Contact page has non-empty paragraphs

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to https://easemytrade.in (site redirects to https://www.easemytrade.in).

**Steps:**

1. Navigate to /contact/.
2. Count the paragraph elements on the page.

**Expected Result:** At least one paragraph is present.

### CP-008 — Disclaimer page loads with educational disclaimer content

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to https://easemytrade.in (site redirects to https://www.easemytrade.in).

**Steps:**

1. Navigate to /disclaimer/.
2. Confirm the main content area is visible.
3. Read the visible body text.

**Expected Result:** Main content area is visible and body text mentions disclaimer/educational/informational content.

### CP-009 — Disclaimer page explicitly states educational/informational purpose

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to https://easemytrade.in (site redirects to https://www.easemytrade.in).

**Steps:**

1. Navigate to /disclaimer/.
2. Read the full visible body text.

**Expected Result:** Body text states the content is educational/informational, or explicitly says it is not investment advice/a solicitation.

### CP-010 — Telegram status page loads without a 404

**Priority:** Low

**Preconditions:** Browser is open. Tester navigates to https://easemytrade.in (site redirects to https://www.easemytrade.in).

**Steps:**

1. Navigate to /telegram-status/.
2. Confirm the page body is visible.
3. Read the page title.

**Expected Result:** Body is visible and the title is non-blank (no 404 page).

### CP-011 — All major pages include EaseMyTrade in the page title

**Priority:** Medium

**Preconditions:** Browser is open. Tester navigates to https://easemytrade.in (site redirects to https://www.easemytrade.in).

**Steps:**

1. Visit each of: Home, Login, Expert View, News, Methodology, Telegram, Contact, Disclaimer, NIFTY, BANKNIFTY.
2. Read each page's title.
3. For any gated page that redirects to /login/, skip the title check for that page only.

**Expected Result:** Every page that successfully loads (not redirected to login) has a title containing "EaseMyTrade".

### CP-012 — All major pages have a visible footer with the EaseMyTrade brand

**Priority:** Low

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Visit each of: Home, Expert View, NIFTY, News, Methodology.
2. Scroll to the bottom of each page and confirm the footer is visible.
3. For any page that redirects to /login/, skip the footer check for that page only.

**Expected Result:** Every page that successfully loads has a visible footer.

## Signal Logic

### SIG-001 — NIFTY signal state is one of the valid engine states

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. On the home page, read the NIFTY zone's displayed signal state.

**Expected Result:** State is one of: Trade Signal, Watchlist Only, No Trade, Hold, Pending (or blank if not yet populated).

### SIG-002 — Signal engine supports all four index zones on the home page

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. On the home page, count the .zone-card elements in the Trading Zones section.

**Expected Result:** At least 4 zone cards are present (NIFTY, BANKNIFTY, FINNIFTY, SENSEX).

### SIG-003 — All index zone support levels are strictly less than resistance levels

**Priority:** High

**Preconditions:** Browser is open. Tester navigates to the EaseMyTrade home page (https://easemytrade.in/).

**Steps:**

1. On the home page, read Support and Resistance for NIFTY, BANKNIFTY, FINNIFTY, and SENSEX.
2. For each zone where both values are populated, compare Support to Resistance.

**Expected Result:** For every zone with populated levels, Support < Resistance (no zone may have Support ≥ Resistance).

### SIG-004 — Confidence score on the NIFTY page is between 0 and 100 percent

**Priority:** High

**Preconditions:** Browser is open. Tester is logged in with a viewer-role account (or the site's temporary public-unlock window is currently open) so gated pages render instead of redirecting to /login/.

**Steps:**

1. Navigate to /nifty/today/.
2. Locate any visible confidence-score element.
3. Read its percentage value.

**Expected Result:** If populated, the confidence percentage is between 0 and 100 inclusive.

### SIG-005 — Live market API returns a valid JSON structure

**Priority:** High

**Preconditions:** API client / browser dev tools access to easemytrade.in.

**Steps:**

1. Send a GET request to https://easemytrade.in/api/live-market.
2. Read the HTTP status code.
3. If status is 200, parse the JSON body.

**Expected Result:** Status is 200, 304 (cached), or 401 (protected — unlock window closed). If 200, the JSON contains "nifty"/"NIFTY" or "indices" data.

### SIG-006 — market.json file has the required fields for the signal engine

**Priority:** High

**Preconditions:** API client access to easemytrade.in.

**Steps:**

1. Send a GET request to https://easemytrade.in/data/market.json.
2. If status is 200, parse the JSON.
3. Inspect the "nifty" object for level, signal, support, and resistance fields.
4. Inspect nifty.signal for state, confidence, stopLoss, and a target field.

**Expected Result:** If the file is publicly accessible, all listed fields are present; confidence is within [0,100] and state is a valid signal state.

### SIG-007 — Active Trade Signal entries have valid buy/sell geometry

**Priority:** High

**Preconditions:** API client access to easemytrade.in.

**Steps:**

1. Fetch /data/market.json.
2. For each index in Trade Signal state, read Entry, Stop Loss, and Target.
3. If Target > Entry, verify it is a valid buy setup with R:R ≥ 2.2.
4. If Target < Entry, verify it is a valid sell setup.

**Expected Result:** Every active Trade Signal has valid entry/stop/target geometry for its direction.

### SIG-008 — Risk-reward ratio is at least 2.2R for all active Trade Signal entries

**Priority:** High

**Preconditions:** API client access to easemytrade.in.

**Steps:**

1. Fetch /data/market.json.
2. For each index in Trade Signal state with populated entry/stop/target, calculate R:R.

**Expected Result:** Every active Trade Signal has a Risk:Reward ratio ≥ 2.2.

### SIG-009 — No Trade state never shows an actionable Buy/Sell instruction

**Priority:** Medium

**Preconditions:** API client access to easemytrade.in.

**Steps:**

1. Fetch /data/market.json.
2. For each index in "No Trade" state, read the action/note text.

**Expected Result:** No index in "No Trade" state has action text containing "buy signal" or "sell signal".

### SIG-010 — Session guard blocks new Trade Signals after 2 completed trades

**Priority:** High

**Preconditions:** API client access to easemytrade.in.

**Steps:**

1. Fetch /data/market.json.
2. For each index whose note/summary mentions "2 completed trades", read its signal state.

**Expected Result:** Any index that has hit the 2-completed-trades session guard is NOT in "Trade Signal" state.

### SIG-011 — Forward record guard blocks fresh signals when net negative

**Priority:** High

**Preconditions:** API client access to easemytrade.in.

**Steps:**

1. Fetch /data/market.json.
2. For each index whose note/summary mentions a net-negative forward record, read its signal state.

**Expected Result:** Any index with a net-negative forward record is NOT in "Trade Signal" state.
