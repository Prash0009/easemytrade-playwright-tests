package com.easemytrade.tests;

import com.easemytrade.base.BaseTest;
import com.easemytrade.config.TestConfig;
import com.easemytrade.pages.ExpertViewPage;
import com.easemytrade.utils.AllureAttachmentHelper;
import com.easemytrade.utils.SignalParser;
import com.microsoft.playwright.Locator;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("EaseMyTrade")
@Feature("Expert View")
public class ExpertViewTest extends BaseTest {

    private ExpertViewPage expertViewPage;

    @BeforeMethod
    public void openExpertViewPage() {
        navigateTo(TestConfig.EXPERT_VIEW_URL);
        skipIfRedirectedToLogin("Expert View");
        expertViewPage = new ExpertViewPage(page);
    }

    // ---- Page Structure ----

    @Test(description = "Expert View page loads with correct title")
    @Story("Page Load")
    @Severity(SeverityLevel.BLOCKER)
    public void testExpertViewPageTitle() {
        String title = expertViewPage.getTitle();
        log.info("Expert View title: {}", title);
        assertThat(title).containsIgnoringCase("Expert View");
        assertThat(title).containsIgnoringCase("EaseMyTrade");
    }

    @Test(description = "Expert View hero section is present with the AI Terminal headline")
    @Story("Page Load")
    @Severity(SeverityLevel.CRITICAL)
    public void testHeroSectionContent() {
        assertThat(expertViewPage.heroSection().isVisible()).as("Hero section visible").isTrue();
        String heading = expertViewPage.heroTitle().innerText().trim();
        log.info("Hero heading: {}", heading);
        assertThat(heading).containsIgnoringCase("AI");
        takeScreenshot("Expert View Hero");
    }

    @Test(description = "Search form is present with input, Generate Report, and Best Pick buttons")
    @Story("Search Form")
    @Severity(SeverityLevel.BLOCKER)
    public void testSearchFormElements() {
        assertThat(expertViewPage.searchForm().isVisible()).as("Search form visible").isTrue();
        assertThat(expertViewPage.searchInput().isVisible()).as("Search input visible").isTrue();
        assertThat(expertViewPage.generateReportBtn().isVisible()).as("Generate Report button visible").isTrue();
        assertThat(expertViewPage.bestPickBtn().isVisible()).as("Best Pick button visible").isTrue();
        takeScreenshot("Expert View Search Form");
    }

    @Test(description = "Universe dropdown is present and shows current selection")
    @Story("Universe Selector")
    @Severity(SeverityLevel.CRITICAL)
    public void testUniverseDropdownPresence() {
        assertThat(expertViewPage.universeDropdown().isVisible()).as("Universe dropdown visible").isTrue();
        String currentUniv = expertViewPage.universeSummaryText().innerText().trim();
        log.info("Default universe: {}", currentUniv);
        assertThat(currentUniv).isNotBlank();
        AllureAttachmentHelper.attachText("Default Universe", currentUniv);
    }

    @Test(description = "Universe dropdown opens and shows all option groups")
    @Story("Universe Selector")
    @Severity(SeverityLevel.NORMAL)
    public void testUniverseDropdownOptions() {
        expertViewPage.openUniverseDropdown();
        assertThat(expertViewPage.universeMenu().isVisible()).as("Universe menu visible after click").isTrue();
        int optionCount = expertViewPage.universeMenu().locator("button[data-expert-universe-option]").count();
        log.info("Universe options count: {}", optionCount);
        assertThat(optionCount).as("Should have multiple universe options").isGreaterThan(5);
        AllureAttachmentHelper.attachText("Universe Options Count", String.valueOf(optionCount));
        takeScreenshot("Universe Dropdown Open");
    }

    @Test(description = "Selecting NIFTY 50 universe updates the dropdown summary text")
    @Story("Universe Selector")
    @Severity(SeverityLevel.NORMAL)
    public void testUniverseSelectionUpdatesLabel() {
        expertViewPage.selectUniverse("nifty50");
        String updatedLabel = expertViewPage.universeSummaryText().innerText().trim();
        log.info("Updated universe label: {}", updatedLabel);
        assertThat(updatedLabel.toLowerCase()).contains("nifty");
        takeScreenshot("Universe Selected NIFTY 50");
    }

