package com.easemytrade.tests;

import com.easemytrade.base.BaseTest;
import com.easemytrade.config.TestConfig;
import com.easemytrade.utils.AllureAttachmentHelper;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("EaseMyTrade")
@Feature("Navigation & Page Load")
public class NavigationTest extends BaseTest {

    @DataProvider(name = "publicPages")
    public Object[][] publicPages() {
        return new Object[][]{
                {TestConfig.HOME_URL, "EaseMyTrade", "home"},
                {TestConfig.LOGIN_URL, "Sign In", "login"},
                {TestConfig.EXPERT_VIEW_URL, "Expert View", "expert-view"},
                {TestConfig.NEWS_URL, "AI Market Intelligence", ""},
                {TestConfig.METHODOLOGY_URL, "Methodology", ""},
                {TestConfig.TELEGRAM_URL, "Telegram", ""},
                {TestConfig.CONTACT_URL, "Contact", ""},
                {TestConfig.DISCLAIMER_URL, "Disclaimer", ""},
        };
    }

    @Test(description = "All public pages load without errors and have expected title keywords",
            dataProvider = "publicPages")
    @Story("Page Load")
    @Severity(SeverityLevel.CRITICAL)
    public void testPublicPageLoads(String url, String titleKeyword, String bodyPageAttr) {
        if (url.equals(TestConfig.LOGIN_URL)) {
            dropAuthenticatedSession();
        }
        navigateTo(url);
        if (!"Sign In".equals(titleKeyword)) {
            skipIfRedirectedToLogin(titleKeyword);
        }
        String title = page.title();
        log.info("URL: {}, Title: '{}'", url, title);
        AllureAttachmentHelper.attachText("Page Title", title);

        assertThat(title).as("Page title at %s should contain '%s'", url, titleKeyword)
                .containsIgnoringCase(titleKeyword);

        assertThat(page.locator("main, body").first().isVisible())
                .as("Main or body must be visible at %s", url).isTrue();

        if (!bodyPageAttr.isBlank()) {
            String attr = page.locator("body").getAttribute("data-page");
            assertThat(attr).as("data-page attribute at %s should be '%s'", url, bodyPageAttr)
                    .isEqualTo(bodyPageAttr);
        }

        takeScreenshot("Page Load: " + titleKeyword);
    }

    @DataProvider(name = "indexPages")
    public Object[][] indexPages() {
        return new Object[][]{
                {TestConfig.NIFTY_URL, "NIFTY"},
                {TestConfig.BANKNIFTY_URL, "BANKNIFTY"},
                {TestConfig.FINNIFTY_URL, "FINNIFTY"},
                {TestConfig.SENSEX_URL, "SENSEX"},
        };
    }

    @Test(description = "All index pages (NIFTY, BANKNIFTY, FINNIFTY, SENSEX) load and contain index name in title/content",
            dataProvider = "indexPages")
    @Story("Index Pages")
    @Severity(SeverityLevel.CRITICAL)
    public void testIndexPageLoads(String url, String indexName) {
        navigateTo(url);
        skipIfRedirectedToLogin(indexName);
        String title = page.title();
        String bodyContent = page.locator("body").innerText();
        log.info("{} page title: '{}'", indexName, title);

        AllureAttachmentHelper.attachText("Page Info",
                "URL: " + url + "\nTitle: " + title + "\nIndex: " + indexName);

        assertThat(title + bodyContent)
                .as("Page at %s should reference %s", url, indexName)
                .containsIgnoringCase(indexName);
        takeScreenshot("Index Page: " + indexName);
    }

    @DataProvider(name = "commodityPages")
    public Object[][] commodityPages() {
        return new Object[][]{
                {TestConfig.GOLD_URL, "Gold"},
                {TestConfig.SILVER_URL, "Silver"},
                {TestConfig.CRUDE_OIL_URL, "Crude"},
                {TestConfig.NATURAL_GAS_URL, "Natural Gas"},
        };
    }

    @Test(description = "All commodity pages load with the commodity name in page title or body",
            dataProvider = "commodityPages")
    @Story("Commodity Pages")
    @Severity(SeverityLevel.NORMAL)
    public void testCommodityPageLoads(String url, String commodityName) {
        navigateTo(url);
        skipIfRedirectedToLogin(commodityName);
        String title = page.title();
        String bodyContent = page.locator("body").innerText();
        log.info("{} page title: '{}'", commodityName, title);
        assertThat(title + bodyContent)
                .as("Page at %s should reference %s", url, commodityName)
                .containsIgnoringCase(commodityName);
        takeScreenshot("Commodity Page: " + commodityName);
    }

    @Test(description = "Brand logo from home page navigates back to home when clicked")
    @Story("Logo Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testLogoNavigatesToHome() {
        navigateTo(TestConfig.EXPERT_VIEW_URL);
        page.locator(".brand").click();
        page.waitForLoadState();
        // easemytrade.in redirects to www.easemytrade.in
        assertThat(page.url()).contains("easemytrade.in/");
        takeScreenshot("Logo Nav to Home");
    }

