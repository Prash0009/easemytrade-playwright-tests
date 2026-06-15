package com.easemytrade.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ExpertViewPage extends BasePage {

    public ExpertViewPage(Page page) { super(page); }

    // Hero section
    public Locator heroSection() { return page.locator(".expert-view-hero-shell"); }
    public Locator heroTitle() { return page.locator(".expert-view-hero-shell h1"); }
    public Locator heroLead() { return page.locator(".expert-view-hero-shell .lead").first(); }

    // Search form
    public Locator searchForm() { return page.locator("#expertViewForm"); }
    public Locator searchInput() { return page.locator("#expertViewInput"); }
    public Locator generateReportBtn() { return page.locator("button.primary-action.expert-view-search__submit"); }
    public Locator bestPickBtn() { return page.locator("#expertViewBestButton"); }
    public Locator bestPickPrevBtn() { return page.locator("#expertViewBestPrevButton"); }
    public Locator bestPickNextBtn() { return page.locator("#expertViewBestNextButton"); }

    // Universe dropdown
    public Locator universeDropdown() { return page.locator("#expertViewUniverseDropdown"); }
    public Locator universeSummaryText() { return page.locator("#expertViewUniverseSummaryText"); }
    public Locator universeMenu() { return page.locator("#expertViewUniverseMenu"); }
    public Locator universeOption(String value) {
        return page.locator("[data-expert-universe-option='" + value + "']");
    }

    // Status message
    public Locator statusMessage() { return page.locator("#expertViewStatus"); }
    public String getStatusText() { return textOf("expertViewStatus"); }

    // Hero metrics (pre-scan)
    public String getConfidenceHero() { return textOf("expertHeroConfidence"); }
    public String getTrendHero() { return textOf("expertHeroTrend"); }
    public String getStyleHero() { return textOf("expertHeroStyle"); }
    public String getSentimentHero() { return textOf("expertHeroSentiment"); }

    // Dashboard cards (appear after scan)
    public Locator dashboardBand() { return page.locator("#expertViewDashboardBand"); }
    public String getConfidenceMeter() { return textOf("expertHeroConfidenceMeter"); }
    public String getBias() { return textOf("expertHeroBias"); }
    public String getRecommendation() { return textOf("expertHeroRecommendation"); }
    public String getShortScope() { return textOf("expertHeroShortScope"); }
    public Locator confidenceBar() { return page.locator("#expertHeroConfidenceBar"); }

    // Report area
    public Locator reportArea() { return page.locator("#expertViewReport"); }
    public Locator emptyReportCard() { return page.locator(".expert-view-empty"); }

    // Analysis cards (rendered by JS after scan)
    public Locator analysisCards() { return page.locator(".analysis-card"); }
    public Locator firstAnalysisCard() { return page.locator(".analysis-card").first(); }

    // AI Systems section
    public Locator systemsShell() { return page.locator("#expertViewSystemsShell"); }
    public Locator robotCards() { return page.locator(".expert-view-robot-card"); }

    // Example chips
    public Locator exampleChips() { return page.locator(".expert-view-chip"); }
    public Locator chipByName(String name) {
        return page.locator("[data-expert-example='" + name + "']");
    }

    // Actions
    public void typeStock(String stock) {
        searchInput().clear();
        searchInput().fill(stock);
    }

    public void submitSearch() {
        generateReportBtn().click();
    }

    public void searchForStock(String stock) {
        typeStock(stock);
        submitSearch();
    }

    public void openUniverseDropdown() {
        universeDropdown().click();
    }

    public void selectUniverse(String value) {
        openUniverseDropdown();
        universeOption(value).click();
    }

    public void waitForReportToLoad() {
        // Wait for the empty card to disappear or for analysis cards to appear
        page.waitForSelector(".analysis-card:not(.expert-view-empty)",
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(60_000));
    }

    public boolean isReportLoaded() {
        try {
            return analysisCards().count() > 0
                    && !emptyReportCard().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public int getAnalysisCardCount() {
        try { return analysisCards().count(); }
        catch (Exception e) { return 0; }
    }

    public boolean isDashboardVisible() {
        return !dashboardBand().getAttribute("class").contains("is-hidden");
    }

    public boolean areSystemsVisible() {
        return !systemsShell().getAttribute("class").contains("is-hidden");
    }
}
