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
        page.navigate(url);
        page.waitForLoadState();
        long ms = Duration.between(start, Instant.now()).toMillis();
        String title = page.title();
        log.debug("  NAV ✓ loaded in {}ms — title: '{}'", ms, title);
        AllureAttachmentHelper.addPageInfo(url, title);
        Allure.parameter("Page Title", title);
        Allure.parameter("Load Time (ms)", ms);
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