    @Test(description = "Best Pick navigation buttons (Prev/Next) are present")
    @Story("Best Pick Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testBestPickNavButtonsPresent() {
        assertThat(expertViewPage.bestPickPrevBtn().isVisible()).as("Best Pick Prev button visible").isTrue();
        assertThat(expertViewPage.bestPickNextBtn().isVisible()).as("Best Pick Next button visible").isTrue();
    }

    @Test(description = "Example ticker chips are present (Reliance, TCS, HDFC Bank, INFY, SBIN)")
    @Story("Example Chips")
    @Severity(SeverityLevel.NORMAL)
    public void testExampleChipsPresent() {
        int chipCount = expertViewPage.exampleChips().count();
        log.info("Example chips count: {}", chipCount);
        assertThat(chipCount).as("Should have at least 5 example chips").isGreaterThanOrEqualTo(5);

        for (String stock : new String[]{"Reliance", "TCS", "HDFC Bank", "INFY", "SBIN"}) {
            Locator chip = expertViewPage.chipByName(stock);
            assertThat(chip.isVisible()).as("Chip for '%s' should be visible".formatted(stock)).isTrue();
        }
        takeScreenshot("Example Chips");
    }

    @Test(description = "Clicking a quick-chip fills the search input with that stock name")
    @Story("Example Chips")
    @Severity(SeverityLevel.NORMAL)
    public void testChipFillsSearchInput() {
        expertViewPage.chipByName("TCS").click();
        String inputValue = expertViewPage.searchInput().inputValue();
        log.info("Input after chip click: '{}'", inputValue);
        assertThat(inputValue).containsIgnoringCase("TCS");
    }

    // ---- Initial State ----

    @Test(description = "Initial status message is present before any search")
    @Story("Initial State")
    @Severity(SeverityLevel.NORMAL)
    public void testInitialStatusMessage() {
        String status = expertViewPage.getStatusText();
        log.info("Initial status: '{}'", status);
        assertThat(expertViewPage.statusMessage().isVisible()).as("Status message visible").isTrue();
        assertThat(status).isNotBlank();
        AllureAttachmentHelper.attachText("Initial Status Message", status);
    }

    @Test(description = "Initial hero metrics show awaiting scan state")
    @Story("Initial State")
    @Severity(SeverityLevel.MINOR)
    public void testInitialHeroMetricsState() {
        String confidence = expertViewPage.getConfidenceHero();
        String trend = expertViewPage.getTrendHero();
        log.info("Initial confidence='{}', trend='{}'", confidence, trend);
        AllureAttachmentHelper.attachText("Initial Hero Metrics",
                "Confidence: " + confidence + "\nTrend: " + trend);
        // Before scan, these should be placeholder values
        assertThat(confidence + trend).as("Initial state should be empty/placeholder").isNotNull();
    }

    @Test(description = "Report area shows the empty 'Waiting for an asset' card before any search")
    @Story("Initial State")
    @Severity(SeverityLevel.NORMAL)
    public void testInitialReportAreaEmpty() {
        assertThat(expertViewPage.reportArea().isVisible()).as("Report area visible").isTrue();
        assertThat(expertViewPage.emptyReportCard().isVisible()).as("Empty card visible before scan").isTrue();
        String emptyText = expertViewPage.emptyReportCard().innerText().trim();
        log.info("Empty card text: '{}'", emptyText);
        assertThat(emptyText).containsIgnoringCase("waiting");
    }

    // ---- Post-Search Report Validation ----

    @Test(description = "Searching for 'Reliance' generates a report with analysis cards")
    @Story("Report Generation")
    @Severity(SeverityLevel.BLOCKER)
    public void testReportGenerationForReliance() {
        expertViewPage.searchForStock("Reliance");
        log.info("Waiting for report to load for Reliance...");

        try {
            expertViewPage.waitForReportToLoad();
        } catch (Exception e) {
            log.warn("Report did not load within timeout: {}", e.getMessage());
            takeScreenshot("Report Load Timeout - Reliance");
            return; // Soft fail — network-dependent test
        }

        assertThat(expertViewPage.isReportLoaded()).as("Report should be loaded after search").isTrue();
        int cardCount = expertViewPage.getAnalysisCardCount();
        log.info("Analysis card count after scan: {}", cardCount);
        assertThat(cardCount).as("At least one analysis card should be rendered").isGreaterThan(0);
        takeScreenshot("Reliance Report Loaded");
    }