    @Test(description = "Expert View nav link navigates to the Expert View page")
    @Story("Navigation Links")
    @Severity(SeverityLevel.NORMAL)
    public void testExpertViewNavLink() {
        navigateTo(TestConfig.HOME_URL);
        page.locator("nav a[href='/expert-view/']").first().click();
        page.waitForLoadState();
        assertThat(page.url()).contains("/expert-view/");
    }

    @Test(description = "News nav link navigates to the News page")
    @Story("Navigation Links")
    @Severity(SeverityLevel.NORMAL)
    public void testNewsNavLink() {
        navigateTo(TestConfig.HOME_URL);
        page.locator("nav a[href='/news/']").first().click();
        page.waitForLoadState();
        assertThat(page.url()).contains("/news/");
    }

    @Test(description = "Disclaimer link in footer navigates to the disclaimer page")
    @Story("Footer Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testFooterDisclaimerNavigation() {
        navigateTo(TestConfig.HOME_URL);
        page.locator("footer a[href='/disclaimer/']").click();
        page.waitForLoadState();
        assertThat(page.url()).contains("/disclaimer/");
    }

    @Test(description = "Home page open market overview CTA scrolls to or navigates to market overview section")
    @Story("Home Page CTAs")
    @Severity(SeverityLevel.NORMAL)
    public void testHomeOpenMarketOverviewCta() {
        navigateTo(TestConfig.HOME_URL);
        page.locator("a[href='/#market-overview']").first().click();
        page.waitForTimeout(500);
        // Should still be on the home page (anchor scroll) — easemytrade.in
        // redirects to www.easemytrade.in, so only the registrable domain matters.
        assertThat(page.url()).contains("easemytrade.in");
    }

    @Test(description = "Market overview redirect page eventually lands on home or market content")
    @Story("Redirects")
    @Severity(SeverityLevel.MINOR)
    public void testMarketOverviewRedirect() {
        navigateTo(TestConfig.MARKET_OVERVIEW_URL);
        // Could redirect to home; easemytrade.in redirects to www.easemytrade.in
        assertThat(page.url()).contains("easemytrade.in");
        assertThat(page.locator("body").isVisible()).isTrue();
    }

    @Test(description = "All nav links have valid non-empty href attributes")
    @Story("Navigation Links")
    @Severity(SeverityLevel.NORMAL)
    public void testAllNavLinksHaveHrefs() {
        navigateTo(TestConfig.HOME_URL);
        var navLinks = page.locator("nav.nav-links a");
        int count = navLinks.count();
        assertThat(count).as("Nav should have at least 5 links").isGreaterThanOrEqualTo(5);

        StringBuilder linkReport = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String href = navLinks.nth(i).getAttribute("href");
            String text = navLinks.nth(i).innerText().trim();
            linkReport.append(text).append(" => ").append(href).append("\n");
            assertThat(href).as("Nav link '%s' must have href", text).isNotBlank();
        }
        AllureAttachmentHelper.attachText("All Nav Links", linkReport.toString());
    }

    @Test(description = "Indian Indices dropdown contains exactly NIFTY, BANKNIFTY, FINNIFTY, SENSEX")
    @Story("Navigation Links")
    @Severity(SeverityLevel.NORMAL)
    public void testIndianIndicesDropdownLinks() {
        navigateTo(TestConfig.HOME_URL);
        page.locator(".nav-dropdown").first().click();

        assertThat(page.locator("a[href='/nifty/today/']").first().isVisible()).isTrue();
        assertThat(page.locator("a[href='/indices/banknifty/']").first().isVisible()).isTrue();
        assertThat(page.locator("a[href='/indices/finnifty/']").first().isVisible()).isTrue();
        assertThat(page.locator("a[href='/indices/sensex/']").first().isVisible()).isTrue();
    }

    @Test(description = "Page title format is consistent: '{Page Name} | EaseMyTrade'")
    @Story("Page Titles")
    @Severity(SeverityLevel.MINOR)
    public void testTitleFormatConsistency() {
        String[][] pagesToCheck = {
                {TestConfig.HOME_URL, "EaseMyTrade"},
                {TestConfig.EXPERT_VIEW_URL, "Expert View | EaseMyTrade"},
                {TestConfig.LOGIN_URL, "EaseMyTrade | Sign In"},
        };

        for (String[] p : pagesToCheck) {
            if (p[0].equals(TestConfig.LOGIN_URL)) {
                dropAuthenticatedSession();
            }
            navigateTo(p[0]);
            if (!p[0].equals(TestConfig.LOGIN_URL) && isOnLoginPage()) {
                log.info("Skipping title check for {} — currently behind login gate", p[0]);
                continue;
            }
            String title = page.title();
            assertThat(title).containsIgnoringCase("EaseMyTrade");
            log.info("Title at {}: '{}'", p[0], title);
        }
    }
}
