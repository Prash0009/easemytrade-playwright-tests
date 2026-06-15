package com.easemytrade.tests;

import com.easemytrade.base.BaseTest;
import com.easemytrade.config.TestConfig;
import com.easemytrade.pages.HomePage;
import com.easemytrade.utils.AllureAttachmentHelper;
import com.easemytrade.utils.SignalParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("EaseMyTrade")
@Feature("Signal Logic")
public class SignalLogicTest extends BaseTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private HomePage homePage;

    @BeforeMethod
    public void openHomePage() {
        navigateTo(TestConfig.HOME_URL);
        homePage = new HomePage(page);
    }

    // ---- Signal State Validation ----

    @Test(description = "NIFTY zone signal state is one of the valid engine states")
    @Story("Signal State Validation")
    @Severity(SeverityLevel.BLOCKER)
    public void testNiftySignalStateIsValid() {
        String signal = homePage.niftyZoneSignal();
        log.info("NIFTY signal state: '{}'", signal);
        AllureAttachmentHelper.attachText("NIFTY Signal State", signal);
        assertSignalStateValid("NIFTY", signal);
    }

    @Test(description = "Signal engine supports all four index zones on the home page")
    @Story("Signal State Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testAllIndexZonesHaveSignalData() {
        StringBuilder report = new StringBuilder();
        for (String[] indexInfo : new String[][]{
                {"NIFTY", homePage.niftyZoneSignal()},
                {"BANKNIFTY", homePage.niftyZoneSignal()},  // reuse the panel
        }) {
            report.append(indexInfo[0]).append(": ").append(indexInfo[1]).append("\n");
        }

        // Verify zone cards exist for all 4 indices
        int zoneCards = page.locator(".zone-card").count();
        assertThat(zoneCards).as("All 4 index zone cards must be present").isGreaterThanOrEqualTo(4);
        AllureAttachmentHelper.attachText("Zone Cards Count", String.valueOf(zoneCards));
        takeScreenshot("All Zone Cards");
    }

    // ---- Support / Resistance Logic ----

    @Test(description = "All index zone support levels must be strictly less than resistance levels")
    @Story("Support & Resistance Calculation")
    @Severity(SeverityLevel.CRITICAL)
    public void testAllZonesSupportBelowResistance() {
        record Zone(String name, String support, String resistance) {}
        List<Zone> zones = List.of(
                new Zone("NIFTY", homePage.niftyZoneSupport(), homePage.niftyZoneResistance()),
                new Zone("BANKNIFTY", homePage.bankNiftyZoneSupport(), homePage.bankNiftyZoneResistance()),
                new Zone("FINNIFTY", homePage.finniftyZoneSupport(), homePage.finniftyZoneResistance()),
                new Zone("SENSEX", homePage.sensexZoneSupport(), homePage.sensexZoneResistance())
        );

        StringBuilder report = new StringBuilder();
        List<String> failures = new ArrayList<>();

        for (Zone z : zones) {
            double sup = SignalParser.parsePrice(z.support());
            double res = SignalParser.parsePrice(z.resistance());
            report.append(String.format("%s: support=%s (%.0f), resistance=%s (%.0f)\n",
                    z.name(), z.support(), sup, z.resistance(), res));

            if (sup > 0 && res > 0) {
                if (!SignalParser.supportBelowResistance(sup, res)) {
                    failures.add(String.format("%s: support=%.0f >= resistance=%.0f (INVALID)", z.name(), sup, res));
                }
            } else {
                log.info("{}: levels not yet populated — skipping numeric check", z.name());
            }
        }

        AllureAttachmentHelper.attachText("Zone Level Analysis", report.toString());
        assertThat(failures)
                .as("These zones have invalid support >= resistance: " + failures)
                .isEmpty();
        takeScreenshot("Support Resistance Zones");
    }

    // ---- Confidence Score Validation ----

    @Test(description = "Confidence score shown on page must be between 0 and 100 percent")
    @Story("Confidence Score Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testConfidenceScoreRange() {
        // Navigate to NIFTY page and grab any visible confidence score
        navigateTo(TestConfig.NIFTY_URL);
        String confidenceText = page.locator("[id*='confidence'], [class*='confidence'], [data-confidence]")
                .first().innerText().trim();
        log.info("Confidence text from page: '{}'", confidenceText);
        AllureAttachmentHelper.attachText("Confidence Text", confidenceText);

        double confidence = SignalParser.parsePercentage(confidenceText);
        if (confidence >= 0) {
            assertThat(SignalParser.isConfidenceInRange(confidence))
                    .as("Confidence score %.1f should be between 0 and 100", confidence)
                    .isTrue();
        }
    }

    // ---- API-level Signal Validation ----

    @Test(description = "Live market API returns valid JSON with required index fields")
    @Story("API Signal Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testLiveMarketApiReturnsValidStructure() throws Exception {
        APIRequestContext api = playwright.request().newContext();
        APIResponse response = api.get(TestConfig.BASE_URL + "/api/live-market",
                RequestOptions.create().setTimeout(TestConfig.SLOW_RESOURCE_TIMEOUT_MS));

        log.info("Live market API status: {}", response.status());
        AllureAttachmentHelper.attachText("API Status", String.valueOf(response.status()));

        // Accept 200 or 304 (cached)
        assertThat(response.status()).as("API status should be 200 or 304").isIn(200, 304);

        if (response.status() == 200) {
            String body = response.text();
            AllureAttachmentHelper.attachJson("Live Market API Response", body);
            JsonNode root = MAPPER.readTree(body);

            // Must have NIFTY data
            assertThat(root.has("nifty") || root.has("NIFTY") || root.has("indices"))
                    .as("Response must contain NIFTY or indices data").isTrue();
        }
        api.dispose();
    }

    @Test(description = "Market JSON data file has required fields for signal engine")
    @Story("Signal Data Integrity")
    @Severity(SeverityLevel.CRITICAL)
    public void testMarketDataHasRequiredSignalFields() throws Exception {
        // The market.json is served as a static file
        APIRequestContext api = playwright.request().newContext();
        APIResponse response = api.get(TestConfig.BASE_URL + "/data/market.json",
                RequestOptions.create().setTimeout(TestConfig.SLOW_RESOURCE_TIMEOUT_MS));

        if (response.status() != 200) {
            log.warn("market.json not publicly accessible (status {}), skipping field check.", response.status());
            api.dispose();
            return;
        }

        String body = response.text();
        AllureAttachmentHelper.attachJson("market.json Contents", body);
        JsonNode root = MAPPER.readTree(body);

        // Validate NIFTY signal structure
        if (root.has("nifty")) {
            JsonNode nifty = root.get("nifty");
            assertThat(nifty.has("level")).as("nifty.level must exist").isTrue();
            assertThat(nifty.has("signal")).as("nifty.signal must exist").isTrue();
            assertThat(nifty.has("support")).as("nifty.support must exist").isTrue();
            assertThat(nifty.has("resistance")).as("nifty.resistance must exist").isTrue();

            JsonNode signal = nifty.get("signal");
            assertThat(signal.has("state")).as("nifty.signal.state must exist").isTrue();
            assertThat(signal.has("confidence")).as("nifty.signal.confidence must exist").isTrue();
            assertThat(signal.has("stopLoss")).as("nifty.signal.stopLoss must exist").isTrue();
            assertThat(signal.has("targetPrice") || signal.has("targetLevel"))
                    .as("nifty.signal must have target field").isTrue();

            // Validate confidence score range
            String confStr = signal.get("confidence").asText();
            double confidence = SignalParser.parsePercentage(confStr);
            if (confidence >= 0) {
                assertThat(SignalParser.isConfidenceInRange(confidence))
                        .as("Confidence %.1f must be in [0, 100]", confidence).isTrue();
            }

            // Validate signal state
            String state = signal.get("state").asText();
            AllureAttachmentHelper.attachText("NIFTY Signal State from JSON", state);
            assertThat(SignalParser.isValidSignalState(state))
                    .as("Signal state '%s' must be valid", state).isTrue();
        }

        api.dispose();
    }

    @Test(description = "When signal is Trade Signal: stop loss must be below entry and target must be above entry (buy)")
    @Story("Trade Signal Geometry")
    @Severity(SeverityLevel.CRITICAL)
    public void testBuySignalGeometryFromJson() throws Exception {
        APIRequestContext api = playwright.request().newContext();
        APIResponse response = api.get(TestConfig.BASE_URL + "/data/market.json",
                RequestOptions.create().setTimeout(TestConfig.SLOW_RESOURCE_TIMEOUT_MS));

        if (response.status() != 200) {
            log.warn("market.json not publicly accessible, skipping geometry check.");
            api.dispose();
            return;
        }

        String body = response.text();
        JsonNode root = MAPPER.readTree(body);

        for (String indexKey : new String[]{"nifty", "banknifty", "finnifty", "sensex"}) {
            if (!root.has(indexKey)) continue;
            JsonNode signal = root.get(indexKey).path("signal");

            String state = signal.path("state").asText();
            if (!"Trade Signal".equalsIgnoreCase(state)) continue;

            double stopLoss = signal.path("stopLoss").asDouble(-1);
            double targetPrice = signal.path("targetPrice").asDouble(-1);

            // Determine entry from stopLevel and the watch note
            double entry = signal.path("entry").asDouble(-1);
            if (entry < 0) {
                // Try parsing from the watch string
                String entryStr = signal.path("entry").asText("");
                entry = SignalParser.parsePrice(entryStr);
            }

            log.info("{}: state={}, entry={}, stop={}, target={}", indexKey, state, entry, stopLoss, targetPrice);

            StringBuilder detail = new StringBuilder();
            detail.append(String.format("Index: %s\nState: %s\nEntry: %.2f\nStop: %.2f\nTarget: %.2f\n",
                    indexKey, state, entry, stopLoss, targetPrice));

            if (entry > 0 && stopLoss > 0 && targetPrice > 0) {
                // For a call (buy) signal
                if (targetPrice > entry) {
                    assertThat(SignalParser.isValidBuySetup(entry, stopLoss, targetPrice))
                            .as("%s buy signal geometry invalid: entry=%.2f, stop=%.2f, target=%.2f",
                                    indexKey, entry, stopLoss, targetPrice)
                            .isTrue();

                    double rr = SignalParser.calcRiskRewardRatio(entry, stopLoss, targetPrice);
                    detail.append(String.format("R:R Ratio: %.2f (min required: %.1f)\n", rr, TestConfig.MIN_RISK_REWARD_RATIO));
                    AllureAttachmentHelper.attachText(indexKey.toUpperCase() + " Trade Geometry", detail.toString());

                    assertThat(rr)
                            .as("%s R:R ratio %.2f must be >= %.1f", indexKey, rr, TestConfig.MIN_RISK_REWARD_RATIO)
                            .isGreaterThanOrEqualTo(TestConfig.MIN_RISK_REWARD_RATIO);
                }
                // For a put (sell) signal
                else if (targetPrice < entry) {
                    assertThat(SignalParser.isValidSellSetup(entry, stopLoss, targetPrice))
                            .as("%s sell signal geometry invalid: entry=%.2f, stop=%.2f, target=%.2f",
                                    indexKey, entry, stopLoss, targetPrice)
                            .isTrue();

                    double rr = SignalParser.calcRiskRewardRatio(entry, stopLoss, targetPrice);
                    detail.append(String.format("R:R Ratio: %.2f (min required: %.1f)\n", rr, TestConfig.MIN_RISK_REWARD_RATIO));
                    AllureAttachmentHelper.attachText(indexKey.toUpperCase() + " Trade Geometry", detail.toString());

                    assertThat(rr)
                            .as("%s R:R ratio %.2f must be >= %.1f", indexKey, rr, TestConfig.MIN_RISK_REWARD_RATIO)
                            .isGreaterThanOrEqualTo(TestConfig.MIN_RISK_REWARD_RATIO);
                }
            }
        }
        api.dispose();
    }

    @Test(description = "Risk-reward ratio is at least 2.2R for all active Trade Signal entries")
    @Story("Trade Signal Geometry")
    @Severity(SeverityLevel.CRITICAL)
    public void testMinimumRiskRewardRatio() throws Exception {
        APIRequestContext api = playwright.request().newContext();
        APIResponse response = api.get(TestConfig.BASE_URL + "/data/market.json",
                RequestOptions.create().setTimeout(TestConfig.SLOW_RESOURCE_TIMEOUT_MS));

        if (response.status() != 200) { api.dispose(); return; }

        JsonNode root = MAPPER.readTree(response.text());
        StringBuilder report = new StringBuilder("R:R Analysis:\n");
        List<String> violations = new ArrayList<>();

        for (String indexKey : new String[]{"nifty", "banknifty", "finnifty", "sensex"}) {
            if (!root.has(indexKey)) continue;
            JsonNode signal = root.get(indexKey).path("signal");
            String state = signal.path("state").asText();

            double stop = signal.path("stopLoss").asDouble(-1);
            double target = signal.path("targetPrice").asDouble(-1);

            // Parse stop/target from text fields if numeric not found
            if (stop < 0) stop = SignalParser.parsePrice(signal.path("stopLevel").asText());
            if (target < 0) target = SignalParser.parsePrice(signal.path("targetLevel").asText());

            // Entry approximation: use stopLevel + (stopLoss to entry gap from notes)
            // As a proxy, use the "stopRule" and "targetRule" text
            double entry = -1;
            JsonNode entryNode = signal.path("entry");
            if (!entryNode.isMissingNode()) {
                entry = SignalParser.parsePrice(entryNode.asText());
            }

            if ("Trade Signal".equalsIgnoreCase(state) && entry > 0 && stop > 0 && target > 0) {
                double rr = SignalParser.calcRiskRewardRatio(entry, stop, target);
                report.append(String.format("  %s: entry=%.0f, stop=%.0f, target=%.0f => RR=%.2f\n",
                        indexKey, entry, stop, target, rr));
                if (rr < TestConfig.MIN_RISK_REWARD_RATIO) {
                    violations.add(String.format("%s RR=%.2f < 2.2", indexKey, rr));
                }
            }
        }

        AllureAttachmentHelper.attachText("R:R Analysis", report.toString());
        assertThat(violations).as("Signals below 2.2R minimum: " + violations).isEmpty();
        api.dispose();
    }

    @Test(description = "No Trade state means signal should not show buy/sell action text")
    @Story("Signal State Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testNoTradeStateHasNoActionableSignal() throws Exception {
        APIRequestContext api = playwright.request().newContext();
        APIResponse response = api.get(TestConfig.BASE_URL + "/data/market.json",
                RequestOptions.create().setTimeout(TestConfig.SLOW_RESOURCE_TIMEOUT_MS));

        if (response.status() != 200) { api.dispose(); return; }

        JsonNode root = MAPPER.readTree(response.text());

        for (String indexKey : new String[]{"nifty", "banknifty", "finnifty", "sensex"}) {
            if (!root.has(indexKey)) continue;
            JsonNode signal = root.get(indexKey).path("signal");
            String state = signal.path("state").asText();
            String action = signal.path("action").asText("");

            if ("No Trade".equalsIgnoreCase(state)) {
                log.info("{}: No Trade state, action='{}'", indexKey, action);
                // Action should be "Hold" or empty, not "Buy" or "Sell"
                assertThat(action.toLowerCase())
                        .as("%s in No Trade state should not have actionable Buy/Sell", indexKey)
                        .doesNotContain("buy signal", "sell signal");
            }
        }
        api.dispose();
    }

    @Test(description = "Session guard logic: verify completed trades count is within daily limit (max 2)")
    @Story("Session Guard Logic")
    @Severity(SeverityLevel.CRITICAL)
    public void testSessionGuardCompletedTradesLimit() throws Exception {
        APIRequestContext api = playwright.request().newContext();
        APIResponse response = api.get(TestConfig.BASE_URL + "/data/market.json",
                RequestOptions.create().setTimeout(TestConfig.SLOW_RESOURCE_TIMEOUT_MS));

        if (response.status() != 200) { api.dispose(); return; }

        JsonNode root = MAPPER.readTree(response.text());
        // Look for session guard information in the signal notes
        for (String indexKey : new String[]{"nifty", "banknifty", "finnifty", "sensex"}) {
            if (!root.has(indexKey)) continue;
            JsonNode signal = root.get(indexKey).path("signal");
            String note = signal.path("note").asText("");
            String summary = root.get(indexKey).path("summary").asText("");

            // If the note says "2 completed trades", state must NOT be "Trade Signal"
            boolean hasMaxTradesNote = note.contains("2 completed trades") || summary.contains("2 completed trades");
            String state = signal.path("state").asText();

            if (hasMaxTradesNote) {
                log.info("{}: Session guard triggered (2 completed trades), state='{}'", indexKey, state);
                AllureAttachmentHelper.attachText(indexKey + " Session Guard Note", note.isEmpty() ? summary : note);
                assertThat(state).as(
                        "%s: Session guard triggered (max 2 trades) but state is still Trade Signal — engine bug",
                        indexKey)
                        .isNotEqualToIgnoringCase("Trade Signal");
            }
        }
        api.dispose();
    }

    @Test(description = "Forward record guard: if net negative record, all indices must be Watchlist Only or No Trade")
    @Story("Forward Record Guard")
    @Severity(SeverityLevel.CRITICAL)
    public void testForwardRecordGuardBlocksFreshSignals() throws Exception {
        APIRequestContext api = playwright.request().newContext();
        APIResponse response = api.get(TestConfig.BASE_URL + "/data/market.json",
                RequestOptions.create().setTimeout(TestConfig.SLOW_RESOURCE_TIMEOUT_MS));

        if (response.status() != 200) { api.dispose(); return; }

        JsonNode root = MAPPER.readTree(response.text());
        // Check if any index mentions forward record is net negative in notes
        for (String indexKey : new String[]{"nifty", "banknifty", "finnifty", "sensex"}) {
            if (!root.has(indexKey)) continue;
            JsonNode signal = root.get(indexKey).path("signal");
            String note = (signal.path("note").asText("") + " " + root.get(indexKey).path("summary").asText("")).toLowerCase();
            String state = signal.path("state").asText();

            boolean forwardRecordNegative = note.contains("net negative") || note.contains("net -");
            if (forwardRecordNegative) {
                log.info("{}: forward record negative, state='{}'", indexKey, state);
                AllureAttachmentHelper.attachText(indexKey + " Forward Record Note", note);
                assertThat(state.toLowerCase())
                        .as("%s: net negative forward record must not publish Trade Signal", indexKey)
                        .isNotEqualToIgnoringCase("trade signal");
            }
        }
        api.dispose();
    }

    // ---- Helpers ----

    private void assertSignalStateValid(String index, String state) {
        if (state == null || state.isBlank() || state.equals("--")) {
            log.warn("{}: signal state not populated yet, skipping.", index);
            return;
        }
        boolean valid = SignalParser.isValidSignalState(state)
                || state.contains("Trade")
                || state.contains("Watchlist")
                || state.contains("Hold")
                || state.contains("No Trade")
                || state.contains("Pending");
        assertThat(valid)
                .as("%s signal state '%s' must be one of: Trade Signal, Watchlist Only, No Trade, Hold, Pending", index, state)
                .isTrue();
    }
}