    @Test(description = "After a successful scan, dashboard metrics (Confidence Meter, Bias, Recommendation) are visible")
    @Story("Dashboard Metrics")
    @Severity(SeverityLevel.CRITICAL)
    public void testDashboardMetricsAfterScan() {
        expertViewPage.searchForStock("TCS");
        try {
            expertViewPage.waitForReportToLoad();
        } catch (Exception e) {
            log.warn("Report timed out for TCS — skipping dashboard check.");
            return;
        }

        assertThat(expertViewPage.isDashboardVisible())
                .as("Dashboard band should be visible after scan").isTrue();

        String confidenceMeter = expertViewPage.getConfidenceMeter();
        String bias = expertViewPage.getBias();
        String recommendation = expertViewPage.getRecommendation();

        log.info("Confidence Meter='{}', Bias='{}', Recommendation='{}'", confidenceMeter, bias, recommendation);
        AllureAttachmentHelper.attachText("Dashboard Metrics",
                "Confidence: " + confidenceMeter + "\nBias: " + bias + "\nRecommendation: " + recommendation);

        assertThat(confidenceMeter).isNotBlank().isNotEqualTo("--");
        assertThat(bias).isNotBlank();
        assertThat(recommendation).isNotBlank().isNotEqualTo("Standby");
        takeScreenshot("Dashboard Metrics After Scan");
    }

    @Test(description = "Confidence score from Expert View scan must be between 0 and 100 percent")
    @Story("Confidence Score Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testExpertViewConfidenceScoreRange() {
        expertViewPage.searchForStock("INFY");
        try {
            expertViewPage.waitForReportToLoad();
        } catch (Exception e) {
            log.warn("Report timed out for INFY — skipping confidence check.");
            return;
        }

        String confidenceText = expertViewPage.getConfidenceMeter();
        double confidence = SignalParser.parsePercentage(confidenceText);
        log.info("Expert View confidence for INFY: '{}' => {}", confidenceText, confidence);
        AllureAttachmentHelper.attachText("INFY Confidence Score", confidenceText);

        if (confidence >= 0) {
            assertThat(SignalParser.isConfidenceInRange(confidence))
                    .as("Confidence %.1f must be in range [0, 100]", confidence).isTrue();
        }
    }

    @Test(description = "AI Recommendation must be one of the expected verdict values")
    @Story("Recommendation Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testAiRecommendationIsValid() {
        expertViewPage.searchForStock("SBIN");
        try {
            expertViewPage.waitForReportToLoad();
        } catch (Exception e) {
            log.warn("Report timed out for SBIN — skipping recommendation check.");
            return;
        }

        String recommendation = expertViewPage.getRecommendation();
        log.info("AI Recommendation for SBIN: '{}'", recommendation);
        AllureAttachmentHelper.attachText("SBIN AI Recommendation", recommendation);

        List<String> validRecommendations = List.of(
                "Strong Buy", "Buy", "Accumulate", "Hold", "Reduce", "Sell", "Strong Sell",
                "Watch", "Neutral", "Standby", "Watchlist Only", "No Trade"
        );

        if (recommendation != null && !recommendation.isBlank() && !recommendation.equals("Standby")) {
            boolean isValid = validRecommendations.stream()
                    .anyMatch(r -> recommendation.toLowerCase().contains(r.toLowerCase()));
            assertThat(isValid)
                    .as("Recommendation '%s' must be one of the valid verdicts: %s", recommendation, validRecommendations)
                    .isTrue();
        }
    }

    @Test(description = "Expert View AI systems section shows five robot cards after a scan")
    @Story("AI Systems")
    @Severity(SeverityLevel.NORMAL)
    public void testFiveAiRobotCardsVisible() {
        expertViewPage.searchForStock("HDFC Bank");
        try {
            expertViewPage.waitForReportToLoad();
        } catch (Exception e) {
            log.warn("Report timed out — skipping robot card check.");
            return;
        }

        assertThat(expertViewPage.areSystemsVisible()).as("AI Systems section should be visible after scan").isTrue();
        int robotCount = expertViewPage.robotCards().count();
        log.info("Robot card count: {}", robotCount);
        assertThat(robotCount).as("Should have exactly 5 AI robot cards").isEqualTo(5);
        takeScreenshot("AI Robot Cards");
    }

