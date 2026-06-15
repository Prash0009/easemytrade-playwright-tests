package com.easemytrade.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class NewsPage extends BasePage {

    public NewsPage(Page page) { super(page); }

    public Locator pageHeading() { return page.locator("h1, h2").first(); }
    public Locator newsItems() { return page.locator(".news-item, article.news-card, .news-entry, [data-news-item]"); }
    public Locator domesticNewsSection() { return page.locator("[data-section='domestic'], .news-domestic, #domesticNews"); }
    public Locator globalNewsSection() { return page.locator("[data-section='global'], .news-global, #globalNews"); }
    public Locator newsLinks() { return page.locator("a[href*='http']:not([href*='easemytrade'])"); }
    public Locator loadingIndicator() { return page.locator(".loading, .spinner, [aria-busy='true']"); }

    public int getNewsItemCount() {
        try { return newsItems().count(); }
        catch (Exception e) { return 0; }
    }

    public boolean hasNewsContent() {
        return isVisible(page.locator("main"));
    }
}
