package com.swaglabs.tests;

import com.swaglabs.base.BaseTest;
import com.swaglabs.pages.InventoryPage;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.reporting.ConsoleReporter;
import com.swaglabs.reporting.FileReporter;
import com.swaglabs.reporting.HtmlReporter;
import com.swaglabs.reporting.Reporter;
import com.swaglabs.utilities.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(LoginTest.class);

    private final Reporter consoleReporter = new ConsoleReporter();
    private final Reporter fileReporter    = new FileReporter();
    private final Reporter htmlReporter    = new HtmlReporter();

    private final String validUsername   = ConfigReader.getProperty("username");
    private final String validPassword   = ConfigReader.getProperty("password");
    private final String invalidUsername = ConfigReader.getProperty("invalid.username");
    private final String invalidPassword = ConfigReader.getProperty("invalid.password");
 
    @Test(priority = 1,
          description = "TC001: Valid credentials should redirect to inventory page")
    public void tc001_verifySuccessfulLogin() {
        logger.trace("TRACE: tc001_verifySuccessfulLogin() — method entered");
        logger.info("========================================");
        logger.info("TC001: Verify Successful Login — START");
        logger.info("========================================");

        logger.debug("DEBUG: Instantiating LoginPage with current driver");
        LoginPage loginPage = new LoginPage(driver);

        logger.info("[TC001] Step 1/3 - Entering valid username: " + validUsername);
        logger.info("[TC001] Step 2/3 - Entering valid password");
        logger.info("[TC001] Step 3/3 - Clicking Login button");
        loginPage.login(validUsername, validPassword);

        logger.info("[TC001] Validation: Checking current URL after login...");
        String currentUrl = driver.getCurrentUrl();
        logger.debug("DEBUG: URL after login = " + currentUrl);

        boolean loginSuccessful = currentUrl.contains("inventory");

        if (loginSuccessful) {
            logger.info("[TC001] RESULT: PASSED — URL contains 'inventory'. Actual: " + currentUrl);
            logToAllReporters("TC001", true, null);
        } else {
            logger.error("[TC001] RESULT: FAILED — Expected URL with 'inventory', got: " + currentUrl);
            logToAllReporters("TC001", false, "URL does not contain 'inventory'. Got: " + currentUrl);
        }

        Assert.assertTrue(loginSuccessful,
                "TC001 FAILED: Login did not redirect to the inventory page. URL: " + currentUrl);

        logger.info("[TC001] TC001 completed.");
        logger.trace("TRACE: tc001_verifySuccessfulLogin() — method exiting");
    }

    @Test(priority = 2,
          description = "TC002: Invalid credentials should display an error message")
    public void tc002_verifyInvalidLogin() {
        logger.trace("TRACE: tc002_verifyInvalidLogin() — method entered");
        logger.info("========================================");
        logger.info("TC002: Verify Invalid Login — START");
        logger.info("========================================");
        logger.warn("WARN: This is a NEGATIVE test — intentionally using wrong credentials");

        LoginPage loginPage = new LoginPage(driver);

        logger.info("[TC002] Step 1/3 - Entering INVALID username: " + invalidUsername);
        logger.info("[TC002] Step 2/3 - Entering INVALID password");
        logger.warn("WARN: [TC002] Using invalid credentials — error message expected");
        logger.info("[TC002] Step 3/3 - Clicking Login button");
        loginPage.login(invalidUsername, invalidPassword);

        logger.info("[TC002] Validation: Checking for error message display...");
        boolean errorShown = loginPage.isErrorDisplayed();
        String  errorText  = loginPage.getErrorMessage();

        logger.debug("DEBUG: isErrorDisplayed() = " + errorShown);
        logger.debug("DEBUG: errorText           = '" + errorText + "'");

        if (errorShown) {
            logger.info("[TC002] RESULT: PASSED — Error message displayed: '" + errorText + "'");
            logToAllReporters("TC002", true, null);
        } else {
            logger.error("[TC002] RESULT: FAILED — Error message was NOT displayed after invalid login");
            logToAllReporters("TC002", false, "Error message not displayed for invalid login");
        }

        Assert.assertTrue(errorShown,
                "TC002 FAILED: Expected error message after invalid login, but none was displayed.");
        Assert.assertFalse(errorText.isEmpty(),
                "TC002 FAILED: Error message element found but text was empty.");

        logger.info("[TC002] TC002 completed.");
    }

    @Test(priority = 3,
          description = "TC003: After login, inventory page should show 'Products' title and items")
    public void tc003_verifyInventoryPageLoaded() {
        logger.trace("TRACE: tc003_verifyInventoryPageLoaded() — method entered");
        logger.info("========================================");
        logger.info("TC003: Verify Inventory Page — START");
        logger.info("========================================");

        logger.info("[TC003] Step 1/4 - Logging in with valid credentials");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(validUsername, validPassword);
        logger.info("[TC003] Step 2/4 - Login completed, switching to InventoryPage");

        logger.debug("DEBUG: Instantiating InventoryPage");
        InventoryPage inventoryPage = new InventoryPage(driver);

        logger.info("[TC003] Step 3/4 - Verifying page title is 'Products'");
        boolean pageLoaded = inventoryPage.isInventoryPageLoaded();

        logger.info("[TC003] Step 4/4 - Counting inventory items on page");
        int itemCount = inventoryPage.getInventoryItemCount();
        logger.debug("DEBUG: itemCount = " + itemCount);

        int expectedCount = Integer.parseInt(
                ConfigReader.getProperty("expected.item.count"));
        logger.info("[TC003] Expected item count: " + expectedCount +
                " | Actual item count: " + itemCount);

        if (itemCount < expectedCount) {
            logger.warn("WARN: Item count " + itemCount +
                    " is less than expected " + expectedCount +
                    " — page may be partially loaded.");
        }

        if (pageLoaded && itemCount > 0) {
            logger.info("[TC003] RESULT: PASSED — Page title = 'Products', Items = " + itemCount);
            logToAllReporters("TC003", true, null);
        } else {
            logger.error("[TC003] RESULT: FAILED — pageLoaded=" + pageLoaded +
                    ", itemCount=" + itemCount);
            logToAllReporters("TC003", false,
                    "pageLoaded=" + pageLoaded + ", itemCount=" + itemCount);
        }

        Assert.assertTrue(pageLoaded,
                "TC003 FAILED: Inventory page title is not 'Products'.");
        Assert.assertTrue(itemCount > 0,
                "TC003 FAILED: No inventory items visible on the page.");

        logger.info("[TC003] TC003 completed.");
    }

    @Test(priority = 4,
          description = "TC004: Clicking logout should return the user to the login page")
    public void tc004_verifyLogoutFunctionality() {
        logger.trace("TRACE: tc004_verifyLogoutFunctionality() — method entered");
        logger.info("========================================");
        logger.info("TC004: Verify Logout Functionality — START");
        logger.info("========================================");

        logger.info("[TC004] Step 1/5 - Logging in...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(validUsername, validPassword);
        logger.info("[TC004] Step 2/5 - Login successful.");

        InventoryPage inventoryPage = new InventoryPage(driver);
        logger.info("[TC004] Step 3/5 - Opening hamburger menu...");
        logger.info("[TC004] Step 4/5 - Clicking Logout link...");
        inventoryPage.logout();

        logger.info("[TC004] Step 5/5 - Verifying URL after logout...");
        String currentUrl = driver.getCurrentUrl();
        logger.debug("DEBUG: URL after logout = " + currentUrl);

        String expectedLoginUrl = ConfigReader.getProperty("login.page.url");
        logger.debug("DEBUG: Expected login URL = " + expectedLoginUrl);

        boolean logoutSuccessful = currentUrl.trim().equals(expectedLoginUrl) ||
                                   (!currentUrl.contains("inventory") &&
                                    currentUrl.contains("saucedemo.com"));

        boolean loginPageVisible = loginPage.isLoginPageDisplayed();
        logger.info("[TC004] Login page visible after logout: " + loginPageVisible);

        if (logoutSuccessful && loginPageVisible) {
            logger.info("[TC004] RESULT: PASSED — Logout successful. URL: " + currentUrl);

            logger.fatal("FATAL demo (Assignment 7 requirement): This level would " +
                    "indicate a critical, unrecoverable system failure — " +
                    "e.g., ChromeDriver binary not found, config file missing.");

            logToAllReporters("TC004", true, null);
        } else {
            logger.error("[TC004] RESULT: FAILED — Expected login URL '" +
                    expectedLoginUrl + "', got '" + currentUrl + "'");
            logToAllReporters("TC004", false,
                    "Logout did not redirect to login page. URL: " + currentUrl);
        }

        Assert.assertTrue(logoutSuccessful,
                "TC004 FAILED: After logout, URL is not the login page. Got: " + currentUrl);

        logger.info("[TC004] TC004 completed.");
        logger.trace("TRACE: tc004_verifyLogoutFunctionality() — method exiting");
    }

    @AfterClass
    public void printFinalReports() {
        logger.info("==============================================");
        logger.info("  ALL TESTS COMPLETED — Printing Summaries");
        logger.info("==============================================");

        System.out.println("\n\n=== REPORTER SUMMARIES (Assignment 8) ===");

        consoleReporter.printSummary();
        fileReporter.printSummary();
        htmlReporter.printSummary();

        logger.info("All summaries printed. Check test-results/ for report files.");
    }

    private void logToAllReporters(String tcId, boolean passed, String reason) {
        logger.debug("DEBUG: logToAllReporters(" + tcId + ", passed=" + passed + ")");

        Reporter[] reporters = { consoleReporter, fileReporter, htmlReporter };

        for (Reporter reporter : reporters) {
            if (passed) {
                reporter.logPass(tcId);          
            } else {
                reporter.logFail(tcId, reason);  
            }
        }
    }
}