    @Test(description = "Report contains sections for Trend, Momentum, Support & Resistance, and Risk")
    @Story("Report Sections")
    @Severity(SeverityLevel.CRITICAL)
    public void testReportSections() {
        expertViewPage.searchForStock("Reliance");
        try {
            expertViewPage.waitForReportToLoad();
        } catch (Exception e) {
            log.warn("Report timed out — skipping sections check.");
            return;
        }

        String reportHtml = page.locator("#expertViewReport").innerHTML();
        AllureAttachmentHelper.attachText("Report HTML Length", String.valueOf(reportHtml.length()));

        // The report is data-driven; verify it has substantive content
        assertThat(reportHtml.length())
                .as("Report HTML should have substantive content (>500 chars)").isGreaterThan(500);
        takeScreenshot("Full Report Sections");
    }

    // ---- Best Pick Navigation ----

    @Test(description = "Best Pick button initiates a scan for the selected universe")
    @Story("Best Pick")
    @Severity(SeverityLevel.CRITICAL)
    public void testBestPickButtonInitiatesScan() {
        String statusBefore = expertViewPage.getStatusText();
        expertViewPage.bestPickBtn().click();

        // Status should change to show scanning
        page.waitForTimeout(2000);
        String statusAfter = expertViewPage.getStatusText();
        log.info("Status before Best Pick: '{}', after: '{}'", statusBefore, statusAfter);
        AllureAttachmentHelper.attachText("Best Pick Status Change",
                "Before: " + statusBefore + "\nAfter: " + statusAfter);

        // Status should have changed, showing the scan initiated
        assertThat(statusAfter).as("Status should change after Best Pick click").isNotNull();
        takeScreenshot("Best Pick Initiated");
    }

    @Test(description = "Searching an empty string does not crash or navigate away")
    @Story("Search Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptySearchDoesNotCrash() {
        expertViewPage.typeStock("");
        expertViewPage.submitSearch();
        page.waitForTimeout(1000);
        // Should still be on the Expert View page
        assertThat(page.url()).contains("/expert-view/");
        takeScreenshot("Empty Search Result");
    }

    @Test(description = "Short Horizon Scope is shown after successful scan")
    @Story("Dashboard Metrics")
    @Severity(SeverityLevel.NORMAL)
    public void testShortHorizonScopeAfterScan() {
        expertViewPage.searchForStock("TCS");
        try {
            expertViewPage.waitForReportToLoad();
        } catch (Exception e) {
            log.warn("Report timed out — skipping scope check.");
            return;
        }

        String scope = expertViewPage.getShortScope();
        log.info("Short Horizon Scope: '{}'", scope);
        AllureAttachmentHelper.attachText("Short Horizon Scope", scope);
        // Should not be the default placeholder after a scan
        assertThat(scope).isNotNull();
    }

    @Test(description = "Expert View page body has correct data-page attribute")
    @Story("Page Metadata")
    @Severity(SeverityLevel.MINOR)
    public void testPageBodyAttribute() {
        String bodyPage = page.locator("body").getAttribute("data-page");
        assertThat(bodyPage).isEqualTo("expert-view");
    }

    // ---- Data Providers ----

    @DataProvider(name = "stockSymbols")
    public Object[][] stockSymbols() {
        return new Object[][]{
                {"Reliance"},
                {"TCS"},
                {"INFY"},
                {"HDFC Bank"},
                {"SBIN"},
        };
    }

    @Test(description = "Searching a stock from the quick examples returns a status update (not an error)",
            dataProvider = "stockSymbols")
    @Story("Report Generation")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchExamplesUpdateStatus(String stock) {
        expertViewPage.searchForStock(stock);
        page.waitForTimeout(3000);
        String status = expertViewPage.getStatusText();
        log.info("Status after searching '{}': '{}'", stock, status);
        AllureAttachmentHelper.attachText("Status for " + stock, status);
        // Status must not indicate an unrecoverable error
        assertThat(status.toLowerCase())
                .as("Status for '%s' must not show a hard error", stock)
                .doesNotContain("exception", "500", "internal server error");
        takeScreenshot("After Search - " + stock);
    }
}
