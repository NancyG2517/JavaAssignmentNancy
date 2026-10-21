package com.swaglabs.base;

import com.swaglabs.utilities.ConfigReader;
import com.swaglabs.utilities.LoggerManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseTest {

    protected static final Logger logger = LoggerManager.getLogger(BaseTest.class);

    protected WebDriver driver;

    @BeforeSuite
    public void suiteSetup() {
        logger.info("=======================================================");
        logger.info("  SwagLabs Automation Suite — Assignments 7 & 8 START");
        logger.info("=======================================================");

        LoggerManager.demonstrateAllLogLevels();
    }

    @BeforeMethod
    public void setUp() {
        logger.trace("TRACE: Entering setUp() — test isolation browser launch");
        logger.debug("DEBUG: Reading 'app.url' from config.properties");

        logger.info("Launching Chrome browser via WebDriverManager...");
        WebDriverManager.chromedriver().setup();
 
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");

        // Disable Chrome's "password found in data breach" popup
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);


        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(Long.parseLong(
                        ConfigReader.getProperty("implicit.wait"))));
        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(Long.parseLong(
                        ConfigReader.getProperty("page.load.timeout"))));

        logger.info("Chrome browser launched and maximized successfully.");
        logger.debug("DEBUG: WebDriver class = " + driver.getClass().getSimpleName());

        String url = ConfigReader.getProperty("app.url");
        logger.info("Navigating browser to URL: " + url);
        driver.get(url);

        logger.info("Page loaded. Current URL: " + driver.getCurrentUrl());
        logger.trace("TRACE: setUp() completed — driver is ready for use");
    }

    @AfterMethod
    public void tearDown() {
        logger.debug("DEBUG: Entering tearDown() — closing browser");

        if (driver != null) {
            logger.info("Closing browser...");
            driver.quit();
            driver = null;
            logger.info("Browser closed. Driver set to null.");
        } else {
            logger.warn("WARN: driver was null in tearDown(). " +
                    "Browser may have already been closed or setUp() failed.");
        }

        logger.trace("TRACE: tearDown() completed");
    }

    public WebDriver getDriver() {
        return driver;
    }
}
