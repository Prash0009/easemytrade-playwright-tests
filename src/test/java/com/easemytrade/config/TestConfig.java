package com.easemytrade.config;

public class TestConfig {

    public static final String BASE_URL = System.getProperty("app.base.url", "https://easemytrade.in");
    public static final String BROWSER = System.getProperty("browser", "chromium");
    public static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "true"));
    public static final int DEFAULT_TIMEOUT_MS = 30_000;
    public static final int NAVIGATION_TIMEOUT_MS = 60_000;
    public static final int SLOW_RESOURCE_TIMEOUT_MS = 120_000;

    // Test viewer account — used to authenticate before hitting pages gated by
    // the temporary-unlock window (see data/page_access.json on the site), so
    // those tests exercise real content instead of skipping when locked.
    // Sourced from a system property first (-Dtest.username=...), then the
    // environment (TEST_VIEWER_USERNAME / TEST_VIEWER_PASSWORD as a CI secret).
    // Left blank, login is skipped and gated-page tests fall back to skipping.
    public static final String TEST_USERNAME = resolveCredential("test.username", "TEST_VIEWER_USERNAME");
    public static final String TEST_PASSWORD = resolveCredential("test.password", "TEST_VIEWER_PASSWORD");

    private static String resolveCredential(String systemProperty, String envVar) {
        String value = System.getProperty(systemProperty);
        if (value != null && !value.isBlank()) return value;
        value = System.getenv(envVar);
        return value != null ? value : "";
    }

    // Page URLs
    public static final String HOME_URL = BASE_URL + "/";
    public static final String LOGIN_URL = BASE_URL + "/login/";
    public static final String MARKET_OVERVIEW_URL = BASE_URL + "/market-overview/";
    public static final String NIFTY_URL = BASE_URL + "/nifty/today/";
    public static final String BANKNIFTY_URL = BASE_URL + "/indices/banknifty/";
    public static final String FINNIFTY_URL = BASE_URL + "/indices/finnifty/";
    public static final String SENSEX_URL = BASE_URL + "/indices/sensex/";
    public static final String GOLD_URL = BASE_URL + "/commodities/gold/";
    public static final String SILVER_URL = BASE_URL + "/commodities/silver/";
    public static final String CRUDE_OIL_URL = BASE_URL + "/commodities/crude-oil/";
    public static final String NATURAL_GAS_URL = BASE_URL + "/commodities/natural-gas/";
    public static final String NEWS_URL = BASE_URL + "/news/";
    public static final String METHODOLOGY_URL = BASE_URL + "/methodology/";
    public static final String EXPERT_VIEW_URL = BASE_URL + "/expert-view/";
    public static final String TELEGRAM_URL = BASE_URL + "/telegram/";
    public static final String CONTACT_URL = BASE_URL + "/contact/";
    public static final String DISCLAIMER_URL = BASE_URL + "/disclaimer/";

    // Signal states
    public static final String[] VALID_SIGNAL_STATES = {"Trade Signal", "Watchlist Only", "No Trade", "Hold", "Pending"};

    // Thresholds
    public static final double MIN_RISK_REWARD_RATIO = 2.2;
    public static final int MIN_CONFIDENCE_SCORE = 0;
    public static final int MAX_CONFIDENCE_SCORE = 100;
    public static final int MIDDAY_MIN_CONFIDENCE = 90;

    private TestConfig() {}
}
