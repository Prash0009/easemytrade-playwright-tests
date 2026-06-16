package com.easemytrade.tests;

import com.easemytrade.base.BaseTest;
import com.easemytrade.config.TestConfig;
import com.easemytrade.pages.HomePage;
import com.easemytrade.utils.AllureAttachmentHelper;
import com.easemytrade.utils.SignalParser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("EaseMyTrade")
@Feature("Home Page")
public class HomePageTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod
    public void openHomePage() {
        navigateTo(TestConfig.HOME_URL);
        homePage = new HomePage(page);
    }

    // ---- Page Structure ----

    @Test(description = "Home page loads with correct title")
    @Story("Page Load")
    @Severity(SeverityLevel.BLOCKER)
    public void testHomePageTitle() {
        String title = homePage.getTitle();
        log.info("Home page title: {}", title);
        AllureAttachmentHelper.attachText("Page Title", title);
        assertThat(title).containsIgnoringCase("EaseMyTrade");
    }

    @Test(description = "Home page URL matches expected base URL")
    @Story("Page Load")
    @Severity(SeverityLevel.CRITICAL)
    public void testHomePageUrl() {
        // easemytrade.in redirects to www.easemytrade.in
        assertThat(homePage.getUrl()).contains("easemytrade.in");
    }

    @Test(description = "Header navigation is visible with all links")
    @Story("Navigation")
    @Severity(SeverityLevel.CRITICAL)
    public void testHeaderNavigation() {
        assertThat(homePage.headerLogo().isVisible()).as("Brand logo visible").isTrue();
        assertThat(homePage.headerNav().isVisible()).as("Nav visible").isTrue();
        assertThat(homePage.expertViewNavLink().isVisible()).as("Expert View link visible").isTrue();
        assertThat(homePage.newsNavLink().isVisible()).as("News link visible").isTrue();
        assertThat(homePage.methodologyNavLink().isVisible()).as("Methodology link visible").isTrue();
        assertThat(homePage.telegramNavLink().isVisible()).as("Telegram link visible").isTrue();
        assertThat(homePage.contactNavLink().isVisible()).as("Contact link visible").isTrue();
        takeScreenshot("Header Navigation");
    }

    @Test(description = "Hero section is present with EaseMyTrade branding")
    @Story("Hero Section")
    @Severity(SeverityLevel.CRITICAL)
    public void testHeroSection() {
        assertThat(homePage.heroSection().isVisible()).as("Hero section visible").isTrue();
        String heading = homePage.heroTitle().innerText().trim();
        assertThat(heading).containsIgnoringCase("EaseMyTrade");
        assertThat(homePage.heroLead().isVisible()).as("Hero lead paragraph visible").isTrue();
        takeScreenshot("Hero Section");
    }

    @Test(description = "Hero CTA buttons are present and correctly linked")
    @Story("Hero Section")
    @Severity(SeverityLevel.NORMAL)
    public void testHeroCallToActionButtons() {
        assertThat(homePage.heroOpenMarketOverviewBtn().isVisible()).as("Open Market Overview CTA visible").isTrue();
        assertThat(homePage.heroIndianIndicesBtn().isVisible()).as("Indian Indices CTA visible").isTrue();
    }

    // ---- Market Watchlist ----

    @Test(description = "Market watchlist shows all tracked instruments")
    @Story("Market Watchlist")
    @Severity(SeverityLevel.CRITICAL)
    public void testMarketWatchlistInstruments() {
        assertThat(homePage.niftyWatchRow().isVisible()).as("NIFTY watch row visible").isTrue();
        assertThat(homePage.bankniftyWatchRow().isVisible()).as("BANKNIFTY watch row visible").isTrue();
        assertThat(homePage.finniftyWatchRow().isVisible()).as("FINNIFTY watch row visible").isTrue();
        assertThat(homePage.sensexWatchRow().isVisible()).as("SENSEX watch row visible").isTrue();
        assertThat(homePage.goldWatchRow().isVisible()).as("GOLD watch row visible").isTrue();
        takeScreenshot("Market Watchlist");
    }

    @Test(description = "Market watchlist links navigate to correct pages")
    @Story("Navigation")
    @Severity(SeverityLevel.CRITICAL)
    public void testWatchlistNavigation() {
        String niftyHref = homePage.niftyWatchRow().getAttribute("href");
        assertThat(niftyHref).isEqualTo("/nifty/today/");

        String bankHref = homePage.bankniftyWatchRow().getAttribute("href");
        assertThat(bankHref).isEqualTo("/indices/banknifty/");

        String finniftyHref = homePage.finniftyWatchRow().getAttribute("href");
        assertThat(finniftyHref).isEqualTo("/indices/finnifty/");

        String sensexHref = homePage.sensexWatchRow().getAttribute("href");
        assertThat(sensexHref).isEqualTo("/indices/sensex/");
    }

    // ---- Trading Zones ----

    @Test(description = "Trading zones section is visible with all 4 index cards")
    @Story("Trading Zones")
    @Severity(SeverityLevel.CRITICAL)
    public void testTradingZonesSectionVisible() {
        assertThat(homePage.tradingZonesSection().isVisible()).as("Trading zones section visible").isTrue();
        int zoneCards = page.locator(".zone-card").count();
        assertThat(zoneCards).as("Should have 4 zone cards (NIFTY, BANKNIFTY, FINNIFTY, SENSEX)").isEqualTo(4);
        takeScreenshot("Trading Zones");
    }

    @Test(description = "NIFTY zone: support level is numerically less than resistance level")
    @Story("Signal Logic - Calculation")
    @Severity(SeverityLevel.CRITICAL)
    public void testNiftyZoneSupportBelowResistance() {
        String supportText = homePage.niftyZoneSupport();
        String resistanceText = homePage.niftyZoneResistance();
        log.info("NIFTY Zone - Support: {}, Resistance: {}", supportText, resistanceText);
        AllureAttachmentHelper.attachText("NIFTY Zone Levels",
                "Support: " + supportText + "\nResistance: " + resistanceText);

        double support = SignalParser.parsePrice(supportText);
        double resistance = SignalParser.parsePrice(resistanceText);

        if (support > 0 && resistance > 0) {
            assertThat(SignalParser.supportBelowResistance(support, resistance))
                    .as("NIFTY support (%s) must be less than resistance (%s)", supportText, resistanceText)
                    .isTrue();
        } else {
            log.warn("NIFTY zone levels not yet populated (support={}, resistance={}). Skipping numeric check.", supportText, resistanceText);
        }
    }

    @Test(description = "BANKNIFTY zone: support level is numerically less than resistance level")
    @Story("Signal Logic - Calculation")
    @Severity(SeverityLevel.CRITICAL)
    public void testBankNiftyZoneSupportBelowResistance() {
        String supportText = homePage.bankNiftyZoneSupport();
        String resistanceText = homePage.bankNiftyZoneResistance();
        log.info("BANKNIFTY Zone - Support: {}, Resistance: {}", supportText, resistanceText);

        double support = SignalParser.parsePrice(supportText);
        double resistance = SignalParser.parsePrice(resistanceText);

        if (support > 0 && resistance > 0) {
            assertThat(SignalParser.supportBelowResistance(support, resistance))
                    .as("BANKNIFTY support (%s) must be less than resistance (%s)", supportText, resistanceText)
                    .isTrue();
        }
    }

    @Test(description = "FINNIFTY zone: support level is numerically less than resistance level")
    @Story("Signal Logic - Calculation")
    @Severity(SeverityLevel.CRITICAL)
    public void testFinniftyZoneSupportBelowResistance() {
        String supportText = homePage.finniftyZoneSupport();
        String resistanceText = homePage.finniftyZoneResistance();
        log.info("FINNIFTY Zone - Support: {}, Resistance: {}", supportText, resistanceText);

        double support = SignalParser.parsePrice(supportText);
        double resistance = SignalParser.parsePrice(resistanceText);

        if (support > 0 && resistance > 0) {
            assertThat(SignalParser.supportBelowResistance(support, resistance))
                    .as("FINNIFTY support (%s) must be less than resistance (%s)", supportText, resistanceText)
                    .isTrue();
        }
    }

    @Test(description = "SENSEX zone: support level is numerically less than resistance level")
    @Story("Signal Logic - Calculation")
    @Severity(SeverityLevel.CRITICAL)
    public void testSensexZoneSupportBelowResistance() {
        String supportText = homePage.sensexZoneSupport();
        String resistanceText = homePage.sensexZoneResistance();
        log.info("SENSEX Zone - Support: {}, Resistance: {}", supportText, resistanceText);

        double support = SignalParser.parsePrice(supportText);
        double resistance = SignalParser.parsePrice(resistanceText);

        if (support > 0 && resistance > 0) {
            assertThat(SignalParser.supportBelowResistance(support, resistance))
                    .as("SENSEX support (%s) must be less than resistance (%s)", supportText, resistanceText)
                    .isTrue();
        }
    }

    @Test(description = "NIFTY zone signal state is one of the valid values")
    @Story("Signal Logic")
    @Severity(SeverityLevel.CRITICAL)
    public void testNiftyZoneSignalState() {
        String signal = homePage.niftyZoneSignal();
        log.info("NIFTY zone signal: {}", signal);
        AllureAttachmentHelper.attachText("NIFTY Signal State", signal);
        if (!signal.isBlank() && !signal.equals("--")) {
            assertThat(SignalParser.isValidSignalState(signal) || signal.contains("Trade") || signal.contains("Watchlist") || signal.contains("Hold"))
                    .as("Signal state '%s' should be a valid state", signal).isTrue();
        }
    }

    // ---- Completed Trades Spotlight ----

    @Test(description = "Previous completed trades spotlight section is present")
    @Story("Completed Trades")
    @Severity(SeverityLevel.NORMAL)
    public void testCompletedTradesSection() {
        assertThat(homePage.tradeSpotlightSection().isVisible()).as("Trade spotlight section visible").isTrue();
        // The grid is populated asynchronously from /api/live-market (with a
        // /data/market.json fallback) after page load, so it can briefly have
        // zero children — and therefore zero height — right after navigation.
        // Wait for it to actually render instead of checking visibility instantly.
        try {
            homePage.tradeSpotlightGrid().waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(TestConfig.SLOW_RESOURCE_TIMEOUT_MS));
        } catch (Exception ignored) {
            // fall through to the assertion below for a clear failure message
        }
        assertThat(homePage.tradeSpotlightGrid().isVisible()).as("Trade spotlight grid visible").isTrue();
        takeScreenshot("Completed Trades Spotlight");
    }

    // ---- Market Drivers ----

    @Test(description = "Market drivers panel is present")
    @Story("Market Drivers")
    @Severity(SeverityLevel.NORMAL)
    public void testMarketDriversPanel() {
        assertThat(homePage.marketDriversPanel().isVisible()).as("Market drivers panel visible").isTrue();
        takeScreenshot("Market Drivers Panel");
    }

    // ---- Footer ----

    @Test(description = "Footer is present with disclaimer link")
    @Story("Footer")
    @Severity(SeverityLevel.NORMAL)
    public void testFooterPresence() {
        assertThat(homePage.footer().isVisible()).as("Footer visible").isTrue();
        assertThat(homePage.footerDisclaimerLink().isVisible()).as("Disclaimer link visible in footer").isTrue();
    }

    @Test(description = "Footer disclaimer link points to /disclaimer/")
    @Story("Footer")
    @Severity(SeverityLevel.NORMAL)
    public void testFooterDisclaimerLinkHref() {
        String href = homePage.footerDisclaimerLink().getAttribute("href");
        assertThat(href).isEqualTo("/disclaimer/");
    }

    // ---- Market Ticker ----

    @Test(description = "Live market ticker strip is present in the header area")
    @Story("Market Ticker")
    @Severity(SeverityLevel.NORMAL)
    public void testMarketTickerPresence() {
        assertThat(homePage.marketTicker().isVisible()).as("Market ticker visible").isTrue();
    }

    // ---- Live Clock ----

    @Test(description = "IST live clock element is present in the navigation")
    @Story("Live Clock")
    @Severity(SeverityLevel.MINOR)
    public void testLiveClockPresence() {
        assertThat(homePage.liveClockElement().isVisible()).as("IST live clock visible").isTrue();
    }

    // ---- Pulse Chart ----

    @Test(description = "Hero pulse chart canvas element is rendered")
    @Story("Chart")
    @Severity(SeverityLevel.NORMAL)
    public void testPulseChartPresence() {
        assertThat(homePage.pulseChart().isVisible()).as("Pulse chart canvas visible").isTrue();
    }

    // ---- Indian Indices Dropdown ----

    @Test(description = "Indian Indices dropdown in nav reveals NIFTY, BANKNIFTY, FINNIFTY, SENSEX links")
    @Story("Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testIndianIndicesDropdown() {
        homePage.indianIndicesDropdown().click();
        assertThat(page.locator("a[href='/nifty/today/']").first().isVisible()).as("NIFTY link").isTrue();
        assertThat(page.locator("a[href='/indices/banknifty/']").first().isVisible()).as("BANKNIFTY link").isTrue();
        assertThat(page.locator("a[href='/indices/finnifty/']").first().isVisible()).as("FINNIFTY link").isTrue();
        assertThat(page.locator("a[href='/indices/sensex/']").first().isVisible()).as("SENSEX link").isTrue();
        takeScreenshot("Indian Indices Dropdown");
    }
}
