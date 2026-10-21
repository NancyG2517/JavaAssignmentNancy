package com.swaglabs.pages;

import com.swaglabs.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class OverviewPage {

    private static final Logger logger = LoggerManager.getLogger(OverviewPage.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;

    private final By pageTitle           = By.cssSelector(".title");
    private final By cartItems           = By.cssSelector(".cart_item");
    private final By itemNames           = By.cssSelector(".inventory_item_name");
    private final By totalLabel          = By.cssSelector(".summary_total_label");
    private final By finishButton        = By.cssSelector("[data-test='finish']");

    private final By paymentInfoLabel  =
            By.xpath("//div[contains(@class,'summary_info_label') " +
                     "and contains(text(),'Payment')]");
    private final By shippingInfoLabel =
            By.xpath("//div[contains(@class,'summary_info_label') " +
                     "and contains(text(),'Shipping')]");

    public OverviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        logger.debug("DEBUG: OverviewPage object instantiated.");
    }

    public boolean isOverviewPageLoaded() {
        try {
            String title = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
            boolean loaded = "Checkout: Overview".equalsIgnoreCase(title.trim());
            logger.info("Overview page title: '" + title + "'. Loaded: " + loaded);
            if (!loaded) {
                logger.error("ERROR: Overview page title mismatch. Actual: '" + title + "'");
            }
            return loaded;
        } catch (Exception e) {
            logger.error("ERROR: Overview page not loaded: " + e.getMessage());
            return false;
        }
    }

    public int getOrderedItemCount() {
        List<WebElement> items = driver.findElements(cartItems);
        int count = items.size();
        logger.info("Overview: " + count + " item(s) in order summary.");
        return count;
    }

    public List<String> getOrderedItemNames() {
        List<WebElement> nameEls = driver.findElements(itemNames);
        List<String> names = new ArrayList<>();
        for (WebElement el : nameEls) {
            names.add(el.getText().trim());
        }
        logger.info("Overview item names: " + names);
        return names;
    }

    public boolean isPaymentInfoDisplayed() {
        try {
            boolean visible = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(paymentInfoLabel)).isDisplayed();
            logger.info("Payment Information section visible: " + visible);
            return visible;
        } catch (Exception e) {
            logger.error("ERROR: Payment Information section not found: " + e.getMessage());
            return false;
        }
    }

    public boolean isShippingInfoDisplayed() {
        try {
            boolean visible = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(shippingInfoLabel)).isDisplayed();
            logger.info("Shipping Information section visible: " + visible);
            return visible;
        } catch (Exception e) {
            logger.error("ERROR: Shipping Information section not found: " + e.getMessage());
            return false;
        }
    }

    public String getTotalAmount() {
        try {
            String total = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(totalLabel)).getText();
            logger.info("Total amount displayed: '" + total + "'");
            return total;
        } catch (Exception e) {
            logger.error("ERROR: Total label not found: " + e.getMessage());
            return "";
        }
    }

    public boolean isTotalDisplayed() {
        String total = getTotalAmount();
        boolean displayed = !total.isEmpty();
        if (!displayed) {
            logger.error("ERROR: Total amount label is empty or not visible.");
        }
        return displayed;
    }

    public void clickFinish() {
        logger.info("Clicking Finish button to place the order...");
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
        logger.info("Finish button clicked — order submitted.");
    }
}
