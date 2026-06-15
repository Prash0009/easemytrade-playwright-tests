package com.easemytrade.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;

public class ExpertViewPage extends BasePage {

    public ExpertViewPage(Page page) { super(page); }

    // ── Locators ──────────────────────────────────────────────────────────────

    public Locator heroSection()        { return page.locator(".expert-view-hero-shell"); }
    public Locator heroTitle()          { return page.locator(".expert-view-hero-shell h1"); }
    public Locator heroLead()           { return page.locator(".expert-view-hero-shell .lead").first(); }
    public Locator searchForm()         { return page.locator("#expertViewForm"); }
    public Locator searchInput()        { return page.locator("#expertViewInput"); }
    public Locator generateReportBtn()  { return page.locator("button.primary-action.expert-view-search__submit"); }
    public Locator bestPickBtn()        { return page.locator("#expertViewBestButton"); }
    public Locator bestPickPrevBtn()    { return page.locator("#expertViewBestPrevButton"); }
    public Locator bestPickNextBtn()    { return page.locator("#expertViewBestNextButton"); }
    public Locator universeDropdown()   { return page.locator("#expertViewUniverseDropdown"); }
    public Locator universeSummaryText(){ return page.locator("#expertViewUniverseSummaryText"); }
    public Locator universeMenu()       { return page.locator("#expertViewUniverseMenu"); }
    public Locator statusMessage()      { return page.locator("#expertViewStatus"); }
    public Locator dashboardBand()      { return page.locator("#expertViewDashboardBand"); }
    public Locator confidenceBar()      { return page.locator("#expertHeroConfidenceBar"); }
    public Locator reportArea()         { return page.locator("#expertViewReport"); }
    public Locator emptyReportCard()    { return page.locator(".expert-view-empty"); }
    public Locator analysisCards()      { return page.locator(".analysis-card"); }
    public Locator systemsShell()       { return page.locator("#expertViewSystemsShell"); }
    public Locator robotCards()         { return page.locator(".expert-view-robot-card"); }
    public Locator exampleChips()       { return page.locator(".expert-view-chip"); }

    public Locator universeOption(String value) {
        return page.locator("[data-expert-universe-option='" + value + "']");
    }
    public Locator chipByName(String name) {
        return page.locator("[data-expert-example='" + name + "']");
    }

    // ── State readers ─────────────────────────────────────────────────────────

    public String getStatusText()       { return textOf("expertViewStatus"); }
    public String getConfidenceHero()   { return textOf("expertHeroConfidence"); }
    public String getTrendHero()        { return textOf("expertHeroTrend"); }
    public String getStyleHero()        { return textOf("expertHeroStyle"); }
    public String getSentimentHero()    { return textOf("expertHeroSentiment"); }
    public String getConfidenceMeter()  { return textOf("expertHeroConfidenceMeter"); }
    public String getBias()             { return textOf("expertHeroBias"); }
    public String getRecommendation()   { return textOf("expertHeroRecommendation"); }
    public String getShortScope()       { return textOf("expertHeroShortScope"); }

    // ── Actions ───────────────────────────────────────────────────────────────

    @Step("Clear and type stock name: {stock}")
    public void typeStock(String stock) {
        log.debug("  [ACTION] typeStock('{}')", stock);
        searchInput().clear();
        searchInput().fill(stock);
        log.debug("  [ACTION] Stock name entered");
    }

    @Step("Click 'Generate Report' button")
    public void submitSearch() {
        log.debug("  [ACTION] submitSearch — clicking Generate Report");
        generateReportBtn().click();
    }

    @Step("Search for stock: {stock}")
    public void searchForStock(String stock) {
        log.debug("  [FLOW] searchForStock('{}')", stock);
        typeStock(stock);
        submitSearch();
    }

    @Step("Open universe dropdown")
    public void openUniverseDropdown() {
        log.debug("  [ACTION] Opening universe dropdown");
        universeDropdown().click();
    }

    @Step("Select universe: {value}")
    public void selectUniverse(String value) {
        log.debug("  [ACTION] selectUniverse('{}')", value);
        openUniverseDropdown();
        universeOption(value).click();
        log.debug("  [ACTION] Universe '{}' selected", value);
    }

    @Step("Click 'Best Pick' button")
    public void clickBestPick() {
        log.debug("  [ACTION] Clicking Best Pick");
        bestPickBtn().click();
    }

    @Step("Click 'Next Best Pick' button")
    public void clickNextBestPick() {
        log.debug("  [ACTION] Clicking Best Pick → Next");
        bestPickNextBtn().click();
    }

    @Step("Click 'Previous Best Pick' button")
    public void clickPrevBestPick() {
        log.debug("  [ACTION] Clicking Best Pick ← Prev");
        bestPickPrevBtn().click();
    }

    // ── Wait conditions ───────────────────────────────────────────────────────

    @Step("Wait for Expert View report to load (up to 60s)")
    public void waitForReportToLoad() {
        log.debug("  [WAIT] Waiting for analysis card to appear...");
        page.waitForSelector(".analysis-card:not(.expert-view-empty)",
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(60_000));
        log.debug("  [WAIT] Report loaded — {} analysis cards present", getAnalysisCardCount());
    }

    // ── Compound state ────────────────────────────────────────────────────────

    public boolean isReportLoaded() {
        try {
            boolean loaded = analysisCards().count() > 0 && !emptyReportCard().isVisible();
            log.debug("  [STATE] isReportLoaded = {}", loaded);
            return loaded;
        } catch (Exception e) { return false; }
    }

    public int getAnalysisCardCount() {
        try {
            int n = analysisCards().count();
            log.debug("  [STATE] analysisCardCount = {}", n);
            return n;
        } catch (Exception e) { return 0; }
    }

    public boolean isDashboardVisible() {
        boolean vis = !dashboardBand().getAttribute("class").contains("is-hidden");
        log.debug("  [STATE] isDashboardVisible = {}", vis);
        return vis;
    }

    public boolean areSystemsVisible() {
        boolean vis = !systemsShell().getAttribute("class").contains("is-hidden");
        log.debug("  [STATE] areSystemsVisible = {}", vis);
        return vis;
    }
}
