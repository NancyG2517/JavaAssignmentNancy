package com.swaglabs.pages;

import com.swaglabs.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CompletePage {

    private static final Logger logger = LoggerManager.getLogger(CompletePage.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;

    private final By confirmationHeader = By.cssSelector(".complete-header");
    private final By confirmationText   = By.cssSelector(".complete-text");
    private final By backHomeButton     = By.cssSelector("[data-test='back-to-products']");

    public CompletePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        logger.debug("DEBUG: CompletePage object instantiated.");
    }

    public boolean isCompletePageLoaded() {
        try {
            boolean visible = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(confirmationHeader)).isDisplayed();
            logger.info("Complete page loaded: " + visible);
            return visible;
        } catch (Exception e) {
            logger.error("ERROR: Complete page not loaded: " + e.getMessage());
            return false;
        }
    }

    public String getConfirmationMessage() {
        try {
            String message = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(confirmationHeader)).getText();
            logger.info("Order confirmation header: '" + message + "'");
            return message;
        } catch (Exception e) {
            logger.error("ERROR: Confirmation header element not found: " + e.getMessage());
            return "";
        }
    }
    public boolean isOrderConfirmed() {
        String message = getConfirmationMessage();
        boolean confirmed = message.toLowerCase().contains("thank you");
        if (confirmed) {
            logger.info("Order confirmed successfully. Header: '" + message + "'");
        } else {
            logger.error("ERROR: Confirmation message does not contain 'thank you'. " +
                    "Actual: '" + message + "'");
        }
        return confirmed;
    }

    public String getCompletionSubText() {
        try {
            String text = driver.findElement(confirmationText).getText();
            logger.debug("DEBUG: Completion sub-text: '" + text + "'");
            return text;
        } catch (Exception e) {
            logger.warn("WARN: Could not read completion sub-text: " + e.getMessage());
            return "";
        }
    }

    public void clickBackHome() {
        logger.info("Clicking 'Back Home' button...");
        wait.until(ExpectedConditions.elementToBeClickable(backHomeButton)).click();
        logger.info("'Back Home' clicked — returning to Products page.");
    }
}
