package com.easemytrade.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    public HomePage(Page page) { super(page); }

    // Hero section
    public Locator heroSection() { return page.locator(".hero-band"); }
    public Locator heroTitle() { return page.locator(".hero-band h1"); }
    public Locator heroLead() { return page.locator(".hero-band .lead"); }
    public Locator heroOpenMarketOverviewBtn() { return page.locator("a.primary-action[href='/#market-overview']"); }
    public Locator heroIndianIndicesBtn() { return page.locator("a.secondary-action[href='/nifty/today/']"); }

    // Market watchlist in hero
    public Locator niftyWatchRow() { return page.locator("a.watch-row[href='/nifty/today/']"); }
    public Locator bankniftyWatchRow() { return page.locator("a.watch-row[href='/indices/banknifty/']"); }
    public Locator finniftyWatchRow() { return page.locator("a.watch-row[href='/indices/finnifty/']"); }
    public Locator sensexWatchRow() { return page.locator("a.watch-row[href='/indices/sensex/']"); }
    public Locator goldWatchRow() { return page.locator("a.watch-row[href='/commodities/gold/']"); }

    // Live levels in watchlist
    public String niftyWatchLevel() { return textOf("heroWatchNiftyLevel"); }
    public String niftyWatchChange() { return textOf("heroWatchNiftyChange"); }
    public String bankNiftyWatchLevel() { return textOf("heroWatchBankLevel"); }
    public String finniftyWatchLevel() { return textOf("heroWatchFinLevel"); }
    public String sensexWatchLevel() { return textOf("heroWatchSensexLevel"); }

    // Trading zones
    public Locator tradingZonesSection() { return page.locator(".trading-zones-band"); }
    public Locator niftyZoneCard() { return page.locator(".zone-card").first(); }
    public String niftyZoneLevel() { return textOf("homeNiftyZoneLevel"); }
    public String niftyZoneSupport() { return textOf("homeNiftyZoneSupport"); }
    public String niftyZoneResistance() { return textOf("homeNiftyZoneResistance"); }
    public String niftyZoneSignal() { return textOf("homeNiftyZoneSignal"); }

    public String bankNiftyZoneLevel() { return textOf("homeBankNiftyZoneLevel"); }
    public String bankNiftyZoneSupport() { return textOf("homeBankNiftyZoneSupport"); }
    public String bankNiftyZoneResistance() { return textOf("homeBankNiftyZoneResistance"); }

    public String finniftyZoneLevel() { return textOf("homeFinNiftyZoneLevel"); }
    public String finniftyZoneSupport() { return textOf("homeFinNiftyZoneSupport"); }
    public String finniftyZoneResistance() { return textOf("homeFinNiftyZoneResistance"); }

    public String sensexZoneLevel() { return textOf("homeSensexZoneLevel"); }
    public String sensexZoneSupport() { return textOf("homeSensexZoneSupport"); }
    public String sensexZoneResistance() { return textOf("homeSensexZoneResistance"); }

    // Completed trades spotlight
    public Locator tradeSpotlightSection() { return page.locator(".trade-spotlight-band"); }
    public Locator tradeSpotlightGrid() { return page.locator("#tradeSpotlightGrid"); }

    // Market drivers
    public Locator marketDriversPanel() { return page.locator("#marketDriversPanel"); }
    public String marketDriversSummary() { return textOf("marketDriversSummary"); }

    // Session label
    public String sessionLabel() { return textOf("sessionLabel"); }

    // Pulse chart
    public Locator pulseChart() { return page.locator("#pulseChart"); }
}
