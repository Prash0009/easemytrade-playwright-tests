package com.easemytrade.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Generic page object for simple content pages: Methodology, Telegram, Contact, Disclaimer.
 */
public class GenericContentPage extends BasePage {

    public GenericContentPage(Page page) { super(page); }

    public Locator pageHeading() { return page.locator("h1").first(); }
    public Locator mainContent() { return page.locator("main"); }
    public Locator allHeadings() { return page.locator("h1, h2, h3"); }
    public Locator allLinks() { return page.locator("main a"); }
    public Locator allParagraphs() { return page.locator("main p"); }

    public boolean hasMainContent() { return isVisible(mainContent()); }
    public String getHeadingText() {
        try { return pageHeading().innerText().trim(); }
        catch (Exception e) { return ""; }
    }
    public int getLinkCount() {
        try { return allLinks().count(); }
        catch (Exception e) { return 0; }
    }
    public int getParagraphCount() {
        try { return allParagraphs().count(); }
        catch (Exception e) { return 0; }
    }
}
