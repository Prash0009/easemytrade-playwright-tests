package com.easemytrade.base;

import com.easemytrade.config.TestConfig;
import com.easemytrade.utils.AllureAttachmentHelper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.ColorScheme;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.nio.file.Paths;

public class BaseTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeSuite(alwaysRun = true)
    public void launchBrowser() {
        log.info("Launching browser: {} | headless: {}", TestConfig.BROWSER, TestConfig.HEADLESS);
        playwright = Playwright.create();

        BrowserType.LaunchOptions opts = new BrowserType.LaunchOptions()
                .setHeadless(TestConfig.HEADLESS)
                .setSlowMo(TestConfig.HEADLESS ? 0 : 50);

        browser = switch (TestConfig.BROWSER.toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(opts);
            case "webkit" -> playwright.webkit().launch(opts);
            default -> playwright.chromium().launch(opts);
        };
        log.info("Browser launched: {}", browser.browserType().name());
    }

    @BeforeMethod(alwaysRun = true)
    public void createContext(java.lang.reflect.Method method) {
        log.info("Starting test: {}", method.getName());
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1440, 900)
                .setColorScheme(ColorScheme.DARK)
                .setIgnoreHTTPSErrors(true)
                .setLocale("en-IN")
                .setTimezoneId("Asia/Kolkata"));

        context.setDefaultTimeout(TestConfig.DEFAULT_TIMEOUT_MS);
        context.setDefaultNavigationTimeout(TestConfig.NAVIGATION_TIMEOUT_MS);
        page = context.newPage();

        page.onPageError(e -> log.error("Page error: {}", e));
        page.onConsoleMessage(msg -> {
            if ("error".equals(msg.type())) {
                log.warn("Console error: {}", msg.text());
            }
        });
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Test FAILED: {}", result.getName());
            AllureAttachmentHelper.attachScreenshot(page, "Failure Screenshot");
            AllureAttachmentHelper.attachPageSource(page, "Page Source on Failure");
        }
        if (page != null) page.close();
        if (context != null) context.close();
    }

    @AfterSuite(alwaysRun = true)
    public void closeBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
        log.info("Browser closed.");
    }

    protected void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        page.navigate(url);
        page.waitForLoadState();
        AllureAttachmentHelper.addPageInfo(url, page.title());
    }

    protected void takeScreenshot(String name) {
        AllureAttachmentHelper.attachScreenshot(page, name);
    }
}
