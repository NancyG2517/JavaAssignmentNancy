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

public class CartPage {

    private static final Logger logger = LoggerManager.getLogger(CartPage.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;

    private final By pageTitle       = By.cssSelector(".title");
    private final By cartItems       = By.cssSelector(".cart_item");
    private final By itemNames       = By.cssSelector(".inventory_item_name");
    private final By checkoutButton  = By.cssSelector("[data-test='checkout']");
    private final By continueShop    = By.cssSelector("[data-test='continue-shopping']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        logger.debug("DEBUG: CartPage object instantiated.");
    }

    public String getPageTitle() {
        String title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
        logger.info("Cart page title: '" + title + "'");
        return title;
    }

    public boolean isCartPageLoaded() {
        try {
            String title = getPageTitle();
            boolean loaded = "Your Cart".equalsIgnoreCase(title.trim());
            if (loaded) {
                logger.info("Cart page confirmed loaded.");
            } else {
                logger.error("ERROR: Cart page title mismatch. " +
                        "Expected='Your Cart', Actual='" + title + "'");
            }
            return loaded;
        } catch (Exception e) {
            logger.error("ERROR: Could not verify cart page load: " + e.getMessage());
            return false;
        }
    }

    public int getCartItemCount() {
        List<WebElement> items = driver.findElements(cartItems);
        int count = items.size();
        logger.info("Cart item count: " + count);
        if (count == 0) {
            logger.warn("WARN: Cart is empty — no items found.");
        }
        return count;
    }

    public List<String> getCartItemNames() {
        List<WebElement> nameElements = driver.findElements(itemNames);
        List<String> names = new ArrayList<>();
        for (WebElement el : nameElements) {
            names.add(el.getText().trim());
        }
        logger.info("Cart items: " + names);
        return names;
    }

    public boolean isProductInCart(String productName) {
        List<String> cartNames = getCartItemNames();
        boolean found = cartNames.stream()
                .anyMatch(name -> name.equalsIgnoreCase(productName.trim()));

        if (found) {
            logger.info("Product found in cart: '" + productName + "'");
        } else {
            logger.error("ERROR: Product NOT found in cart: '" + productName +
                    "'. Cart contains: " + cartNames);
        }
        return found;
    }

    public void clickCheckout() {
        logger.info("Clicking Checkout button...");
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        logger.info("Checkout button clicked — navigating to checkout information page.");
    }
}
