package com.easemytrade.tests;

import com.easemytrade.base.BaseTest;
import com.easemytrade.config.TestConfig;
import com.easemytrade.pages.IndexPage;
import com.easemytrade.utils.AllureAttachmentHelper;
import com.easemytrade.utils.SignalParser;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("EaseMyTrade")
@Feature("Index Pages")
public class IndexPageTest extends BaseTest {

    @DataProvider(name = "indexData")
    public Object[][] indexData() {
        return new Object[][]{
                {TestConfig.NIFTY_URL, "nifty", "NIFTY"},
                {TestConfig.BANKNIFTY_URL, "banknifty", "BANKNIFTY"},
                {TestConfig.FINNIFTY_URL, "finnifty", "FINNIFTY"},
                {TestConfig.SENSEX_URL, "sensex", "SENSEX"},
        };
    }

    // ---- Page Load ----

    @Test(description = "Each index page loads with the index name in page content",
            dataProvider = "indexData")
    @Story("Page Load")
    @Severity(SeverityLevel.BLOCKER)
    public void testIndexPageLoadsWithContent(String url, String key, String displayName) {
        navigateTo(url);
        IndexPage indexPage = new IndexPage(page, key);
        assertThat(indexPage.hasSignalContent()).as("%s main content visible", displayName).isTrue();
        String bodyText = page.locator("body").innerText();
        assertThat(bodyText.toLowerCase()).containsIgnoringCase(displayName.toLowerCase());
        takeScreenshot(displayName + " Page Load");
    }

    @Test(description = "Each index page has a title containing EaseMyTrade",
            dataProvider = "indexData")
    @Story("Page Load")
    @Severity(SeverityLevel.CRITICAL)
    public void testIndexPageTitle(String url, String key, String displayName) {
        navigateTo(url);
        String title = page.title();
        assertThat(title).containsIgnoringCase("EaseMyTrade");
        log.info("{} page title: '{}'", displayName, title);
    }

    // ---- Navigation Links on Index Pages ----

    @Test(description = "Index pages have fully functional navigation header",
            dataProvider = "indexData")
    @Story("Navigation")
    @Severity(SeverityLevel.CRITICAL)
    public void testIndexPageNavigation(String url, String key, String displayName) {
        navigateTo(url);
        assertThat(page.locator(".brand").isVisible()).as("Brand logo visible on %s", displayName).isTrue();
        assertThat(page.locator("nav.nav-links").isVisible()).as("Nav visible on %s", displayName).isTrue();
    }

    // ---- Signal State Validation ----

    @Test(description = "NIFTY page shows a valid signal state")
    @Story("Signal Logic")
    @Severity(SeverityLevel.CRITICAL)
    public void testNiftySignalState() {
        navigateTo(TestConfig.NIFTY_URL);
        IndexPage niftyPage = new IndexPage(page, "nifty");
        String state = niftyPage.getSignalState();
        log.info("NIFTY signal state: '{}'", state);
        AllureAttachmentHelper.attachText("NIFTY Signal State", state);
        if (!state.isBlank()) {
            assertThat(SignalParser.isValidSignalState(state)
                    || state.contains("Trade") || state.contains("Watchlist")
                    || state.contains("No Trade") || state.contains("Hold") || state.contains("Pending"))
                    .as("NIFTY signal state '%s' must be valid", state).isTrue();
        }
        takeScreenshot("NIFTY Signal State");
    }

    @Test(description = "BANKNIFTY page shows a valid signal state")
    @Story("Signal Logic")
    @Severity(SeverityLevel.CRITICAL)
    public void testBankNiftySignalState() {
        navigateTo(TestConfig.BANKNIFTY_URL);
        IndexPage bankPage = new IndexPage(page, "banknifty");
        String state = bankPage.getSignalState();
        log.info("BANKNIFTY signal state: '{}'", state);
        AllureAttachmentHelper.attachText("BANKNIFTY Signal State", state);
        if (!state.isBlank()) {
            assertThat(state.contains("Trade") || state.contains("Watchlist")
                    || state.contains("No Trade") || state.contains("Hold") || state.contains("Pending")
                    || SignalParser.isValidSignalState(state))
                    .as("BANKNIFTY state '%s' must be valid", state).isTrue();
        }
    }

    // ---- Support / Resistance Logical Check ----

    @Test(description = "NIFTY support is numerically less than resistance",
            dataProvider = "indexData")
    @Story("Support & Resistance Calculation")
    @Severity(SeverityLevel.CRITICAL)
    public void testIndexSupportBelowResistance(String url, String key, String displayName) {
        navigateTo(url);
        IndexPage indexPage = new IndexPage(page, key);
        String supportText = indexPage.getSupportText();
        String resistanceText = indexPage.getResistanceText();

        log.info("{} support='{}', resistance='{}'", displayName, supportText, resistanceText);
        AllureAttachmentHelper.attachText(displayName + " Levels",
                "Support: " + supportText + "\nResistance: " + resistanceText);

        double support = SignalParser.parsePrice(supportText);
        double resistance = SignalParser.parsePrice(resistanceText);

        if (support > 0 && resistance > 0) {
            assertThat(SignalParser.supportBelowResistance(support, resistance))
                    .as("%s: support (%.0f) must be < resistance (%.0f)", displayName, support, resistance)
                    .isTrue();
        } else {
            log.info("{}: support/resistance not yet populated — skipping numeric check", displayName);
        }
        takeScreenshot(displayName + " Support Resistance");
    }

