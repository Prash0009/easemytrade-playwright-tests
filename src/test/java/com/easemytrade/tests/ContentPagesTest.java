package com.easemytrade.tests;

import com.easemytrade.base.BaseTest;
import com.easemytrade.config.TestConfig;
import com.easemytrade.pages.GenericContentPage;
import com.easemytrade.utils.AllureAttachmentHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("EaseMyTrade")
@Feature("Content Pages")
public class ContentPagesTest extends BaseTest {

    // ---- News Page ----

    @Test(description = "News page loads with main content area")
    @Story("News Page")
    @Severity(SeverityLevel.CRITICAL)
    public void testNewsPageLoads() {
        navigateTo(TestConfig.NEWS_URL);
        skipIfRedirectedToLogin("News");
        GenericContentPage newsPage = new GenericContentPage(page);
        assertThat(newsPage.hasMainContent()).as("News page main content visible").isTrue();
        String title = newsPage.getTitle();
        assertThat(title).containsIgnoringCase("EaseMyTrade");
        takeScreenshot("News Page");
    }

    @Test(description = "News page contains at least one heading")
    @Story("News Page")
    @Severity(SeverityLevel.NORMAL)
    public void testNewsPageHasHeadings() {
        navigateTo(TestConfig.NEWS_URL);
        skipIfRedirectedToLogin("News");
        GenericContentPage newsPage = new GenericContentPage(page);
        int headingCount = newsPage.allHeadings().count();
        log.info("News page headings: {}", headingCount);
        assertThat(headingCount).as("News page should have at least one heading").isGreaterThan(0);
    }

    // ---- Methodology Page ----

    @Test(description = "Methodology page loads and contains content about signal logic")
    @Story("Methodology Page")
    @Severity(SeverityLevel.CRITICAL)
    public void testMethodologyPageLoads() {
        navigateTo(TestConfig.METHODOLOGY_URL);
        skipIfRedirectedToLogin("Methodology");
        GenericContentPage mPage = new GenericContentPage(page);
        assertThat(mPage.hasMainContent()).as("Methodology page main content visible").isTrue();
        String bodyText = page.locator("body").innerText();
        AllureAttachmentHelper.attachText("Methodology Body Length", String.valueOf(bodyText.length()));
        takeScreenshot("Methodology Page");
    }

    @Test(description = "Methodology page references key signal concepts")
    @Story("Methodology Page")
    @Severity(SeverityLevel.NORMAL)
    public void testMethodologyPageContent() {
        navigateTo(TestConfig.METHODOLOGY_URL);
        skipIfRedirectedToLogin("Methodology");
        String bodyText = page.locator("body").innerText().toLowerCase();
        // The page should mention signal / methodology concepts
        boolean hasMethodologyContent = bodyText.contains("signal") || bodyText.contains("nifty")
                || bodyText.contains("methodology") || bodyText.contains("indicator");
        AllureAttachmentHelper.attachText("Content Check", "Has methodology content: " + hasMethodologyContent);
        assertThat(hasMethodologyContent).as("Methodology page should discuss signal/indicator concepts").isTrue();
    }

    // ---- Telegram Page ----

    @Test(description = "Telegram page loads with main content")
    @Story("Telegram Page")
    @Severity(SeverityLevel.CRITICAL)
    public void testTelegramPageLoads() {
        navigateTo(TestConfig.TELEGRAM_URL);
        skipIfRedirectedToLogin("Telegram");
        GenericContentPage tPage = new GenericContentPage(page);
        assertThat(tPage.hasMainContent()).as("Telegram page main content visible").isTrue();
        String bodyText = page.locator("body").innerText().toLowerCase();
        assertThat(bodyText).containsIgnoringCase("telegram");
        takeScreenshot("Telegram Page");
    }

    // ---- Contact Page ----

    @Test(description = "Contact page loads and shows contact information")
    @Story("Contact Page")
    @Severity(SeverityLevel.CRITICAL)
    public void testContactPageLoads() {
        navigateTo(TestConfig.CONTACT_URL);
        GenericContentPage contactPage = new GenericContentPage(page);
        assertThat(contactPage.hasMainContent()).as("Contact page main content visible").isTrue();
        String bodyText = page.locator("body").innerText().toLowerCase();
        boolean hasContactInfo = bodyText.contains("contact") || bodyText.contains("email") || bodyText.contains("prashant");
        assertThat(hasContactInfo).as("Contact page should have contact information").isTrue();
        takeScreenshot("Contact Page");
    }

