package com.easemytrade.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class AllureAttachmentHelper {

    private static final Logger log = LoggerFactory.getLogger(AllureAttachmentHelper.class);

    public static void attachScreenshot(Page page, String name) {
        try {
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
        } catch (Exception e) {
            log.warn("Could not capture screenshot: {}", e.getMessage());
        }
    }

    public static void attachPageSource(Page page, String name) {
        try {
            String content = page.content();
            Allure.addAttachment(name, "text/html",
                    new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), ".html");
        } catch (Exception e) {
            log.warn("Could not capture page source: {}", e.getMessage());
        }
    }

    public static void addPageInfo(String url, String title) {
        Allure.addAttachment("Page Info", "text/plain",
                new ByteArrayInputStream(("URL: " + url + "\nTitle: " + title).getBytes(StandardCharsets.UTF_8)), ".txt");
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain",
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), ".txt");
    }

    public static void attachJson(String name, String json) {
        Allure.addAttachment(name, "application/json",
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)), ".json");
    }

    private AllureAttachmentHelper() {}
}