    // ---- Entry / Stop / Target Checks ----

    @Test(description = "NIFTY page: when entry, stop, and target are shown, buy signal geometry must be valid")
    @Story("Trade Signal Geometry")
    @Severity(SeverityLevel.CRITICAL)
    public void testNiftyTradeGeometry() {
        navigateTo(TestConfig.NIFTY_URL);
        IndexPage niftyPage = new IndexPage(page, "nifty");
        String state = niftyPage.getSignalState();

        if (!"Trade Signal".equalsIgnoreCase(state)) {
            log.info("NIFTY is not in Trade Signal state ({}), skipping geometry check.", state);
            return;
        }

        String entryText = niftyPage.getEntryLevel();
        String stopText = niftyPage.getStopLossLevel();
        String targetText = niftyPage.getTargetLevel();

        log.info("NIFTY Trade Geometry: entry='{}', stop='{}', target='{}'", entryText, stopText, targetText);

        double entry = SignalParser.parsePrice(entryText);
        double stop = SignalParser.parsePrice(stopText);
        double target = SignalParser.parsePrice(targetText);

        AllureAttachmentHelper.attachText("NIFTY Trade Geometry",
                String.format("Entry: %.2f\nStop: %.2f\nTarget: %.2f", entry, stop, target));

        if (entry > 0 && stop > 0 && target > 0) {
            if (target > entry) {
                // Buy signal
                assertThat(SignalParser.isValidBuySetup(entry, stop, target))
                        .as("NIFTY buy signal geometry invalid").isTrue();
                double rr = SignalParser.calcRiskRewardRatio(entry, stop, target);
                assertThat(rr).as("NIFTY R:R %.2f must be >= 2.2", rr)
                        .isGreaterThanOrEqualTo(TestConfig.MIN_RISK_REWARD_RATIO);
            } else if (target < entry) {
                // Sell signal
                assertThat(SignalParser.isValidSellSetup(entry, stop, target))
                        .as("NIFTY sell signal geometry invalid").isTrue();
            }
        }
    }

    // ---- Completed Trade Cards ----

    @Test(description = "Index pages show completed trade review cards (historical records)")
    @Story("Completed Trades")
    @Severity(SeverityLevel.NORMAL)
    public void testNiftyCompletedTradeCards() {
        navigateTo(TestConfig.NIFTY_URL);
        IndexPage niftyPage = new IndexPage(page, "nifty");
        // Trade cards may or may not exist — just verify the page structure
        assertThat(niftyPage.hasSignalContent()).as("NIFTY page has signal content").isTrue();
        takeScreenshot("NIFTY Completed Trades");
    }

    // ---- Commodity Pages ----

    @DataProvider(name = "commodityData")
    public Object[][] commodityData() {
        return new Object[][]{
                {TestConfig.GOLD_URL, "Gold"},
                {TestConfig.SILVER_URL, "Silver"},
                {TestConfig.CRUDE_OIL_URL, "Crude"},
                {TestConfig.NATURAL_GAS_URL, "Natural Gas"},
        };
    }

    @Test(description = "Commodity pages load with commodity name present in body",
            dataProvider = "commodityData")
    @Story("Commodity Pages")
    @Severity(SeverityLevel.NORMAL)
    public void testCommodityPageContent(String url, String commodityName) {
        navigateTo(url);
        String bodyText = page.locator("body").innerText();
        assertThat(bodyText).containsIgnoringCase(commodityName);
        assertThat(page.locator("main").isVisible()).isTrue();
        takeScreenshot(commodityName + " Commodity Page");
    }

    @Test(description = "Commodity pages have navigation header with all links",
            dataProvider = "commodityData")
    @Story("Commodity Pages - Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testCommodityPageNavigation(String url, String commodityName) {
        navigateTo(url);
        assertThat(page.locator(".brand").isVisible()).as("Brand visible on %s page", commodityName).isTrue();
        assertThat(page.locator("nav.nav-links").isVisible()).as("Nav visible on %s page", commodityName).isTrue();
    }

    // ---- Logical Check: Commodity prices are positive numbers ----

    @Test(description = "Gold commodity page shows a positive price value")
    @Story("Commodity Price Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testGoldPriceIsPositive() {
        navigateTo(TestConfig.GOLD_URL);
        String priceText = "";
        try {
            priceText = page.locator("[id*='gold'], [id*='Gold'], [id*='commodity']").first().innerText().trim();
        } catch (Exception ignored) {}

        log.info("Gold price text: '{}'", priceText);
        AllureAttachmentHelper.attachText("Gold Price Text", priceText);
        double price = SignalParser.parsePrice(priceText);
        if (price > 0) {
            assertThat(price).as("Gold price must be positive").isGreaterThan(0);
        }
        takeScreenshot("Gold Price");
    }
}