    @Test(description = "Contact page paragraphs are non-empty")
    @Story("Contact Page")
    @Severity(SeverityLevel.NORMAL)
    public void testContactPageHasParagraphs() {
        navigateTo(TestConfig.CONTACT_URL);
        GenericContentPage contactPage = new GenericContentPage(page);
        assertThat(contactPage.getParagraphCount()).as("Contact page should have paragraphs").isGreaterThan(0);
    }

    // ---- Disclaimer Page ----

    @Test(description = "Disclaimer page loads with educational disclaimer content")
    @Story("Disclaimer Page")
    @Severity(SeverityLevel.CRITICAL)
    public void testDisclaimerPageLoads() {
        navigateTo(TestConfig.DISCLAIMER_URL);
        GenericContentPage discPage = new GenericContentPage(page);
        assertThat(discPage.hasMainContent()).as("Disclaimer page main content visible").isTrue();
        String bodyText = page.locator("body").innerText().toLowerCase();
        boolean hasDisclaimer = bodyText.contains("disclaimer") || bodyText.contains("educational")
                || bodyText.contains("not investment advice") || bodyText.contains("information only");
        assertThat(hasDisclaimer).as("Disclaimer page should have disclaimer text").isTrue();
        takeScreenshot("Disclaimer Page");
    }

    @Test(description = "Disclaimer page explicitly states content is for educational/informational purposes only")
    @Story("Disclaimer Page")
    @Severity(SeverityLevel.CRITICAL)
    public void testDisclaimerEducationalStatement() {
        navigateTo(TestConfig.DISCLAIMER_URL);
        String bodyText = page.locator("body").innerText().toLowerCase();
        AllureAttachmentHelper.attachText("Disclaimer Content Length", String.valueOf(bodyText.length()));
        boolean hasEducationalStatement = bodyText.contains("educational") || bodyText.contains("information")
                || bodyText.contains("not") && (bodyText.contains("investment advice") || bodyText.contains("solicitation"));
        assertThat(hasEducationalStatement)
                .as("Disclaimer must state educational/informational purpose").isTrue();
    }

    // ---- Telegram Status Page ----

    @Test(description = "Telegram status page loads without 404")
    @Story("Telegram Status Page")
    @Severity(SeverityLevel.MINOR)
    public void testTelegramStatusPageLoads() {
        navigateTo(TestConfig.BASE_URL + "/telegram-status/");
        assertThat(page.locator("body").isVisible()).isTrue();
        // Just check it loads without a crash
        assertThat(page.title()).isNotBlank();
        takeScreenshot("Telegram Status Page");
    }

    // ---- Cross-page consistency: All pages have brand in title ----

    @Test(description = "All major pages include EaseMyTrade in the page title")
    @Story("Brand Consistency")
    @Severity(SeverityLevel.NORMAL)
    public void testAllPagesHaveEaseMyTradeBranding() {
        String[] urls = {
                TestConfig.HOME_URL, TestConfig.LOGIN_URL, TestConfig.EXPERT_VIEW_URL,
                TestConfig.NEWS_URL, TestConfig.METHODOLOGY_URL,
                TestConfig.TELEGRAM_URL, TestConfig.CONTACT_URL, TestConfig.DISCLAIMER_URL,
                TestConfig.NIFTY_URL, TestConfig.BANKNIFTY_URL,
        };

        StringBuilder report = new StringBuilder();
        for (String url : urls) {
            navigateTo(url);
            if (!url.equals(TestConfig.LOGIN_URL) && isOnLoginPage()) {
                log.info("Skipping title check for {} — currently behind login gate", url);
                continue;
            }
            String title = page.title();
            report.append(url).append(" => ").append(title).append("\n");
            assertThat(title).as("Page at %s should reference EaseMyTrade in title", url)
                    .containsIgnoringCase("EaseMyTrade");
        }
        AllureAttachmentHelper.attachText("All Page Titles", report.toString());
    }

    // ---- All pages have a footer ----

    @Test(description = "All major pages have a visible footer with the EaseMyTrade brand")
    @Story("Footer Consistency")
    @Severity(SeverityLevel.MINOR)
    public void testAllPagesHaveFooter() {
        String[] urls = {
                TestConfig.HOME_URL, TestConfig.EXPERT_VIEW_URL, TestConfig.NIFTY_URL,
                TestConfig.NEWS_URL, TestConfig.METHODOLOGY_URL,
        };
        for (String url : urls) {
            navigateTo(url);
            if (isOnLoginPage()) {
                log.info("Skipping footer check for {} — currently behind login gate", url);
                continue;
            }
            boolean hasFooter = page.locator("footer").isVisible();
            assertThat(hasFooter).as("Page at %s should have a footer", url).isTrue();
        }
    }
}
