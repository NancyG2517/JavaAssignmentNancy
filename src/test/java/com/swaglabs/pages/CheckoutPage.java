package com.swaglabs.pages;

import com.swaglabs.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {

    private static final Logger logger = LoggerManager.getLogger(CheckoutPage.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;

    private final By pageTitle       = By.cssSelector(".title");
    private final By firstNameField  = By.cssSelector("[data-test='firstName']");
    private final By lastNameField   = By.cssSelector("[data-test='lastName']");
    private final By postalCodeField = By.cssSelector("[data-test='postalCode']");
    private final By continueButton  = By.cssSelector("[data-test='continue']");
    private final By errorMessage    = By.cssSelector("[data-test='error']");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        logger.debug("DEBUG: CheckoutPage object instantiated.");
    }

    public boolean isCheckoutPageLoaded() {
        try {
            String title = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
            boolean loaded = "Checkout: Your Information".equalsIgnoreCase(title.trim());
            logger.info("Checkout page title: '" + title + "'. Loaded: " + loaded);
            if (!loaded) {
                logger.error("ERROR: Checkout page title mismatch. Actual: '" + title + "'");
            }
            return loaded;
        } catch (Exception e) {
            logger.error("ERROR: Checkout page not loaded: " + e.getMessage());
            return false;
        }
    }

    public void enterFirstName(String firstName) {
        logger.info("Entering First Name: '" + firstName + "'");
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(firstNameField));
        field.clear();
        field.sendKeys(firstName);
        logger.debug("DEBUG: First name entered successfully.");
    }

    public void enterLastName(String lastName) {
        logger.info("Entering Last Name: '" + lastName + "'");
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(lastNameField));
        field.clear();
        field.sendKeys(lastName);
        logger.debug("DEBUG: Last name entered successfully.");
    }

    public void enterPostalCode(String postalCode) {
        logger.info("Entering Postal Code: '" + postalCode + "'");
        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(postalCodeField));
        field.clear();
        field.sendKeys(postalCode);
        logger.debug("DEBUG: Postal code entered successfully.");
    }

    public void fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        logger.info("=== Filling Checkout Information Form ===");
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        logger.info("=== Checkout Information Form Filled ===");
    }

    public void clickContinue() {
        logger.info("Clicking Continue button on checkout form...");
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
        logger.info("Continue button clicked — proceeding to Checkout Overview.");
    }

    public boolean isErrorDisplayed() {
        try {
            boolean visible = driver.findElement(errorMessage).isDisplayed();
            if (visible) {
                logger.warn("WARN: Checkout form error message is displayed.");
            }
            return visible;
        } catch (Exception e) {
            return false;
        }
    }
}
