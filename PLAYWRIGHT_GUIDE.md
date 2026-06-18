# Playwright (Java) — Tutorial & Cheatsheet

> A practical, copy-paste tutorial for working with **Playwright for Java**, written around the
> patterns used in this repo (Playwright 1.47 + TestNG + Maven). Read top-to-bottom the first time;
> after that, use it as a cheatsheet — every section is self-contained.
>
> Official docs: <https://playwright.dev/java/> · Companion:
> [`INTERVIEW_QUESTIONS.md`](INTERVIEW_QUESTIONS.md), [`FRAMEWORK_GUIDE.md`](FRAMEWORK_GUIDE.md).

---

## Table of Contents

1. [Mental Model](#1-mental-model)
2. [Setup & First Test](#2-setup--first-test)
3. [The Core Objects: Playwright, Browser, Context, Page](#3-the-core-objects)
4. [Locators — Finding Elements](#4-locators--finding-elements)
5. [Actions — Interacting](#5-actions--interacting)
6. [Reading State](#6-reading-state)
7. [Auto-Waiting & Explicit Waits](#7-auto-waiting--explicit-waits)
8. [Assertions](#8-assertions)
9. [Navigation](#9-navigation)
10. [Frames, Dialogs, Tabs, Files](#10-frames-dialogs-tabs-files)
11. [Network: Intercept, Mock, Wait](#11-network-intercept-mock-wait)
12. [API Testing with APIRequestContext](#12-api-testing-with-apirequestcontext)
13. [Authentication & Storage State](#13-authentication--storage-state)
14. [Screenshots, Video, Tracing](#14-screenshots-video-tracing)
15. [Debugging](#15-debugging)
16. [Cross-Browser & Headless](#16-cross-browser--headless)
17. [Best Practices](#17-best-practices)
18. [Quick Cheatsheet Tables](#18-quick-cheatsheet-tables)

---

## 1. Mental Model

Playwright drives a real browser. Four ideas explain almost everything:

- **`Locator` is lazy.** It describes *how to find* an element, not the element itself. The DOM is
  queried (and auto-waited) only when you act on it. This is why locators never go "stale."
- **Auto-waiting is the default.** Before clicking/reading, Playwright waits for the element to be
  attached, visible, stable, and enabled (actionability) up to a timeout. You rarely sleep.
- **Contexts are isolation boundaries.** A `BrowserContext` is a fresh incognito profile (own
  cookies/storage). One browser → many contexts → many pages.
- **Web-first assertions retry.** `assertThat(locator).isVisible()` polls until true or times out —
  no manual wait loops.

---

## 2. Setup & First Test

**Maven dependency** (this repo uses 1.47.0):
```xml
<dependency>
  <groupId>com.microsoft.playwright</groupId>
  <artifactId>playwright</artifactId>
  <version>1.47.0</version>
</dependency>
```

**Install browser binaries** (one-time; the jar does *not* bundle them):
```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI \
              -Dexec.args="install --with-deps chromium"
# or: all engines
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps"
```

**Minimal standalone script** (try-with-resources auto-closes everything):
```java
import com.microsoft.playwright.*;

public class Quick {
    public static void main(String[] args) {
        try (Playwright pw = Playwright.create()) {
            Browser browser = pw.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newContext().newPage();
            page.navigate("https://easemytrade.in/");
            System.out.println(page.title());
            page.screenshot(new Page.ScreenshotOptions()
                .setPath(java.nio.file.Paths.get("home.png")));
            browser.close();
        }
    }
}
```

**Minimal TestNG test** (the style this repo uses):
```java
public class SmokeTest {
    Playwright pw; Browser browser; Page page;

    @BeforeClass void setup() {
        pw = Playwright.create();
        browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }
    @BeforeMethod void newPage() { page = browser.newContext().newPage(); }
    @AfterMethod  void close()   { page.context().close(); }
    @AfterClass   void teardown(){ browser.close(); pw.close(); }

    @Test void titleHasBrand() {
        page.navigate("https://easemytrade.in/");
        assertThat(page).hasTitle(java.util.regex.Pattern.compile("EaseMyTrade"));
    }
}
```

---

## 3. The Core Objects

```
Playwright            // the driver process — create once, close last
  └── Browser         // a launched engine (chromium/firefox/webkit) — expensive, reuse
        └── BrowserContext   // isolated session (cookies/storage) — cheap, one per test
              └── Page       // a tab
                    └── Frame  // iframes within a page
```

```java
Playwright pw = Playwright.create();

Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions()
    .setHeadless(true)
    .setSlowMo(50));                 // ms delay per action — great for watching/debugging

BrowserContext ctx = browser.newContext(new Browser.NewContextOptions()
    .setViewportSize(1440, 900)
    .setColorScheme(ColorScheme.DARK)
    .setLocale("en-IN")
    .setTimezoneId("Asia/Kolkata")
    .setIgnoreHTTPSErrors(true));

ctx.setDefaultTimeout(30_000);            // all actions
ctx.setDefaultNavigationTimeout(60_000);  // navigations only

Page page = ctx.newPage();
```

**Switching engines by string** (exactly how `BaseTest` does it):
```java
Browser browser = switch (browserName.toLowerCase()) {
    case "firefox" -> pw.firefox().launch(opts);
    case "webkit"  -> pw.webkit().launch(opts);
    default        -> pw.chromium().launch(opts);
};
```

**Rule of thumb:** launch the `Browser` once (per suite), create a fresh `BrowserContext` per test
for isolation, close `page`→`context` after each test, close `browser`→`playwright` at the end.

---

## 4. Locators — Finding Elements

Prefer **user-facing, role-based** locators; fall back to CSS/test-ids; avoid brittle XPath.

```java
// Recommended (resilient, semantic)
page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In"));
page.getByText("Trade Signal");
page.getByLabel("Username");
page.getByPlaceholder("Search a stock…");
page.getByTestId("confidence-meter");        // needs data-testid

// CSS (used throughout this repo's page objects)
page.locator(".brand");
page.locator("nav a[href='/expert-view/']");
page.locator("#liveClock");
page.locator("[id*='confidence'], [class*='confidence'], [data-confidence]");  // multi-pattern

// XPath (last resort)
page.locator("xpath=//div[@class='zone-card'][1]");
```

**Filtering & chaining:**
```java
page.locator(".zone-card").filter(new Locator.FilterOptions().setHasText("NIFTY"));
page.locator(".zone-card").first();
page.locator(".zone-card").last();
page.locator(".zone-card").nth(2);
page.locator("article").getByRole(AriaRole.LINK);     // scope within a parent
```

**Counting & iterating:**
```java
int n = page.locator(".zone-card").count();
for (Locator card : page.locator(".zone-card").all()) {
    System.out.println(card.innerText());
}
```

**Why `Locator` over the old `ElementHandle`:** locators re-resolve on every use (no staleness),
auto-wait, and support strictness (a locator that matches >1 element throws on action unless you
narrow with `.first()`/`.nth()`).

---

## 5. Actions — Interacting

All actions auto-wait for actionability first.

```java
loc.click();
loc.dblclick();
loc.click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));   // right-click
loc.fill("TCS");                       // clears then types (preferred for inputs)
loc.clear();
loc.pressSequentially("TCS", new Locator.PressSequentiallyOptions().setDelay(80)); // real keystrokes
loc.press("Enter");
loc.check();  loc.uncheck();           // checkboxes/radios
loc.selectOption("nifty50");           // <select> by value
loc.selectOption(new SelectOption().setLabel("NIFTY 50"));
loc.hover();
loc.focus();
loc.setInputFiles(Paths.get("upload.csv"));
loc.scrollIntoViewIfNeeded();
loc.dragTo(page.locator("#dropzone"));
```

Page-level keyboard/mouse when you need raw input:
```java
page.keyboard().press("Control+A");
page.mouse().wheel(0, 600);            // scroll
```

---

## 6. Reading State

```java
String text  = loc.innerText().trim();      // visible text
String all   = loc.textContent();           // includes hidden
String val   = loc.inputValue();            // form field value
String href  = loc.getAttribute("href");
boolean vis  = loc.isVisible();
boolean on   = loc.isEnabled();
boolean checked = loc.isChecked();
int count    = loc.count();

String title = page.title();
String url   = page.url();
```

**Defensive read pattern** (from this repo's `BasePage` — never let a missing element throw):
```java
public String textOf(String id) {
    try { return page.locator("#" + id).innerText().trim(); }
    catch (Exception e) { return ""; }     // absent → empty, let the test's assertion decide
}
```

**Run JS in the page** (escape hatch for things the API can't express):
```java
Object h = page.evaluate("() => document.body.scrollHeight");
String ua = (String) page.evaluate("() => navigator.userAgent");
```

---

## 7. Auto-Waiting & Explicit Waits

**Auto-wait** covers most cases — `click`/`fill`/`innerText` wait for actionability automatically.
Use explicit waits only for conditions the action can't infer:

```java
// Wait for an element state
loc.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
loc.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

// Wait for navigation / URL (used by this repo's login-gate logic)
page.waitForURL("**/login/**", new Page.WaitForURLOptions().setTimeout(2000));

// Wait for load lifecycle
page.waitForLoadState();                              // 'load'
page.waitForLoadState(LoadState.NETWORKIDLE);         // no network for 500ms
page.waitForLoadState(LoadState.DOMCONTENTLOADED);

// Wait for a response/request
page.waitForResponse("**/api/live-market", () -> page.locator("#refresh").click());

// Last resort — hard pause (avoid in real tests; ok for debug)
page.waitForTimeout(500);
```

**Custom polling predicate** (this repo's `waitUntil` — for composite app-state that no single
locator captures):
```java
void waitUntil(BooleanSupplier condition, int timeoutMs) {
    long deadline = System.currentTimeMillis() + timeoutMs;
    while (System.currentTimeMillis() < deadline) {
        if (condition.getAsBoolean()) return;
        page.waitForTimeout(500);
    }
}
// usage: waitUntil(() -> page.locator(".ai-card").count() >= 5, 15_000);
```

> ⚠️ Avoid `waitForTimeout` for synchronization — it's either too short (flaky) or too long (slow).
> Prefer waiting on a *condition*.

---

## 8. Assertions

**Web-first assertions** (auto-retrying — strongly preferred): `import static
com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;`
```java
assertThat(loc).isVisible();
assertThat(loc).isHidden();
assertThat(loc).isEnabled();
assertThat(loc).hasText("Trade Signal");
assertThat(loc).containsText("Buy");
assertThat(loc).hasValue("TCS");
assertThat(loc).hasAttribute("href", "/disclaimer/");
assertThat(loc).hasCount(4);
assertThat(page).hasTitle(Pattern.compile("EaseMyTrade"));
assertThat(page).hasURL(Pattern.compile(".*/expert-view/.*"));
```

**AssertJ** (used in this repo for parsed/computed values, with rich `.as(...)` messages):
```java
import static org.assertj.core.api.Assertions.assertThat;

assertThat(zoneCards).as("All 4 index zone cards must be present").isGreaterThanOrEqualTo(4);
assertThat(rr).as("R:R %.2f must be >= 2.2", rr).isGreaterThanOrEqualTo(2.2);
assertThat(state).as("state should not be Trade Signal").isNotEqualToIgnoringCase("Trade Signal");
assertThat(violations).as("Signals below 2.2R: " + violations).isEmpty();
```

**When to use which:** Playwright `assertThat` for *element/page conditions* (it retries on the live
DOM). AssertJ for *plain Java values* you've already extracted and parsed (numbers, lists, enums).

---

## 9. Navigation

```java
page.navigate("https://easemytrade.in/");
page.navigate(url, new Page.NavigateOptions()
    .setWaitUntil(WaitUntilState.NETWORKIDLE)
    .setTimeout(60_000));

page.reload();
page.goBack();
page.goForward();
```

**Navigation with retry** (this repo wraps navigate to survive `net::ERR_ABORTED` from an in-flight
redirect being cancelled):
```java
void navigateWithRetry(String url) {
    PlaywrightException last = null;
    for (int i = 0; i < 3; i++) {
        try { page.navigate(url); return; }
        catch (PlaywrightException e) { last = e; page.waitForTimeout(400); }
    }
    throw last;
}
```

---

## 10. Frames, Dialogs, Tabs, Files

```java
// iframe
Frame frame = page.frameLocator("#payment-iframe").owner().contentFrame();
page.frameLocator("#checkout").getByRole(AriaRole.BUTTON, ...).click();

// Dialogs (alert/confirm/prompt) — register handler BEFORE the trigger
page.onDialog(dialog -> { System.out.println(dialog.message()); dialog.accept(); });

// New tab / popup
Page popup = page.waitForPopup(() -> page.getByText("Open report").click());
popup.waitForLoadState();

// Downloads
Download dl = page.waitForDownload(() -> page.getByText("Export").click());
dl.saveAs(Paths.get("report.csv"));

// Uploads
page.locator("input[type=file]").setInputFiles(Paths.get("data.csv"));
```

---

## 11. Network: Intercept, Mock, Wait

```java
// Observe
page.onRequest(req  -> System.out.println("→ " + req.method() + " " + req.url()));
page.onResponse(res -> { if (res.status() >= 400) System.out.println("← " + res.status()); });

// Block resources (speed up / reduce flakiness)
page.route("**/*.{png,jpg,svg}", route -> route.abort());

// Mock an API response (test UI against fixed data)
page.route("**/api/live-market", route -> route.fulfill(new Route.FulfillOptions()
    .setStatus(200)
    .setContentType("application/json")
    .setBody("{\"nifty\":{\"signal\":{\"state\":\"No Trade\"}}}")));

// Wait for a specific call to complete
APIResponse r = page.waitForResponse("**/data/market.json",
    () -> page.reload()).request().response();
```

> Mocking is invaluable for testing UI states that are hard to produce on a live data feed (e.g.
> force a "Trade Signal" or an error response) without touching the backend.

---

## 12. API Testing with APIRequestContext

Playwright can hit HTTP endpoints directly — same process, no browser tab needed. This repo uses it
to validate signal logic against raw JSON.

```java
APIRequestContext api = playwright.request().newContext();

// GET with timeout
APIResponse res = api.get(BASE_URL + "/api/live-market",
    RequestOptions.create().setTimeout(120_000));

System.out.println(res.status());        // 200 / 304 / 401 ...
String body = res.text();

// POST JSON (login)
APIResponse login = api.post(BASE_URL + "/api/auth/login/",
    RequestOptions.create().setData(Map.of("username", u, "password", p)));
String setCookie = login.headers().get("set-cookie");

api.dispose();   // always release
```

**Parse & assert JSON with Jackson:**
```java
ObjectMapper mapper = new ObjectMapper();
JsonNode root = mapper.readTree(body);
JsonNode signal = root.path("nifty").path("signal");

assertThat(signal.has("state")).isTrue();
double stop   = signal.path("stopLoss").asDouble(-1);   // -1 sentinel if missing → no NPE
String state  = signal.path("state").asText("");
```

> `.path(...)` (not `.get(...)`) returns a missing-node instead of `null`, so chained reads with a
> default never throw — the pattern this repo relies on for resilient field access.

---

## 13. Authentication & Storage State

Two ways to avoid logging in through the UI on every test.

**A) API login → inject cookie** (this repo's approach):
```java
APIResponse res = api.post(BASE_URL + "/api/auth/login/", RequestOptions.create()
    .setData(Map.of("username", u, "password", p)));
String token = res.headers().get("set-cookie").split(";", 2)[0].split("=", 2)[1];

Cookie cookie = new Cookie("emt_session", token)
    .setDomain(".easemytrade.in").setPath("/")
    .setHttpOnly(true).setSecure(true).setSameSite(SameSiteAttribute.LAX);

context.addCookies(List.of(cookie));     // every fresh context starts logged in
```

**B) Storage state file** (log in once, reuse the whole session):
```java
// Save after a UI login:
context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("state.json")));

// Reuse in later contexts:
BrowserContext authed = browser.newContext(new Browser.NewContextOptions()
    .setStorageStatePath(Paths.get("state.json")));
```

**Drop / restore auth mid-test** (to visit a page as a logged-out user, then continue):
```java
context.clearCookies();                 // logged-out for the next navigation
context.addCookies(List.of(cookie));    // restore
```

---

## 14. Screenshots, Video, Tracing

```java
// Screenshot
page.screenshot(new Page.ScreenshotOptions()
    .setPath(Paths.get("page.png")).setFullPage(true));
byte[] png = loc.screenshot();          // element only

// Video — set at context creation
BrowserContext ctx = browser.newContext(new Browser.NewContextOptions()
    .setRecordVideoDir(Paths.get("videos/")));

// Trace — record then open the viewer
context.tracing().start(new Tracing.StartOptions()
    .setScreenshots(true).setSnapshots(true).setSources(true));
// ... run actions ...
context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));
// View: npx playwright show-trace trace.zip
```

**Attach to Allure** (this repo's helper does this for screenshots/source/JSON automatically on
failure):
```java
Allure.addAttachment("Failure Screenshot", "image/png",
    new ByteArrayInputStream(page.screenshot()), ".png");
```

---

## 15. Debugging

```bash
# Step through with Playwright Inspector
PWDEBUG=1 mvn test -Dtest=ExpertViewTest

# Watch the browser + slow it down
mvn test -Dheadless=false        # if your framework wires -Dheadless to LaunchOptions
```
```java
.setHeadless(false).setSlowMo(50)   // visually follow each action
page.pause();                        // opens Inspector and pauses (when headed)
```
- **Trace Viewer** (`show-trace trace.zip`) is the single best post-mortem tool: DOM snapshots,
  network, console, and the action timeline for every step.
- Page event listeners (`onConsoleMessage`, `onPageError`, `onResponse`) turn the test log into a
  live narrative — this repo logs all of them.

---

## 16. Cross-Browser & Headless

```java
pw.chromium().launch(opts);   // Chrome/Edge engine
pw.firefox().launch(opts);    // Gecko
pw.webkit().launch(opts);     // Safari engine
```
- Drive selection from a property: `-Dbrowser=webkit` → `switch` in setup (see §3).
- Headless is the default for CI (`setHeadless(true)`); flip to `false` locally to watch.
- Run the same suite across all three for parity; this repo's CI runs a 3-way matrix.

---

## 17. Best Practices

**Do**
- Prefer `getByRole` / `getByLabel` / `getByTestId` over CSS; CSS over XPath.
- Let auto-wait and web-first `assertThat(locator)` do the waiting.
- One `BrowserContext` per test for isolation; reuse the `Browser`.
- Keep selectors in **page objects**, assertions in **tests**.
- Use `APIRequestContext` to set up state / validate data fast.
- Always `api.dispose()` and `context.close()` (try/finally or lifecycle hooks).
- Use `.path(...).asX(default)` for resilient JSON reads.

**Don't**
- Don't `waitForTimeout` to "fix" flakiness — wait on a condition.
- Don't reuse `ElementHandle`s — use `Locator`s.
- Don't chain brittle absolute XPath.
- Don't share mutable state between tests (breaks parallelism).
- Don't assert on live data without guarding for "not yet populated" (`value > 0`).
- Don't log in through the UI on every test if an API/storage-state path exists.

---

## 18. Quick Cheatsheet Tables

### Lifecycle
| Goal | Code |
|---|---|
| Start | `Playwright pw = Playwright.create();` |
| Launch | `pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true))` |
| New session | `browser.newContext(opts)` |
| New tab | `context.newPage()` |
| Close | `page.close()` → `context.close()` → `browser.close()` → `pw.close()` |

### Locators
| Goal | Code |
|---|---|
| By role | `page.getByRole(AriaRole.BUTTON, new ...GetByRoleOptions().setName("Sign In"))` |
| By text | `page.getByText("Trade Signal")` |
| By label | `page.getByLabel("Username")` |
| By test id | `page.getByTestId("confidence-meter")` |
| CSS | `page.locator("nav a[href='/news/']")` |
| Nth / first | `page.locator(".card").nth(2)` / `.first()` |
| Filter | `.filter(new Locator.FilterOptions().setHasText("NIFTY"))` |
| Count | `page.locator(".card").count()` |

### Actions
| Goal | Code |
|---|---|
| Click | `loc.click()` |
| Type (clear first) | `loc.fill("TCS")` |
| Keystrokes | `loc.pressSequentially("TCS")` |
| Key | `loc.press("Enter")` |
| Dropdown | `loc.selectOption("nifty50")` |
| Check | `loc.check()` |
| Upload | `loc.setInputFiles(Paths.get("f.csv"))` |
| Hover | `loc.hover()` |

### Reads
| Goal | Code |
|---|---|
| Visible text | `loc.innerText()` |
| Input value | `loc.inputValue()` |
| Attribute | `loc.getAttribute("href")` |
| Visible? | `loc.isVisible()` |
| Title / URL | `page.title()` / `page.url()` |
| Run JS | `page.evaluate("() => ...")` |

### Waits
| Goal | Code |
|---|---|
| Element visible | `loc.waitFor(...setState(VISIBLE))` |
| URL | `page.waitForURL("**/login/**")` |
| Load state | `page.waitForLoadState(LoadState.NETWORKIDLE)` |
| Response | `page.waitForResponse("**/api/x", () -> trigger())` |

### Assertions
| Goal | Code |
|---|---|
| Element visible (retry) | `assertThat(loc).isVisible()` |
| Element text | `assertThat(loc).hasText("...")` |
| Page title | `assertThat(page).hasTitle(Pattern.compile("..."))` |
| Value (AssertJ) | `assertThat(rr).as("...").isGreaterThanOrEqualTo(2.2)` |

### API
| Goal | Code |
|---|---|
| New context | `playwright.request().newContext()` |
| GET | `api.get(url, RequestOptions.create().setTimeout(120_000))` |
| POST JSON | `api.post(url, RequestOptions.create().setData(Map.of(...)))` |
| Status / body | `res.status()` / `res.text()` |
| Parse | `new ObjectMapper().readTree(body)` |
| Cleanup | `api.dispose()` |

### CLI
| Goal | Command |
|---|---|
| Install browsers | `... CLI -Dexec.args="install --with-deps"` |
| Run all tests | `mvn test` |
| One class | `mvn test -Dtest=SignalLogicTest` |
| Pick browser | `mvn test -Dbrowser=firefox` |
| Watch | `mvn test -Dheadless=false` |
| Inspector | `PWDEBUG=1 mvn test` |
| View trace | `npx playwright show-trace trace.zip` |
| Allure report | `mvn allure:serve` |
