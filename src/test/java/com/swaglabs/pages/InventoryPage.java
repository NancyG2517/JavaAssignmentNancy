package com.swaglabs.pages;

import com.swaglabs.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class InventoryPage {

    private static final Logger logger = LoggerManager.getLogger(InventoryPage.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;

    private final By pageTitle        = By.cssSelector(".title");
    private final By menuButton       = By.id("react-burger-menu-btn");
    private final By logoutLink       = By.id("logout_sidebar_link");
    private final By inventoryItems   = By.cssSelector(".inventory_item");
    private final By cartBadge        = By.cssSelector(".shopping_cart_badge");
    private final By cartIcon         = By.cssSelector(".shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        logger.debug("DEBUG: InventoryPage object instantiated.");
    }

    public String getPageTitle() {
        logger.debug("DEBUG: Retrieving inventory page title...");
        String title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
        logger.info("Inventory page title = '" + title + "'");
        return title;
    }

    public boolean isInventoryPageLoaded() {
        try {
            String title = getPageTitle();
            boolean loaded = "Products".equalsIgnoreCase(title.trim());

            if (loaded) {
                logger.info("Inventory page confirmed loaded. Title = '" + title + "'");
            } else {
                logger.error("ERROR: Inventory page title mismatch. " +
                        "Expected='Products', Actual='" + title + "'");
            }
            return loaded;

        } catch (Exception e) {
            logger.error("ERROR: Could not verify inventory page load. " +
                    e.getClass().getSimpleName() + ": " + e.getMessage());
            return false;
        }
    }

    public int getInventoryItemCount() {
        List<WebElement> items = driver.findElements(inventoryItems);
        int count = items.size();
        logger.info("Inventory item count = " + count);
        if (count == 0) {
            logger.warn("WARN: No inventory items found — page may not have loaded correctly.");
        }
        return count;
    }

    public void clickMenuButton() {
        logger.info("Opening hamburger menu (sidebar)...");
        wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
        logger.debug("DEBUG: Hamburger menu button clicked.");
    }

    public void clickLogoutLink() {
        logger.info("Clicking Logout link in sidebar...");
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        logger.info("Logout link clicked — browser should redirect to login page.");
    }

    public void logout() {
        logger.info("=== Logout sequence started ===");
        clickMenuButton();
        clickLogoutLink();
        logger.info("=== Logout sequence completed ===");
    }

    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.debug("DEBUG: getCurrentUrl() = " + url);
        return url;
    }

    public String getCartBadgeCount() {
        try {
            String count = driver.findElement(cartBadge).getText();
            logger.info("Shopping cart badge count = " + count);
            return count;
        } catch (Exception e) {
            logger.debug("DEBUG: Cart badge not visible — cart is empty.");
            return "0";
        }
    }

    public String getProductNameByIndex(int index) {
        List<WebElement> items = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));
        String name = items.get(index)
                .findElement(By.cssSelector(".inventory_item_name"))
                .getText().trim();
        logger.info("Product at index " + index + ": '" + name + "'");
        return name;
    }

    public void addProductToCartByIndex(int index) {
        List<WebElement> items = driver.findElements(inventoryItems);
        WebElement item = items.get(index);
        String productName = item.findElement(By.cssSelector(".inventory_item_name")).getText().trim();
        logger.info("Adding product to cart: '" + productName + "' (index " + index + ")");
        item.findElement(By.cssSelector("button[data-test^='add-to-cart']")).click();
        logger.info("'" + productName + "' added to cart successfully.");
    }

    public void clickCartIcon() {
        logger.info("Clicking shopping cart icon...");
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
        logger.info("Cart icon clicked — navigating to cart page.");
    }
}