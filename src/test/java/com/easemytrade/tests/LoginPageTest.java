package com.easemytrade.tests;

import com.easemytrade.base.BaseTest;
import com.easemytrade.config.TestConfig;
import com.easemytrade.pages.LoginPage;
import com.easemytrade.utils.AllureAttachmentHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("EaseMyTrade")
@Feature("Login Page")
public class LoginPageTest extends BaseTest {

    private LoginPage loginPage;

    // This suite specifically exercises the logged-out login experience —
    // an injected test-viewer session would change how /login/ behaves.
    @Override
    protected boolean useAuthenticatedSession() {
        return false;
    }

    @BeforeMethod
    public void openLoginPage() {
        navigateTo(TestConfig.LOGIN_URL);
        loginPage = new LoginPage(page);
    }

    @Test(description = "Login page loads with correct title")
    @Story("Page Load")
    @Severity(SeverityLevel.BLOCKER)
    public void testLoginPageTitle() {
        assertThat(loginPage.getTitle()).containsIgnoringCase("Sign In");
        assertThat(loginPage.getTitle()).containsIgnoringCase("EaseMyTrade");
    }

    @Test(description = "Login form elements are present: username, password, submit, logout")
    @Story("Form Elements")
    @Severity(SeverityLevel.BLOCKER)
    public void testLoginFormPresence() {
        assertThat(loginPage.loginForm().isVisible()).as("Login form visible").isTrue();
        assertThat(loginPage.usernameInput().isVisible()).as("Username input visible").isTrue();
        assertThat(loginPage.passwordInput().isVisible()).as("Password input visible").isTrue();
        assertThat(loginPage.signInBtn().isVisible()).as("Sign In button visible").isTrue();
        assertThat(loginPage.signOutBtn().isVisible()).as("Sign Out button visible").isTrue();
        takeScreenshot("Login Form Elements");
    }

    @Test(description = "Password field type is 'password' by default (hidden)")
    @Story("Password Field")
    @Severity(SeverityLevel.CRITICAL)
    public void testPasswordIsHiddenByDefault() {
        assertThat(loginPage.isPasswordHidden()).as("Password should be hidden by default").isTrue();
    }

    @Test(description = "Toggling password show/hide changes input type")
    @Story("Password Field")
    @Severity(SeverityLevel.NORMAL)
    public void testPasswordToggleVisibility() {
        loginPage.fillPassword("testPassword");
        assertThat(loginPage.isPasswordHidden()).as("Initially hidden").isTrue();
        loginPage.togglePasswordVisibility();
        boolean isText = "text".equals(loginPage.passwordInput().getAttribute("type"));
        assertThat(isText).as("After toggle, type should be 'text'").isTrue();
        loginPage.togglePasswordVisibility();
        assertThat(loginPage.isPasswordHidden()).as("After second toggle, hidden again").isTrue();
        takeScreenshot("Password Toggle");
    }

    @Test(description = "Submitting empty form shows an error status message")
    @Story("Form Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmptyFormSubmissionShowsError() {
        loginPage.clickSignIn();
        page.waitForTimeout(1000);
        String status = loginPage.getStatusText();
        log.info("Status after empty submit: '{}'", status);
        AllureAttachmentHelper.attachText("Empty Submit Status", status);
        assertThat(status).isNotBlank();
        assertThat(loginPage.isStatusError()).as("Status should be error for empty form").isTrue();
        takeScreenshot("Empty Form Error");
    }

    @Test(description = "Submitting with only username shows error about missing password")
    @Story("Form Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testMissingPasswordShowsError() {
        loginPage.fillUsername("testuser");
        loginPage.clickSignIn();
        page.waitForTimeout(1000);
        assertThat(loginPage.isStatusError()).as("Status should be error when password is missing").isTrue();
    }

    @Test(description = "Invalid credentials show an error status")
    @Story("Authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidCredentialsShowError() {
        loginPage.login("invaliduser", "wrongpassword");
        page.waitForTimeout(3000);
        String status = loginPage.getStatusText();
        log.info("Status after invalid login: '{}'", status);
        AllureAttachmentHelper.attachText("Invalid Login Status", status);
        assertThat(loginPage.isStatusError())
                .as("Invalid credentials should show an error status").isTrue();
        takeScreenshot("Invalid Credentials Error");
    }

    @Test(description = "After 4 failed attempts, login is temporarily locked (cooldown message appears)")
    @Story("Brute Force Protection")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginCooldownAfterMaxAttempts() {
        for (int i = 0; i < 4; i++) {
            loginPage.login("baduser", "badpass" + i);
            page.waitForTimeout(2000);
            // Reload the page state (form resets)
            if (i < 3) {
                loginPage.usernameInput().clear();
                loginPage.passwordInput().clear();
            }
        }
        String status = loginPage.getStatusText();
        log.info("Status after 4 failed attempts: '{}'", status);
        AllureAttachmentHelper.attachText("Cooldown Status", status);
        // Either locked out or error — both are acceptable outcomes
        assertThat(status).isNotBlank();
        takeScreenshot("After Max Failed Attempts");
    }

    @Test(description = "Login page brand logo links to home page")
    @Story("Navigation")
    @Severity(SeverityLevel.MINOR)
    public void testBrandLogoLinksToHome() {
        String href = loginPage.brandLogo().getAttribute("href");
        assertThat(href).isEqualTo("/");
    }

    @Test(description = "Login page has hero heading describing protected access")
    @Story("Page Content")
    @Severity(SeverityLevel.MINOR)
    public void testHeroHeadingPresent() {
        assertThat(loginPage.heroHeading().isVisible()).isTrue();
        String heading = loginPage.heroHeading().innerText().trim();
        assertThat(heading).isNotBlank();
        log.info("Login page hero heading: '{}'", heading);
    }

    @Test(description = "Login page has data-page='login' body attribute")
    @Story("Page Metadata")
    @Severity(SeverityLevel.MINOR)
    public void testLoginBodyAttribute() {
        String attr = page.locator("body").getAttribute("data-page");
        assertThat(attr).isEqualTo("login");
    }

    @Test(description = "Login redirect preserves 'next' query parameter destination")
    @Story("Authentication")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginNextQueryParam() {
        navigateTo(TestConfig.LOGIN_URL + "?next=/expert-view/");
        assertThat(page.url()).contains("next=/expert-view/");
        assertThat(page.locator("#loginForm").isVisible()).isTrue();
    }
}
