package com.easemytrade.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage {

    protected final Page page;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public BasePage(Page page) {
        this.page = page;
        log.debug("  [PAGE] {} initialised", getClass().getSimpleName());
    }

    // ── Header / Navigation ────────────────────────────────────────────────────

    public Locator headerLogo()               { return page.locator(".brand"); }
    public Locator headerNav()                { return page.locator("nav.nav-links"); }
    public Locator marketOverviewNavLink()    { return page.locator("nav a[href='/#market-overview']"); }
    public Locator expertViewNavLink()        { return page.locator("nav a[href='/expert-view/']"); }
    public Locator newsNavLink()              { return page.locator("nav a[href='/news/']"); }
    public Locator methodologyNavLink()       { return page.locator("nav a[href='/methodology/']"); }
    public Locator telegramNavLink()          { return page.locator("nav a[href='/telegram/']"); }
    public Locator contactNavLink()           { return page.locator("nav a[href='/contact/']"); }
    public Locator indianIndicesDropdown()    { return page.locator("nav .nav-dropdown").first(); }
    public Locator liveClockElement()         { return page.locator("#liveClock"); }

    // ── Footer ─────────────────────────────────────────────────────────────────

    public Locator footerDisclaimerLink()     { return page.locator("footer a[href='/disclaimer/']"); }
    public Locator footer()                   { return page.locator("footer.site-footer"); }

    // ── Market Ticker ──────────────────────────────────────────────────────────

    public Locator marketTicker()             { return page.locator(".market-ticker"); }

    // ── Shared actions (with @Step so Allure shows them as named steps) ────────

    @Step("Open 'Indian Indices' dropdown in navigation")
    public void openIndianIndicesDropdown() {
        log.debug("  [ACTION] Opening Indian Indices nav dropdown");
        indianIndicesDropdown().click();
        log.debug("  [ACTION] Dropdown opened");
    }

    @Step("Click brand logo to navigate home")
    public void clickBrandLogo() {
        log.debug("  [ACTION] Clicking brand logo");
        headerLogo().click();
    }

    @Step("Read page title")
    public String getTitle() {
        String title = page.title();
        log.debug("  [STATE] Page title = '{}'", title);
        return title;
    }

    public String getUrl() {
        String url = page.url();
        log.debug("  [STATE] Current URL = '{}'", url);
        return url;
    }

    // ── Utility helpers ────────────────────────────────────────────────────────

    @Step("Wait for element to be visible")
    public void waitForVisible(Locator locator) {
        log.debug("  [WAIT] Waiting for element to be visible");
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        log.debug("  [WAIT] Element is now visible");
    }

    public boolean isVisible(Locator locator) {
        try {
            boolean v = locator.isVisible();
            log.trace("  [CHECK] isVisible = {}", v);
            return v;
        } catch (Exception e) {
            log.debug("  [CHECK] isVisible threw (treated as false): {}", e.getMessage());
            return false;
        }
    }

    public String textOf(String id) {
        try {
            String text = page.locator("#" + id).innerText().trim();
            log.debug("  [READ] #{} = '{}'", id, text);
            return text;
        } catch (Exception e) {
            log.debug("  [READ] #{} threw: {}", id, e.getMessage());
            return "";
        }
    }

    public String attrOf(String selector, String attr) {
        try {
            String value = page.locator(selector).getAttribute(attr);
            log.debug("  [READ] {}[{}] = '{}'", selector, attr, value);
            return value != null ? value : "";
        } catch (Exception e) {
            log.debug("  [READ] {}[{}] threw: {}", selector, attr, e.getMessage());
            return "";
        }
    }
}
