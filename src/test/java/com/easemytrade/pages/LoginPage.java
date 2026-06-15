package com.easemytrade.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class LoginPage extends BasePage {

    public LoginPage(Page page) { super(page); }

    // ── Locators ──────────────────────────────────────────────────────────────

    public Locator loginForm()       { return page.locator("#loginForm"); }
    public Locator usernameInput()   { return page.locator("#username"); }
    public Locator passwordInput()   { return page.locator("#password"); }
    public Locator togglePasswordBtn(){ return page.locator("#togglePassword"); }
    public Locator signInBtn()       { return page.locator("button[type='submit']"); }
    public Locator signOutBtn()      { return page.locator("#logoutButton"); }
    public Locator statusMessage()   { return page.locator("#status"); }
    public Locator brandLogo()       { return page.locator(".brand"); }
    public Locator heroHeading()     { return page.locator(".hero h1"); }

    // ── Actions ───────────────────────────────────────────────────────────────

    @Step("Fill username field with: {username}")
    public void fillUsername(String username) {
        log.debug("  [ACTION] fillUsername('{}')", username);
        usernameInput().fill(username);
    }

    @Step("Fill password field")
    public void fillPassword(String password) {
        log.debug("  [ACTION] fillPassword(***)");
        passwordInput().fill(password);
    }

    @Step("Click 'Sign In' button")
    public void clickSignIn() {
        log.debug("  [ACTION] clickSignIn");
        signInBtn().click();
    }

    @Step("Login with username: {username}")
    public void login(String username, String password) {
        log.debug("  [ACTION] login('{}', ***)", username);
        fillUsername(username);
        fillPassword(password);
        clickSignIn();
    }

    @Step("Toggle password visibility")
    public void togglePasswordVisibility() {
        log.debug("  [ACTION] togglePasswordVisibility");
        togglePasswordBtn().click();
    }

    // ── State readers ─────────────────────────────────────────────────────────

    public String getStatusText() {
        try {
            String text = statusMessage().innerText().trim();
            log.debug("  [STATE] status message = '{}'", text);
            return text;
        } catch (Exception e) {
            log.debug("  [STATE] status message not found: {}", e.getMessage());
            return "";
        }
    }

    public boolean isStatusError() {
        try {
            boolean err = statusMessage().getAttribute("class").contains("is-error");
            log.debug("  [STATE] isStatusError = {}", err);
            return err;
        } catch (Exception e) { return false; }
    }

    public boolean isStatusSuccess() {
        try {
            boolean ok = statusMessage().getAttribute("class").contains("is-success");
            log.debug("  [STATE] isStatusSuccess = {}", ok);
            return ok;
        } catch (Exception e) { return false; }
    }

    public boolean isPasswordHidden() {
        try {
            boolean hidden = "password".equals(passwordInput().getAttribute("type"));
            log.debug("  [STATE] isPasswordHidden = {}", hidden);
            return hidden;
        } catch (Exception e) { return true; }
    }
}
