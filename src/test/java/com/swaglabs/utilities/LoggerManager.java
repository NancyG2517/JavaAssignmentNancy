package com.swaglabs.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager {

    private static final Logger logger = LogManager.getLogger(LoggerManager.class);

    private LoggerManager() {
        throw new IllegalStateException("Utility class — do not instantiate.");
    } 

    public static Logger getLogger(Class<?> clazz) {
        logger.trace("TRACE: Creating logger for class: " + clazz.getName());
        return LogManager.getLogger(clazz);
    }

    public static void demonstrateAllLogLevels() {
        System.out.println();

        logger.trace("TRACE: Entering setUp() method — WebDriver initialisation starting");

        logger.debug("DEBUG: ChromeOptions configured — headless=false, maximized=true");

        logger.info("INFO:  Browser launched. Navigating to https://www.saucedemo.com/");

        logger.warn("WARN:  Login button took 4.5 seconds to become clickable (threshold: 3s)");

        logger.error("ERROR: Inventory page title mismatch. Expected='Products', Actual=''");

        logger.fatal("FATAL: WebDriver session terminated unexpectedly. No further tests possible.");

        System.out.println();
    }
}
