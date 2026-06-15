package com.easemytrade.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    public LoginPage(Page page) { super(page); }

    public Locator loginForm() { return page.locator("#loginForm"); }
    public Locator usernameInput() { return page.locator("#username"); }
    public Locator passwordInput() { return page.locator("#password"); }
    public Locator togglePasswordBtn() { return page.locator("#togglePassword"); }
    public Locator signInBtn() { return page.locator("button[type='submit']"); }
    public Locator signOutBtn() { return page.locator("#logoutButton"); }
    public Locator statusMessage() { return page.locator("#status"); }
    public Locator brandLogo() { return page.locator(".brand"); }
    public Locator heroHeading() { return page.locator(".hero h1"); }

    public void fillUsername(String username) { usernameInput().fill(username); }
    public void fillPassword(String password) { passwordInput().fill(password); }
    public void clickSignIn() { signInBtn().click(); }

    public void login(String username, String password) {
        fillUsername(username);
        fillPassword(password);
        clickSignIn();
    }

    public String getStatusText() {
        try { return statusMessage().innerText().trim(); }
        catch (Exception e) { return ""; }
    }

    public boolean isStatusError() {
        try { return statusMessage().getAttribute("class").contains("is-error"); }
        catch (Exception e) { return false; }
    }

    public boolean isStatusSuccess() {
        try { return statusMessage().getAttribute("class").contains("is-success"); }
        catch (Exception e) { return false; }
    }

    public boolean isPasswordHidden() {
        try { return "password".equals(passwordInput().getAttribute("type")); }
        catch (Exception e) { return true; }
    }

    public void togglePasswordVisibility() { togglePasswordBtn().click(); }
}
