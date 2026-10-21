package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.pages.CartPage;
import com.swaglabs.pages.CheckoutPage;
import com.swaglabs.pages.CompletePage;
import com.swaglabs.pages.InventoryPage;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.OverviewPage;
import com.swaglabs.utilities.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class E2EPurchaseFlowTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(E2EPurchaseFlowTest.class);

    @Test(description = "E2E: Login → Add 2 Products → Cart → Checkout → Overview → " +
                        "Complete → Back Home → Logout")
    public void testCompletePurchaseFlow() {

        logger.info("======================================================");
        logger.info("  E2E TEST: testCompletePurchaseFlow — START");
        logger.info("======================================================");

        logger.info("STEP 1-2: Logging in with valid credentials...");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
                ConfigReader.getProperty("username"),
                ConfigReader.getProperty("password")
        );

        logger.info("STEP 1-2 DONE: Login sequence completed.");

        logger.info("STEP 3: Verifying the Products page is displayed...");

        InventoryPage inventoryPage = new InventoryPage(driver);
        boolean inventoryLoaded = inventoryPage.isInventoryPageLoaded();

        Assert.assertTrue(inventoryLoaded,
                "STEP 3 FAILED: Products page did not load after login. " +
                "URL: " + driver.getCurrentUrl());

        logger.info("STEP 3 PASSED: Products page confirmed. Title = 'Products'.");

        logger.info("STEP 4: Reading product names then adding two products to cart...");

        String product1Name = inventoryPage.getProductNameByIndex(0);
        String product2Name = inventoryPage.getProductNameByIndex(1);

        logger.info("Selected Product 1: '" + product1Name + "'");
        logger.info("Selected Product 2: '" + product2Name + "'");

        inventoryPage.addProductToCartByIndex(0);   
        inventoryPage.addProductToCartByIndex(1);  
        String badgeCount = inventoryPage.getCartBadgeCount();
        Assert.assertEquals(badgeCount, "2",
                "STEP 4 FAILED: Cart badge should show '2' after adding two products, " +
                "but shows: '" + badgeCount + "'");

        logger.info("STEP 4 PASSED: Both products added. Cart badge = " + badgeCount);

        logger.info("STEP 5: Opening cart and verifying products...");

        inventoryPage.clickCartIcon();

        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(cartPage.isCartPageLoaded(),
                "STEP 5 FAILED: Cart page did not load. URL: " + driver.getCurrentUrl());

        Assert.assertEquals(cartPage.getCartItemCount(), 2,
                "STEP 5 FAILED: Expected 2 items in cart, but found: " + cartPage.getCartItemCount());

        Assert.assertTrue(cartPage.isProductInCart(product1Name),
                "STEP 5 FAILED: '" + product1Name + "' not found in cart.");

        Assert.assertTrue(cartPage.isProductInCart(product2Name),
                "STEP 5 FAILED: '" + product2Name + "' not found in cart.");

        logger.info("STEP 5 PASSED: Cart contains both products: ['" +
                product1Name + "', '" + product2Name + "']");

        logger.info("STEP 6: Clicking Checkout button...");

        cartPage.clickCheckout();

        logger.info("STEP 6 DONE: Checkout button clicked.");

        logger.info("STEP 7-8: Entering checkout information and clicking Continue...");

        CheckoutPage checkoutPage = new CheckoutPage(driver);

        Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(),
                "STEP 7 FAILED: Checkout information page did not load. " +
                "URL: " + driver.getCurrentUrl());

        String firstName  = ConfigReader.getProperty("checkout.firstname");
        String lastName   = ConfigReader.getProperty("checkout.lastname");
        String postalCode = ConfigReader.getProperty("checkout.zipcode");

        logger.info("Checkout info — First: " + firstName +
                " | Last: " + lastName + " | Zip: " + postalCode);

        checkoutPage.fillCheckoutInfo(firstName, lastName, postalCode);
        checkoutPage.clickContinue();

        Assert.assertFalse(checkoutPage.isErrorDisplayed(),
                "STEP 8 FAILED: Validation error shown after submitting checkout form. " +
                "Check first/last name and postal code values.");

        logger.info("STEP 7-8 PASSED: Checkout info submitted with no errors.");

        logger.info("STEP 9: Validating the Checkout Overview page...");

        OverviewPage overviewPage = new OverviewPage(driver);

        Assert.assertTrue(overviewPage.isOverviewPageLoaded(),
                "STEP 9 FAILED: Checkout Overview page did not load. " +
                "URL: " + driver.getCurrentUrl());

        int overviewItemCount = overviewPage.getOrderedItemCount();
        Assert.assertEquals(overviewItemCount, 2,
                "STEP 9a FAILED: Expected 2 products in overview, found: " + overviewItemCount);
        logger.info("STEP 9a PASSED: " + overviewItemCount + " product(s) displayed in overview.");

        Assert.assertTrue(overviewPage.isPaymentInfoDisplayed(),
                "STEP 9b FAILED: Payment Information section not visible on overview page.");
        logger.info("STEP 9b PASSED: Payment Information section is visible.");

        Assert.assertTrue(overviewPage.isShippingInfoDisplayed(),
                "STEP 9c FAILED: Shipping Information section not visible on overview page.");
        logger.info("STEP 9c PASSED: Shipping Information section is visible.");

        String totalAmount = overviewPage.getTotalAmount();
        Assert.assertTrue(overviewPage.isTotalDisplayed(),
                "STEP 9d FAILED: Total amount label is not displayed on overview page.");
        logger.info("STEP 9d PASSED: Total amount displayed: '" + totalAmount + "'");

        logger.info("STEP 9 PASSED: All overview page validations successful.");

        logger.info("STEP 10: Clicking Finish to place the order...");

        overviewPage.clickFinish();

        logger.info("STEP 10 DONE: Finish button clicked — order placed.");

        logger.info("STEP 11: Verifying order completion confirmation...");

        CompletePage completePage = new CompletePage(driver);

        Assert.assertTrue(completePage.isCompletePageLoaded(),
                "STEP 11 FAILED: Order complete page did not load. " +
                "URL: " + driver.getCurrentUrl());

        Assert.assertTrue(completePage.isOrderConfirmed(),
                "STEP 11 FAILED: Order confirmation message not found or does not " +
                "contain 'thank you'. Actual: '" + completePage.getConfirmationMessage() + "'");

        logger.info("STEP 11 PASSED: Order confirmed. Message: '" +
                completePage.getConfirmationMessage() + "'");
        logger.info("Completion sub-text: '" + completePage.getCompletionSubText() + "'");

        logger.info("STEP 12-13: Clicking 'Back Home' and verifying Products page...");

        completePage.clickBackHome();

        InventoryPage inventoryPageAfterOrder = new InventoryPage(driver);

        Assert.assertTrue(inventoryPageAfterOrder.isInventoryPageLoaded(),
                "STEP 13 FAILED: Products page did not load after clicking 'Back Home'. " +
                "URL: " + driver.getCurrentUrl());

        logger.info("STEP 12-13 PASSED: Successfully returned to Products page.");

        logger.info("STEP 14: Logging out via the hamburger menu...");

        inventoryPageAfterOrder.logout();

        LoginPage loginPageAfterLogout = new LoginPage(driver);
        Assert.assertTrue(loginPageAfterLogout.isLoginPageDisplayed(),
                "STEP 14 FAILED: Login page not displayed after logout. " +
                "URL: " + driver.getCurrentUrl());

        logger.info("STEP 14 PASSED: Logout successful. Login page is displayed.");

        logger.info("======================================================");
        logger.info("  E2E TEST: testCompletePurchaseFlow — ALL 14 STEPS PASSED");
        logger.info("======================================================");
    }
}
