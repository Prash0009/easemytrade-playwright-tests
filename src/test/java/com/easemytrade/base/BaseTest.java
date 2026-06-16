package com.easemytrade.base;

import com.easemytrade.config.TestConfig;
import com.easemytrade.utils.AllureAttachmentHelper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.ColorScheme;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.time.Duration;
import java.time.Instant;

public class BaseTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    // ── Suite lifecycle ────────────────────────────────────────────────────────

    @BeforeSuite(alwaysRun = true)
    public void launchBrowser() {
        log.debug("═══════════════════════════════════════════════════");
        log.debug("  EaseMyTrade Playwright Test Suite — starting up");
        log.debug("  Base URL  : {}", TestConfig.BASE_URL);
        log.debug("  Browser   : {}", TestConfig.BROWSER);
        log.debug("  Headless  : {}", TestConfig.HEADLESS);
        log.debug("  Timeout   : {}ms (default), {}ms (navigation)", TestConfig.DEFAULT_TIMEOUT_MS, TestConfig.NAVIGATION_TIMEOUT_MS);
        log.debug("═══════════════════════════════════════════════════");

        playwright = Playwright.create();
        BrowserType.LaunchOptions opts = new BrowserType.LaunchOptions()
                .setHeadless(TestConfig.HEADLESS)
                .setSlowMo(TestConfig.HEADLESS ? 0 : 50);

        browser = switch (TestConfig.BROWSER.toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(opts);
            case "webkit"  -> playwright.webkit().launch(opts);
            default        -> playwright.chromium().launch(opts);
        };
        log.info("✅ Browser launched: {} v{}",
                browser.browserType().name(), browser.version());
    }

    // ── Test lifecycle ─────────────────────────────────────────────────────────

    @BeforeMethod(alwaysRun = true)
    public void createContext(java.lang.reflect.Method method) {
        log.debug("─────────────────────────────────────────────────");
        log.debug("  TEST START ▶  {}#{}", method.getDeclaringClass().getSimpleName(), method.getName());
        log.debug("─────────────────────────────────────────────────");

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1440, 900)
                .setColorScheme(ColorScheme.DARK)
                .setIgnoreHTTPSErrors(true)
                .setLocale("en-IN")
                .setTimezoneId("Asia/Kolkata"));

        context.setDefaultTimeout(TestConfig.DEFAULT_TIMEOUT_MS);
        context.setDefaultNavigationTimeout(TestConfig.NAVIGATION_TIMEOUT_MS);
        page = context.newPage();

        // Verbose page-level event listeners
        page.onPageError(err -> log.error("  [PAGE ERROR] {}", err));
        page.onConsoleMessage(msg -> {
            switch (msg.type()) {
                case "error"   -> log.warn("  [CONSOLE ERROR]   {}", msg.text());
                case "warning" -> log.debug("  [CONSOLE WARN]    {}", msg.text());
                case "info"    -> log.debug("  [CONSOLE INFO]    {}", msg.text());
                default        -> log.trace("  [CONSOLE {}]  {}", msg.type().toUpperCase(), msg.text());
            }
        });
        page.onRequest(req  -> log.trace("  →  {} {}", req.method(), req.url()));
        page.onResponse(res -> {
            if (res.status() >= 400) {
                log.warn("  ← {} {}  ({})", res.status(), res.url(), res.statusText());
            } else {
                log.trace("  ← {} {}", res.status(), res.url());
            }
        });

        log.debug("  Browser context created. Viewport: 1440×900, TZ: Asia/Kolkata");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName   = result.getName();
        String statusText = switch (result.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED ✅";
            case ITestResult.FAILURE -> "FAILED ❌";
            case ITestResult.SKIP    -> "SKIPPED ⏭";
            default                  -> "UNKNOWN";
        };

        log.debug("  TEST END ■  {} — {}", testName, statusText);

        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("  Failure detail: {}", result.getThrowable() != null
                    ? result.getThrowable().getMessage() : "no message");
            AllureAttachmentHelper.attachScreenshot(page, "📸 Failure Screenshot");
            AllureAttachmentHelper.attachPageSource(page, "📄 Page Source at Failure");
            if (result.getThrowable() != null) {
                AllureAttachmentHelper.attachText("💥 Exception",
                        result.getThrowable().toString());
            }
        }

        if (result.getStatus() == ITestResult.SUCCESS) {
            // Capture a final success screenshot at reduced quality
            AllureAttachmentHelper.attachScreenshot(page, "📸 Final State (Pass)");
        }

        if (page    != null) page.close();
        if (context != null) context.close();
        log.debug("─────────────────────────────────────────────────");
    }

    @AfterSuite(alwaysRun = true)
    public void closeBrowser() {
        if (browser    != null) browser.close();
        if (playwright != null) playwright.close();
        log.info("Browser closed. Suite complete.");
    }

    // ── Shared helpers ─────────────────────────────────────────────────────────

    @Step("Navigate to {url}")
    protected void navigateTo(String url) {
        log.debug("  NAV → {}", url);
        Instant start = Instant.now();
        navigateWithRetry(url);
        long ms = Duration.between(start, Instant.now()).toMillis();
        String title = titleWithRetry();
        log.debug("  NAV ✓ loaded in {}ms — title: '{}'", ms, title);
        AllureAttachmentHelper.addPageInfo(url, title);
        Allure.parameter("Page Title", title);
        Allure.parameter("Load Time (ms)", ms);
    }

    /**
     * page.navigate() can throw "net::ERR_ABORTED" when a prior in-flight
     * navigation (e.g. the previous test's redirect still resolving) is
     * cancelled by the new request. Retry briefly instead of failing.
     */
    private void navigateWithRetry(String url) {
        PlaywrightException lastError = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                page.navigate(url);
                return;
            } catch (PlaywrightException e) {
                lastError = e;
                page.waitForTimeout(400);
            }
        }
        throw lastError;
    }

    /**
     * page.title() can throw "Execution context was destroyed" when a redirect
     * (bare domain → www, or the auth guard bouncing a protected page to /login/)
     * is still settling after the initial navigation resolves. Retry briefly
     * instead of letting that timing race fail the test.
     */
    private String titleWithRetry() {
        PlaywrightException lastError = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                page.waitForLoadState();
                return page.title();
            } catch (PlaywrightException e) {
                lastError = e;
                page.waitForTimeout(400);
            }
        }
        throw lastError;
    }

    /** True if the auth guard redirected the current page to the login screen. */
    protected boolean isOnLoginPage() {
        return page.url().contains("/login/");
    }

    /**
     * Pages such as commodities, indices, expert-view, news, and methodology are
     * gated by a temporary-unlock window (see data/page_access.json on the site).
     * The gate is enforced client-side: the static HTML always loads first, then
     * app.js's guardProtectedPageAccess() fires an async probe and, if locked,
     * sets window.location.href to /login/. That redirect can still be in
     * flight right after waitForLoadState() resolves, so give it a short window
     * to settle before deciding whether the page is actually locked. When that
     * window is closed and the guard does redirect, it's expected behavior, not
     * a product defect — skip the test rather than failing it.
     */
    protected void skipIfRedirectedToLogin(String featureName) {
        if (!isOnLoginPage()) {
            try {
                page.waitForURL("**/login/**", new Page.WaitForURLOptions().setTimeout(2000));
            } catch (PlaywrightException ignored) {
                // No redirect within the window — the page is genuinely unlocked.
            }
        }
        if (isOnLoginPage()) {
            throw new SkipException(featureName
                    + " is currently behind the login gate (page-access unlock window is closed) — not a failure.");
        }
    }

    @Step("Capture screenshot: {name}")
    protected void takeScreenshot(String name) {
        log.debug("  📸 Screenshot: {}", name);
        AllureAttachmentHelper.attachScreenshot(page, name);
    }

    /** Pause (only for debugging when headless=false). Not used in CI. */
    protected void debugPause(int ms) {
        if (!TestConfig.HEADLESS) {
            log.debug("  ⏸ Debug pause {}ms", ms);
            page.waitForTimeout(ms);
        }
    }
}
