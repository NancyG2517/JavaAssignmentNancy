package com.swaglabs.pages;

import com.swaglabs.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private static final Logger logger = LoggerManager.getLogger(LoginPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameField  = By.id("user-name");
    private final By passwordField  = By.id("password");
    private final By loginButton    = By.id("login-button");
    private final By errorMessage   = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        logger.debug("DEBUG: LoginPage object instantiated.");
    }

    public void enterUsername(String username) {
        logger.info("Entering username: '" + username + "' into the username field");
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(usernameField));
        field.clear();
        field.sendKeys(username);
        logger.debug("DEBUG: Username field populated successfully.");
    }

    public void enterPassword(String password) {
        logger.info("Entering password: [HIDDEN] into the password field");
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField));
        field.clear();
        field.sendKeys(password);
        logger.debug("DEBUG: Password field populated successfully.");
    }

    public void clickLoginButton() {
        logger.info("Clicking the Login button...");
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        logger.info("Login button clicked — waiting for page transition.");
    }

    public void login(String username, String password) {
        logger.info("--- Login sequence started ---");
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        logger.info("--- Login sequence completed ---");
    }

    public String getErrorMessage() {
        try {
            WebElement errorEl = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(errorMessage));
            String text = errorEl.getText();
            logger.warn("WARN: Error message visible on login page: '" + text + "'");
            return text;
        } catch (Exception e) {
            logger.error("ERROR: Attempted to read error message but element was not found. " +
                    "Exception: " + e.getClass().getSimpleName());
            return "";
        }
    }

    public boolean isErrorDisplayed() {
        try {
            boolean visible = driver.findElement(errorMessage).isDisplayed();
            if (visible) {
                logger.warn("WARN: isErrorDisplayed() = true — login was rejected.");
            } else {
                logger.debug("DEBUG: isErrorDisplayed() = false — error element exists but is hidden.");
            }
            return visible;
        } catch (Exception e) {

            logger.debug("DEBUG: isErrorDisplayed() = false — error element not present in DOM.");
            return false;
        }
    }

    public boolean isLoginPageDisplayed() {
        try {
            return driver.findElement(usernameField).isDisplayed();
        } catch (Exception e) {
            logger.error("ERROR: Login page elements not found — not on login page?");
            return false;
        }
    }
}
