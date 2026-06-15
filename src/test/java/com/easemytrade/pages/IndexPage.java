package com.easemytrade.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Page object for individual index pages: NIFTY, BANKNIFTY, FINNIFTY, SENSEX.
 * These pages share the same DOM structure with different element IDs.
 */
public class IndexPage extends BasePage {

    private final String indexKey; // e.g. "nifty", "banknifty", "finnifty", "sensex"

    public IndexPage(Page page, String indexKey) {
        super(page);
        this.indexKey = indexKey.toLowerCase();
    }

    // --- Signal card (the main signal display) ---

    public Locator signalSection() { return page.locator(".signal-section, .signal-card, [data-section='signal'], .content-band").first(); }
    public Locator signalStateLabel() { return page.locator(".signal-state, .signal-status, [data-signal-state]").first(); }
    public Locator confidenceLabel() { return page.locator(".confidence-label, [data-confidence], .signal-confidence").first(); }

    // Generic text extraction by common element selectors
    public String getSignalState() {
        // Try multiple selectors used across index pages
        for (String sel : new String[]{"[data-signal-state]", ".signal-state", ".signal-badge", ".status-badge", ".trade-state"}) {
            try {
                Locator l = page.locator(sel).first();
                if (l.isVisible()) return l.innerText().trim();
            } catch (Exception ignored) {}
        }
        return "";
    }

    public String getConfidenceText() {
        for (String sel : new String[]{"[data-confidence]", ".confidence-value", ".confidence-label"}) {
            try {
                Locator l = page.locator(sel).first();
                if (l.isVisible()) return l.innerText().trim();
            } catch (Exception ignored) {}
        }
        return "";
    }

    // --- Zone / support / resistance ---

    public Locator zoneSection() { return page.locator(".zone-card, .trading-zones-band, .zone-section").first(); }

    public String getSupportText() {
        for (String sel : new String[]{"[data-support]", ".support-level", ".zone-support strong", ".support strong"}) {
            try {
                Locator l = page.locator(sel).first();
                if (l.isVisible()) return l.innerText().trim();
            } catch (Exception ignored) {}
        }
        // Fallback: look for labels followed by numbers
        try { return page.locator(".zone-levels div:first-child strong").first().innerText().trim(); }
        catch (Exception e) { return ""; }
    }

    public String getResistanceText() {
        for (String sel : new String[]{"[data-resistance]", ".resistance-level", ".zone-resistance strong", ".resistance strong"}) {
            try {
                Locator l = page.locator(sel).first();
                if (l.isVisible()) return l.innerText().trim();
            } catch (Exception ignored) {}
        }
        try { return page.locator(".zone-levels div:last-child strong").first().innerText().trim(); }
        catch (Exception e) { return ""; }
    }

    // --- Completed trade review cards ---

    public Locator completedTradeCards() { return page.locator(".trade-review-card, .completed-trade, .trade-card, .profitReview"); }
    public int completedTradeCardCount() {
        try { return completedTradeCards().count(); }
        catch (Exception e) { return 0; }
    }

    // --- Page structure checks ---

    public Locator mainContent() { return page.locator("main"); }
    public Locator pageHeader() { return page.locator("h1, h2").first(); }

    public boolean hasSignalContent() {
        return isVisible(page.locator("main"));
    }

    public String getPageHeading() {
        try { return page.locator("h1").first().innerText().trim(); }
        catch (Exception e) { return ""; }
    }

    // --- Entry / stop / target (if shown on index page) ---

    public String getEntryLevel() {
        for (String sel : new String[]{"[data-entry]", ".entry-level", ".signal-entry"}) {
            try {
                Locator l = page.locator(sel).first();
                if (l.isVisible()) return l.innerText().trim();
            } catch (Exception ignored) {}
        }
        return "";
    }

    public String getStopLossLevel() {
        for (String sel : new String[]{"[data-stop]", ".stop-level", ".signal-stop", ".stoploss"}) {
            try {
                Locator l = page.locator(sel).first();
                if (l.isVisible()) return l.innerText().trim();
            } catch (Exception ignored) {}
        }
        return "";
    }

    public String getTargetLevel() {
        for (String sel : new String[]{"[data-target]", ".target-level", ".signal-target"}) {
            try {
                Locator l = page.locator(sel).first();
                if (l.isVisible()) return l.innerText().trim();
            } catch (Exception ignored) {}
        }
        return "";
    }
}
