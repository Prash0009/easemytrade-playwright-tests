package com.easemytrade.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage {

    protected final Page page;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public BasePage(Page page) {
        this.page = page;
    }

    // ---- Header / Navigation ----

    public Locator headerLogo() { return page.locator(".brand"); }
    public Locator headerNav() { return page.locator("nav.nav-links"); }
    public Locator marketOverviewNavLink() { return page.locator("nav a[href='/#market-overview']"); }
    public Locator expertViewNavLink() { return page.locator("nav a[href='/expert-view/']"); }
    public Locator newsNavLink() { return page.locator("nav a[href='/news/']"); }
    public Locator methodologyNavLink() { return page.locator("nav a[href='/methodology/']"); }
    public Locator telegramNavLink() { return page.locator("nav a[href='/telegram/']"); }
    public Locator contactNavLink() { return page.locator("nav a[href='/contact/']"); }
    public Locator indianIndicesDropdown() { return page.locator("nav .nav-dropdown").first(); }
    public Locator liveClockElement() { return page.locator("#liveClock"); }

    // ---- Footer ----

    public Locator footerDisclaimerLink() { return page.locator("footer a[href='/disclaimer/']"); }
    public Locator footer() { return page.locator("footer.site-footer"); }

    // ---- Market Ticker ----

    public Locator marketTicker() { return page.locator(".market-ticker"); }

    // ---- Utilities ----

    public String getTitle() { return page.title(); }
    public String getUrl() { return page.url(); }

    public void waitForVisible(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public boolean isVisible(Locator locator) {
        try { return locator.isVisible(); }
        catch (Exception e) { return false; }
    }

    public String textOf(String id) {
        try { return page.locator("#" + id).innerText().trim(); }
        catch (Exception e) { return ""; }
    }

    public String attrOf(String selector, String attr) {
        try { return page.locator(selector).getAttribute(attr); }
        catch (Exception e) { return ""; }
    }
}
